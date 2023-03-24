package com.tokopedia.tokopedia_applink_annotation_processor

import com.google.auto.service.AutoService
import com.squareup.javapoet.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import com.tokopedia.tokopedia_applink_annotation.TokopediaAppLink
import com.tokopedia.tokopedia_applink_annotation.TokopediaAppLinks

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
    private val collectionsClassName: ClassName = ClassName.get("java.util", "Collections")
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
        val matchedElementWithAnnotation = getElementsWithTokopediaAppLinkAnnotation(roundEnv)
        val mapperClass = createMapperClass()
        val mapperMethod = createMapperMethodHeader()
        matchedElementWithAnnotation.forEach { element ->
            val listTokopediaAppLinkAnnotation = getListTokopediaAppLinkAnnotation(element)
            listTokopediaAppLinkAnnotation.forEachIndexed { index, tokopediaAppLinkAnnotation ->
                createMapperFromAnnotation(element, index, tokopediaAppLinkAnnotation, mapperMethod)
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
        mapperMethod?.addStatement("\$T.reverse(list)", collectionsClassName)
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
        element: Element,
        index: Int,
        tokopediaAppLinkAnnotation: TokopediaAppLink,
        mapperMethod: MethodSpec.Builder?
    ) {
        val interfaceCustomLogic = elementUtils?.getTypeElement(
            "com.tokopedia.applink.CustomAppLinkMapping"
        )?.asType()
        val elementType = element.asType()
        val isElementForCustomAppLinkMapperLogic =
            typeUtils?.isAssignable(elementType, interfaceCustomLogic)
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
                if (index == 0 && isElementForCustomAppLinkMapperLogic == true) {
                    val customMappingClassName: ClassName = ClassName.get(
                        "com.tokopedia.applink",
                        element.simpleName.toString()
                    )
                    methodSpec.addStatement(
                        "return \$T.INSTANCE.customDest(context, uri, s, strings)",
                        customMappingClassName
                    )
                } else {
                    if (tokopediaAppLinkAnnotation.dlpLogic == "MatchPattern") {
                        methodSpec.addStatement(
                            "return \$T.buildUri(\$S, strings.toArray(new \$T[strings.size()]))",
                            uriUtilClassName,
                            tokopediaAppLinkAnnotation.internalAppLink,
                            stringClassName
                        )
                    } else {
                        methodSpec.addStatement(
                            "return \$S",
                            tokopediaAppLinkAnnotation.internalAppLink
                        )
                    }
                }
            }.build()
        val dlpLogicClassName: ClassName =
            ClassName.get("com.tokopedia.applink", tokopediaAppLinkAnnotation.dlpLogic)
        mapperMethod?.addStatement(
            "list.add(new \$T(new \$T(\$S), \$L))",
            dlpClassName,
            dlpLogicClassName,
            tokopediaAppLinkAnnotation.matchedAppLink,
            TypeSpec.anonymousClassBuilder("").addSuperinterface(parameterizedFunction4)
                .addMethod(methodInvoke).build()
        )
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

    private fun getListTokopediaAppLinkAnnotation(element: Element): MutableList<TokopediaAppLink> {
        val listTokopediaAppLinksAnnotation = element.getAnnotation(TokopediaAppLinks::class.java)
        val listTokopediaAppLinkAnnotation = element.getAnnotation(TokopediaAppLink::class.java)
        return mutableListOf<TokopediaAppLink>().apply {
            addAll(listTokopediaAppLinksAnnotation?.value?.toList() ?: listOf())
            if (listTokopediaAppLinkAnnotation != null) {
                add(listTokopediaAppLinkAnnotation)
            }
        }
    }

    private fun getElementsWithTokopediaAppLinkAnnotation(roundEnv: RoundEnvironment?): Collection<Element> {
        val elementsWithTokopediaAppLinksAnnotation = roundEnv?.getElementsAnnotatedWith(
            TokopediaAppLinks::class.java
        ) ?: listOf()
        val elementsWithTokopediaAppLinkAnnotation = roundEnv?.getElementsAnnotatedWith(
            TokopediaAppLink::class.java
        ) ?: listOf()
        val mappedElement = mutableListOf<Element>().apply {
            addAll(elementsWithTokopediaAppLinksAnnotation)
            addAll(elementsWithTokopediaAppLinkAnnotation)
        }
        return mappedElement
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            TokopediaAppLink::class.java.canonicalName,
            TokopediaAppLinks::class.java.canonicalName
        )
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }
}
