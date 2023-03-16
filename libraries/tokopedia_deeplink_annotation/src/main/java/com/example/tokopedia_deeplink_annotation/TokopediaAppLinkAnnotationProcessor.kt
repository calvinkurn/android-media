package com.example.tokopedia_deeplink_annotation

import com.google.auto.service.AutoService
import com.squareup.javapoet.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types


@AutoService(Processor::class)
class TokopediaAppLinkAnnotationProcessor : AbstractProcessor() {

    companion object {
        var messenger: Messager? = null
        var filer: Filer? = null
        var typeUtils: Types? = null
        var elementUtils: Elements? = null
    }

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
        val elementsWithTokopediaAppLinks = roundEnv?.getElementsAnnotatedWith(
            TokopediaAppLinks::class.java
        ) ?: listOf()
        val elementsWithTokopediaAppLink = roundEnv?.getElementsAnnotatedWith(
            TokopediaAppLink::class.java
        ) ?: listOf()
        val mappedElement = mutableListOf<Element>().apply {
            addAll(elementsWithTokopediaAppLinks)
            addAll(elementsWithTokopediaAppLink)
        }
        val typeSpecBuilder = TypeSpec.classBuilder("TokopediaAppLinkMapper")
        val list = ClassName.get("java.util", "List")
        val arrayList = ClassName.get("java.util", "ArrayList")

        val dlpClassName: ClassName = ClassName.get("com.tokopedia.applink", "DLP")
        val function4ClassName: ClassName = ClassName.get("kotlin.jvm.functions", "Function4")
        val contextClassName: ClassName = ClassName.get("android.content", "Context")
        val uriClassName: ClassName = ClassName.get("android.net", "Uri")
        val stringClassName: ClassName = ClassName.get("java.lang", "String")


        val parameterizedArrayListDlp: TypeName = ParameterizedTypeName.get(arrayList, dlpClassName)
        val parameterizedListString: TypeName = ParameterizedTypeName.get(list, stringClassName)
        val parameterizedFunction4: TypeName = ParameterizedTypeName.get(
            function4ClassName,
            contextClassName,
            uriClassName,
            stringClassName,
            parameterizedListString,
            stringClassName
        )


        val listType: TypeName =
            ParameterizedTypeName.get(list, dlpClassName)
        val operationNameListMethodBuilder = MethodSpec.methodBuilder("listCustomerAppMappedAppLink")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(listType)
            .addStatement("\$T list = new \$T<>()", parameterizedArrayListDlp, arrayList)

        mappedElement.forEach { element ->
            val listTokopediaAppLinkDetails = element.getAnnotation(TokopediaAppLinks::class.java)
            val listTokopediaAppLinkDetail = element.getAnnotation(TokopediaAppLink::class.java)
            val mappedTokopediaAppLinkDetail = mutableListOf<TokopediaAppLink>().apply {
                addAll(listTokopediaAppLinkDetails?.value?.toList() ?: listOf())
                if(listTokopediaAppLinkDetail != null) {
                    add(listTokopediaAppLinkDetail)
                }
            }
            mappedTokopediaAppLinkDetail.forEachIndexed { index, tokopediaAppLinkDetail ->
                val interfaceTypeMirror =
                    elementUtils?.getTypeElement("com.tokopedia.applink.CustomAppLinkMapping")
                        ?.asType()
                val elementTypeMirror = element.asType()
                val res = typeUtils?.isAssignable(elementTypeMirror, interfaceTypeMirror)
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
                        if (index == 0 && res == true) {
                            val customMappingClassName: ClassName = ClassName.get(
                                "com.tokopedia.applink",
                                element.simpleName.toString()
                            )
                            methodSpec.addStatement(
                                "return \$T.INSTANCE.customDest(context, uri, s, strings)",
                                customMappingClassName
                            )
                        } else {
                            methodSpec.addStatement(
                                "return \$S",
                                tokopediaAppLinkDetail.internalAppLink
                            )
                        }
                    }.build()
                val dlpLogicClassName: ClassName =
                    ClassName.get("com.tokopedia.applink", tokopediaAppLinkDetail.dlpLogic)
                operationNameListMethodBuilder.addStatement(
                    "list.add(new \$T(new \$T(\$S), \$L))",
                    dlpClassName,
                    dlpLogicClassName,
                    tokopediaAppLinkDetail.matchedAppLink,
                    TypeSpec.anonymousClassBuilder("").addSuperinterface(parameterizedFunction4)
                        .addMethod(methodInvoke).build()
                )
            }
        }.also {
            if (mappedElement.size > 0) {
                operationNameListMethodBuilder.addStatement("return list")
                typeSpecBuilder.addMethod(operationNameListMethodBuilder.build())
                val typeSpec = typeSpecBuilder.build()

                val javaFile2 = JavaFile.builder(
                    "com.tokopedia.applink",
                    typeSpec
                ).build()
                javaFile2.writeTo(filer)
            }
        }
        return true
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
