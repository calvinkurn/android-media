package com.tokopedia.tokopedia_applink_annotation_processor

import com.google.auto.service.AutoService
import com.squareup.javapoet.*
import com.tokopedia.tokopedia_applink_annotation.start.StartMatcher
import com.tokopedia.tokopedia_applink_annotation.start.StartsMatcher
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import com.tokopedia.tokopedia_applink_annotation.exact.ExactMatcher
import com.tokopedia.tokopedia_applink_annotation.exact.ExactsMatcher
import com.tokopedia.tokopedia_applink_annotation.host.HostMatcher
import com.tokopedia.tokopedia_applink_annotation.host.HostsMatcher
import com.tokopedia.tokopedia_applink_annotation.match.MatchPatternMatcher
import com.tokopedia.tokopedia_applink_annotation.match.MatchesPatternMatcher

@AutoService(Processor::class)
class TokopediaAppLinkAnnotationProcessor : AbstractProcessor() {

    companion object {
        private const val MAPPER_CLASS_NAME = "TokopediaAppLinkMapper"
    }

    private val list = ClassName.get("java.util", "List")
    private val arrayList = ClassName.get("java.util", "ArrayList")
    private val dlpClassName: ClassName = ClassName.get("com.tokopedia.applink", "DLP")
    private val function4ClassName: ClassName = ClassName.get("kotlin.jvm.functions", "Function4")
    private val contextClassName: ClassName = ClassName.get("android.content", "Context")
    private val uriClassName: ClassName = ClassName.get("android.net", "Uri")
    private val stringClassName: ClassName = ClassName.get("java.lang", "String")
    private val uriUtilClassName: ClassName = ClassName.get("com.tokopedia.applink", "UriUtil")

