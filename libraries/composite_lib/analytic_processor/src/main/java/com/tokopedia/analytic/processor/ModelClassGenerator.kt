package com.tokopedia.analytic.processor

import com.squareup.javapoet.*
import com.tokopedia.analytic.annotation.CustomChecker
import com.tokopedia.analytic.annotation.DefinedInCollections
import javax.lang.model.element.Modifier

// This class is used to generate the model bundler classes
class ModelClassGenerator(clazz: AnnotatedModelClass) : ClassGenerator(clazz) {
    override val getBundleFuncBuilder: MethodSpec.Builder = MethodSpec
        .methodBuilder("getBundle")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addParameter(
                ParameterGenerator.createParameter(
                        clazz.getClassName().decapitalize(),
                        TypeName.get(clazz.element.asType())
                )
        )
        .addStatement(
            "\$T $BUNDLE_NAME = new \$T()",
            bundleClassName,
            bundleClassName
        )

    override fun addCheckerStatement(field: ModelClassField, putStatement: CodeBlock): CodeBlock.Builder {
        val checkerStatement = CodeBlock.builder()

        val fieldTypeName = TypeName.get(field.element.asType()) // tipe

        //  double price = productListImpressionProduct.getPrice();
        if (field.element.getAnnotation(DefinedInCollections::class.java) == null) {
            checkerStatement.addStatement(
                    "\$T \$L = \$N",
                    fieldTypeName,
                    field.key,
                    getValueStatement(field)
            )
        } else {
            checkerStatement.addStatement(
                    "\$T \$L = \$N",
                    fieldTypeName,
                    field.element.simpleName,
                    getValueStatement(field)
            )
        }

        if (field.element.getAnnotation(CustomChecker::class.java) != null) {
            createCustomCheckerCondition(field, checkerStatement, false)
            checkerStatement.add(putStatement)
            createCheckerSuccessBlock(field, checkerStatement)
            createCheckerFailedBlock(field, checkerStatement)
            checkerStatement.endControlFlow()
        } else {
            checkerStatement.add(putStatement)
        }
        return checkerStatement
    }

    override val getBundleFromMap: MethodSpec.Builder = MethodSpec
        .methodBuilder("getBundle")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addParameter(
                ParameterGenerator.createParameterizedParameter(
                        "data",
                        ClassName.get(Map::class.java),
                        TypeName.get(String::class.java),
                        ClassName.get("java.lang", "Object")
                )
        )
        .addStatement(
            "\$T $BUNDLE_NAME = new \$T()",
            bundleClassName,
            bundleClassName
        )
        .beginControlFlow("try ")

    override fun generate(): JavaFile {
        clazz.fields.forEach {
            getBundleFuncBuilder.addCode(createPutStatement(it.value))
            getBundleFromMap.addCode(createPutStatementFromMap(it.value))
        }

        getBundleFromMap
            .nextControlFlow("catch (\$T e) ", ClassName.get("java.lang", "ClassCastException"))
            .addStatement(
                "\$T.e(\$S, e.getMessage())",
                ClassName.get("android.util", "Log"),
                "MapBundler"
            )
            .endControlFlow()

        return JavaFile.builder(
            clazz.pack,
            classBuilder.addMethod(
                getBundleFuncBuilder
                    .addStatement("return bundle")
                    .returns(bundleClassName)
                    .build()
            )
                .addMethod(getBundleFromMap.addStatement("return bundle").returns(bundleClassName).build())
                .build()
        )
            .indent("    ")
            .build()
    }
}