    private val parameterizedArrayListDlp: TypeName = ParameterizedTypeName.get(
        arrayList,
        dlpClassName
    )
    private val parameterizedListString: TypeName = ParameterizedTypeName.get(list, stringClassName)
    private val parameterizedFunction4: TypeName = ParameterizedTypeName.get(
        function4ClassName,
        contextClassName,
        uriClassName,
        stringClassName,
        parameterizedListString,
        stringClassName
    )
    private val listType: TypeName = ParameterizedTypeName.get(list, dlpClassName)
    private var messenger: Messager? = null
    private var filer: Filer? = null
    private var typeUtils: Types? = null
    private var elementUtils: Elements? = null

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        messenger = processingEnv?.messager
        filer = processingEnv?.filer
        typeUtils = processingEnv?.typeUtils
        elementUtils = processingEnv?.elementUtils
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        val matchedElementWithAnnotation = getListElementWithAnnotation(roundEnv)
        val mapperClass = createMapperClass()
        val mapperMethod = createMapperMethodHeader()
        matchedElementWithAnnotation.forEach { element ->
            getListMatcherAppLinkAnnotation(element).forEachIndexed { index, matcherAnnotation ->
                createMapperFromAnnotation(matcherAnnotation, mapperMethod)
            }
        }.also {
            if (matchedElementWithAnnotation.isNotEmpty()) {
                finishGenerateMapperClass(mapperClass, mapperMethod)
            }
        }
        return true
    }

    private fun finishGenerateMapperClass(
        mapperClass: TypeSpec.Builder?,
        mapperMethod: MethodSpec.Builder?
    ) {
        mapperMethod?.addStatement("return list")
        mapperClass?.addMethod(mapperMethod?.build())
        JavaFile.builder(
            "com.tokopedia.applink",
            mapperClass?.build()
        ).build().run {
            writeTo(filer)
        }
    }

    private fun createMapperFromAnnotation(
        matcherAnnotation: Annotation,
        mapperMethod: MethodSpec.Builder?
    ) {
        val methodInvoke = MethodSpec.methodBuilder("invoke")
            .addAnnotation(Override::class.java)
            .addModifiers(
                Modifier.PUBLIC
            )
            .addParameter(contextClassName, "context")
            .addParameter(uriClassName, "uri")
            .addParameter(stringClassName, "s")
            .addParameter(parameterizedListString, "strings")
            .returns(stringClassName).also { methodSpec ->
                when (matcherAnnotation) {
                    is HostMatcher -> {
                        methodSpec.addStatement(
                            "return \$S",
                            matcherAnnotation.destinationAppLink
                        )
                    }
                    is ExactMatcher -> {
                        methodSpec.addStatement(
                            "return \$S",
                            matcherAnnotation.destinationAppLink
                        )
                    }
                    is MatchPatternMatcher -> {
                        methodSpec.addStatement(
                            "return \$T.buildUri(\$S, strings.toArray(new \$T[strings.size()]))",
                            uriUtilClassName,
                            matcherAnnotation.destinationAppLink,
                            stringClassName
                        )
                    }
                    is StartMatcher -> {
                        methodSpec.addStatement(
                            "return \$S",
                            matcherAnnotation.destinationAppLink
                        )
                    }
                }
            }.build()
        val dlpLogicClassName: ClassName =
            ClassName.get("com.tokopedia.applink", getDlpLogic(matcherAnnotation))
        val matchedAppLink = getListMatchedAppLink(matcherAnnotation)
        mapperMethod?.addStatement(
            "list.add(new \$T(new \$T(\$S), \$L))",
            dlpClassName,
            dlpLogicClassName,
            matchedAppLink,
            TypeSpec.anonymousClassBuilder("").addSuperinterface(parameterizedFunction4)
                .addMethod(methodInvoke).build()
        )
    }

    private fun getListMatchedAppLink(matcherAnnotation: Annotation): String {
        when (matcherAnnotation) {
            is HostMatcher -> {
                return matcherAnnotation.matchesAppLink
            }
            is ExactMatcher -> {
                return matcherAnnotation.matchesAppLink
            }
            is MatchPatternMatcher -> {
                return matcherAnnotation.matchesAppLink
            }
            is StartMatcher -> {
                return matcherAnnotation.matchesAppLink
            }
            else -> {
                return ""
            }
        }
    }

    private fun getDlpLogic(matcherAnnotation: Annotation): String {
        when (matcherAnnotation) {
            is HostMatcher -> {
                return "Host"
            }
            is ExactMatcher -> {
                return "Exact"
            }
            is MatchPatternMatcher -> {
                return "MatchPattern"
            }
            is StartMatcher -> {
                return "StartsWith"
            }
            else -> {
                return ""
            }
        }
    }

    private fun createMapperMethodHeader(): MethodSpec.Builder? {
        return MethodSpec.methodBuilder("listCustomerAppMappedAppLink")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(listType)
            .addStatement("\$T list = new \$T<>()", parameterizedArrayListDlp, arrayList)
    }

    private fun createMapperClass(): TypeSpec.Builder? {
        return TypeSpec.classBuilder(MAPPER_CLASS_NAME)
    }

    private fun getListMatcherAppLinkAnnotation(element: Element): MutableList<Annotation> {
        return mutableListOf<Annotation>().apply {
            addAll(getListHostMatcherAnnotation(element).reversed())
            addAll(getListExactMatcherAnnotation(element).reversed())
            addAll(getListMatchMatcherAnnotation(element).reversed())
            addAll(getListStartMatcherAnnotation(element).reversed())
        }
    }

    private fun getListStartMatcherAnnotation(element: Element): Collection<Annotation> {
        val listStartMatcherAnnotation = element.getAnnotation(StartsMatcher::class.java)
        val startMatcherAnnotation = element.getAnnotation(StartMatcher::class.java)
        return mutableListOf<Annotation>().apply {
            addAll(listStartMatcherAnnotation?.value?.toList() ?: listOf())
            if (startMatcherAnnotation != null) {
                add(startMatcherAnnotation)
            }
        }
    }

    private fun getListMatchMatcherAnnotation(element: Element): Collection<Annotation> {
        val listMatchMatcherAnnotation = element.getAnnotation(MatchesPatternMatcher::class.java)
        val matchMatcherAnnotation = element.getAnnotation(MatchPatternMatcher::class.java)
        return mutableListOf<Annotation>().apply {
            addAll(listMatchMatcherAnnotation?.value?.toList() ?: listOf())
            if (matchMatcherAnnotation != null) {
                add(matchMatcherAnnotation)
            }
        }
    }

    private fun getListExactMatcherAnnotation(element: Element): Collection<Annotation> {
        val listExactMatcherAnnotation = element.getAnnotation(ExactsMatcher::class.java)
        val exactMatcherMatcherAnnotation = element.getAnnotation(ExactMatcher::class.java)
        return mutableListOf<Annotation>().apply {
            addAll(listExactMatcherAnnotation?.value?.toList() ?: listOf())
            if (exactMatcherMatcherAnnotation != null) {
                add(exactMatcherMatcherAnnotation)
            }
        }
    }

    private fun getListHostMatcherAnnotation(element: Element): Collection<Annotation> {
        val listHostMatcherAnnotation = element.getAnnotation(HostsMatcher::class.java)
        val hostMatcherMatcherAnnotation = element.getAnnotation(HostMatcher::class.java)
        return mutableListOf<Annotation>().apply {
            addAll(listHostMatcherAnnotation?.value?.toList() ?: listOf())
            if (hostMatcherMatcherAnnotation != null) {
                add(hostMatcherMatcherAnnotation)
            }
        }
    }

    private fun getListElementWithAnnotation(roundEnv: RoundEnvironment?): Collection<Element> {
        return roundEnv?.getElementsAnnotatedWithAny(setOf(
            HostsMatcher::class.java,
            HostMatcher::class.java,
            ExactMatcher::class.java,
            ExactMatcher::class.java,
            MatchPatternMatcher::class.java,
            StartMatcher::class.java
        )).orEmpty()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            HostMatcher::class.java.canonicalName,
            HostsMatcher::class.java.canonicalName,
            ExactMatcher::class.java.canonicalName,
            ExactsMatcher::class.java.canonicalName,
            MatchPatternMatcher::class.java.canonicalName,
            MatchesPatternMatcher::class.java.canonicalName,
            StartMatcher::class.java.canonicalName,
            StartsMatcher::class.java.canonicalName
        )
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }
}
