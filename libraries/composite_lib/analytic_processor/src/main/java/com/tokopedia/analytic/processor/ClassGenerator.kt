package com.tokopedia.analytic.processor

import com.squareup.javapoet.*
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.Type
import com.tokopedia.analytic.annotation.*
import com.tokopedia.analytic.processor.utils.*
import java.io.Serializable
import java.lang.reflect.Constructor
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.ElementFilter
import javax.tools.Diagnostic

abstract class ClassGenerator(
    val clazz: AnnotatedModelClass
) {

    companion object {
        const val BUNDLE_NAME = "bundle"
        const val objectTypeConditionCheckFormat = "\$T.INSTANCE.\$N(\$L)"
        const val mapTypeConditionCheckFormat = "\$T.INSTANCE.\$N((\$T) \$L)"

        val bundleClassName = ClassName.get("android.os", "Bundle")
        val iteratorClassName = ClassName.get("java.util", "Iterator")
        val collectionClassName = ClassName.get("java.util", "Collection")
        val arrayListClassName = ClassName.get("java.util", "ArrayList")
        val listClassName = ClassName.get("java.util", "List")
        val setClassName = ClassName.get("java.util", "Set")
        val mapClassName = ClassName.get("java.util", "Map")
        val entryClassName = ClassName.get("java.util.Map", "Entry")
        val levelClassName: ClassName = ClassName.get("com.tokopedia.analytic.annotation", "Level")
        val bundlerUtilClassName: ClassName = ClassName.get("com.tokopedia.gtmutil", "BundlerUtil")
    }

    private val CLASS_NAME_SUFFIX = "Bundler"
    private val CUSTOM_CHECKER_NAME_SUFFIX = "Checkers"

    protected val classBuilder: TypeSpec.Builder
    private val customCheckerClass: TypeSpec.Builder
    protected abstract val getBundleFuncBuilder: MethodSpec.Builder
    protected abstract val getBundleFromMap: MethodSpec.Builder

    init {
        classBuilder =
            TypeSpec.classBuilder("${clazz.getClassName()}$CLASS_NAME_SUFFIX")
                .addOriginatingElement(clazz.element)
                .addModifiers(Modifier.PUBLIC)

        customCheckerClass =
                TypeSpec.interfaceBuilder("${clazz.getClassName()}$CUSTOM_CHECKER_NAME_SUFFIX")
                        .addOriginatingElement(clazz.element)
                        .addModifiers(Modifier.PUBLIC)
    }

    abstract fun generate(): JavaFile

    fun hasCustomChecker() = customCheckerClass.build().methodSpecs.isNotEmpty()

    fun generateCheckerInterface(): JavaFile {
        val customCheckerClass = customCheckerClass.build()
        return JavaFile.builder(
                clazz.pack,
                customCheckerClass
        )
                .indent("    ")
                .build()
    }

    // this function is called for every class field to generate a put statement based on it's type
    protected fun createPutStatement(field: ModelClassField): CodeBlock {
        val fieldTypeName = TypeName.get(field.element.asType())
        val putStatement = if (isRawType(fieldTypeName)) {
            createPutRawStatement(field)
        } else if (isBundleable(field.element)) {
            createPutBundleStatement(field)
        } else if (isParcelable(field.element)) {
            createPutParcelableStatement(field)
        } else if (isList(fieldTypeName) || isSet(fieldTypeName)) {
            createPutListStatement(field)
        } else if (isMap(fieldTypeName)) {
            createPutMapStatement(field)
        } else if (isSerializable(field.element)) {
            createPutSerializableStatement(field)
        } else {
            throw IllegalArgumentException("Unknown put statement")
        }

        val checkerStatement = addCheckerStatement(field, putStatement)

        return checkerStatement.build()
    }

    open fun addCheckerStatement(field: ModelClassField, putStatement: CodeBlock): CodeBlock.Builder {
        val checkerStatement = CodeBlock.builder()
        // just put code outside checjer
        checkerStatement.add(putStatement)
        if (field.element.getAnnotation(CustomChecker::class.java) != null) {
            createCustomCheckerCondition(field, checkerStatement, false)
            createCheckerSuccessBlock(field, checkerStatement)
            createCheckerFailedBlock(field, checkerStatement)
            checkerStatement.endControlFlow()
        }
        return checkerStatement
    }

    protected fun createCheckerFailedBlock(field: ModelClassField, checkerStatement: CodeBlock.Builder) {
        val loggerAnnotation = clazz.element.getAnnotation(Logger::class.java)
        val customCheckerAnnotation = field.element.getAnnotation(CustomChecker::class.java)
        var checkerClass: TypeMirror? = null
        try {
            customCheckerAnnotation.checkerClass
        } catch (e: MirroredTypeException) {
            checkerClass = e.typeMirror
        }
        if (checkerClass == null) AnnotationProcessor.messenger?.printMessage(Diagnostic.Kind.ERROR, "Checker class not found", clazz.element)
        if (loggerAnnotation != null) {
            var loggerClass: TypeMirror? = null
            try {
                loggerAnnotation.loggerClass
            } catch (e: MirroredTypeException) {
                loggerClass = e.typeMirror
            }
            if (loggerClass == null) AnnotationProcessor.messenger?.printMessage(Diagnostic.Kind.ERROR, "Logger class not found", clazz.element)
            checkerStatement.nextControlFlow("else")
            checkerStatement.addStatement("\$T.INSTANCE.log(\$T.\$L, \$S)",
                    ClassName.get(loggerClass),
                    levelClassName,
                    customCheckerAnnotation.level,
                    "${checkerClass.toString().split(".").last()} return false when checking ${field.element.asType()} ${field.key} using ${customCheckerAnnotation.functionName.joinToString(", ")}")

            val errorHandlerAnnotation = clazz.element.getAnnotation(ErrorHandler::class.java)
            var errorHandlerClass: TypeMirror? = null
            if (errorHandlerAnnotation != null) {
                try {
                    errorHandlerAnnotation.errorHandler
                } catch (e: MirroredTypeException) {
                    errorHandlerClass = e.typeMirror
                }
            } else {
                AnnotationProcessor.messenger?.printMessage(Diagnostic.Kind.ERROR, "You must specify an error handler when using custom checker with ERROR level", clazz.element)
            }
            if (customCheckerAnnotation.level == Level.ERROR && errorHandlerClass == null) {
                AnnotationProcessor.messenger?.printMessage(Diagnostic.Kind.ERROR, "Error handler class not found", clazz.element)
            }

            if (customCheckerAnnotation.level == Level.ERROR)
                checkerStatement.addStatement("\$T.INSTANCE.onError(new \$T(\$S))",
                    ClassName.get(errorHandlerClass),
                    ClassName.get("java.lang", "Exception"),
                    "${checkerClass.toString().split(".").last()} return false when checking ${field.element.asType()} ${field.key} using ${customCheckerAnnotation.functionName.joinToString(", ")}")
        }
    }

    protected fun createCheckerSuccessBlock(field: ModelClassField, checkerStatement: CodeBlock.Builder) {
        val loggerAnnotation = clazz.element.getAnnotation(Logger::class.java)
        val customCheckerAnnotation = field.element.getAnnotation(CustomChecker::class.java)
        if (loggerAnnotation != null) {
            var loggerClass: TypeMirror? = null
            try {
                loggerAnnotation.loggerClass
            } catch (e: MirroredTypeException) {
                loggerClass = e.typeMirror
            }
            if (loggerClass == null) AnnotationProcessor.messenger?.printMessage(Diagnostic.Kind.ERROR, "Logger class not found", clazz.element)
            var checkerClass: TypeMirror? = null
            try {
                customCheckerAnnotation.checkerClass
            } catch (e: MirroredTypeException) {
                checkerClass = e.typeMirror
            }
            if (checkerClass == null) AnnotationProcessor.messenger?.printMessage(Diagnostic.Kind.ERROR, "Checker class not found", clazz.element)
            checkerStatement.addStatement("\$T.INSTANCE.log(\$T.\$L, \$S)",
                    ClassName.get(loggerClass),
                    levelClassName,
                    Level.SUCCESS,
                    "${checkerClass.toString().split(".").last()} successfully check ${field.element.asType()} ${field.key} using ${customCheckerAnnotation.functionName.joinToString(", ")}")
        } else {
            AnnotationProcessor.messenger?.printMessage(Diagnostic.Kind.ERROR, "Something went wrong when generating checker", clazz.element)
        }
    }

    private fun createPutSerializableStatement(field: ModelClassField): CodeBlock {
        val statementBuilder = CodeBlock.builder()

        statementBuilder.addStatement(
            "\$N.putSerializable(\$S, \$N)",
            BUNDLE_NAME,
            field.key,
            getValueStatement(field)
        )

        return statementBuilder.build()
    }

    protected fun createPutStatementFromMap(field: ModelClassField): CodeBlock {
        val fieldTypeName = TypeName.get(field.element.asType())
        val putStatement = when {
            isRawType(fieldTypeName) -> createPutRawStatementFromMap(field)
            isBundleable(field.element) -> createPutBundleStatementFromMap(field)
            isList(fieldTypeName) || isSet(fieldTypeName) -> createPutListStatementFromMap(field)
            isMap(fieldTypeName) -> {
                if (field.element.getAnnotation(CustomChecker::class.java) != null) {
                    createPutMapStatementFromMap(field)
                } else {
                    AnnotationProcessor.messenger?.printMessage(Diagnostic.Kind.ERROR, "Map data type must have a custom checker!", field.element)
                    throw Exception("Map data type must have a custom checker!")
                }
            }
            isSerializable(field.element) -> createPutSerializableStatementFromMap(field)
            isParcelable(field.element) -> createPutParcelableStatementFromMap(field)
            else -> throw java.lang.IllegalArgumentException("Unknown put statement")
        }

        val checkerStatement = CodeBlock.builder()
        if (field.element.getAnnotation(CustomChecker::class.java) != null) {
            createCustomCheckerCondition(field, checkerStatement, true)
            checkerStatement.add(putStatement)
            createCheckerSuccessBlock(field, checkerStatement)
            createCheckerFailedBlock(field, checkerStatement)
            checkerStatement.endControlFlow()
        } else {
            checkerStatement.add(putStatement)
        }

        return checkerStatement.build()
    }

    private fun createPutParcelableStatementFromMap(field: ModelClassField): CodeBlock {
        val statementBuilder = CodeBlock.builder()

        statementBuilder.beginControlFlow("if (data.containsKey(\$S)) ", field.key)

        statementBuilder.addStatement(
            "\$N.putParcelable(\$S, (\$T) data.get(\$S))",
            BUNDLE_NAME,
            field.key,
            ClassName.get("android.os", "Parcelable"),
            field.key
        )

        statementBuilder.endControlFlow()

        return statementBuilder.build()
    }

    private fun createPutSerializableStatementFromMap(field: ModelClassField): CodeBlock {
        val statementBuilder = CodeBlock.builder()

        statementBuilder.beginControlFlow("if (data.containsKey(\$S)) ", field.key)

        statementBuilder.addStatement(
            "\$N.putSerializable(\$S, (\$T) data.get(\$S))",
            BUNDLE_NAME,
            field.key,
            Serializable::class.java,
            field.key
        )

        statementBuilder.endControlFlow()

        return statementBuilder.build()
    }

    private fun createPutMapStatementFromMap(field: ModelClassField): CodeBlock {
        val statementBuilder = CodeBlock.builder()

        statementBuilder.beginControlFlow("if (data.containsKey(\$S))", field.element.simpleName)
        val keyType = (field.element.asType() as Type.ClassType).typeArguments[0]
        val keyTypeName = TypeName.get(keyType)
        val valueType = (field.element.asType() as Type.ClassType).typeArguments[1]
        val valueTypeName = TypeName.get(valueType)

        if (isBundleable(valueType.asElement())) {
            statementBuilder.addStatement(
                "\$T<\$T> \$L = \$L.values()",
                collectionClassName,
                valueType,
                "${field.element.simpleName}Values",
                getValueStatement(field)
            )

            statementBuilder
                .addStatement(
                    "\$T<\$T> ${field.element.simpleName}Iterator = \$L.iterator()",
                    iteratorClassName,
                    valueTypeName,
                    "${field.element.simpleName}Values"
                )
            statementBuilder.add(createPutBundlesStatement(field, valueTypeName))
        } else if (isParcelable(valueType.asElement())) {
            val parameterizedArrayListTypeName =
                ParameterizedTypeName.get(arrayListClassName, valueTypeName)
            val arrayListName = "${field.element.simpleName}ArrayList"
            statementBuilder.addStatement(
                "\$T \$N = new \$T(\$L.values())",
                parameterizedArrayListTypeName,
                arrayListName,
                parameterizedArrayListTypeName,
                getValueStatement(field)
            )
            statementBuilder.add(createPutParcelableArrayListStatement(field))
        } else if (isRawType(valueTypeName)) {
            val parameterizedMapEntryTypeName = ParameterizedTypeName.get(entryClassName, keyTypeName, valueTypeName)
            val parameterizedIteratorTypeName = ParameterizedTypeName.get(iteratorClassName, parameterizedMapEntryTypeName)

            statementBuilder
                    .addStatement("\$T \$N = (\$T) data.get(\$S)",
                            field.element.asType(),
                            field.element.simpleName,
                            field.element.asType(),
                            field.element.simpleName)
                    .addStatement(
                    "\$T \$N = \$N.entrySet().iterator()",
                    parameterizedIteratorTypeName,
                    "${field.element.simpleName}Entries",
                    field.element.simpleName)

            statementBuilder.beginControlFlow("while (\$N.hasNext())", "${field.element.simpleName}Entries")
                    .addStatement("\$T \$N = \$N.next()",
                            parameterizedMapEntryTypeName,
                            "entry",
                            "${field.element.simpleName}Entries")
                    .addStatement("\$N.put\$N(entry.getKey(), entry.getValue())",
                            BUNDLE_NAME,
                            RAW_BUNDLE_TYPE[valueType.toString()])

            val loggerAnnotation = clazz.element.getAnnotation(Logger::class.java)
            if (loggerAnnotation != null) {
                var loggerClass: TypeMirror? = null
                try {
                    loggerAnnotation.loggerClass
                } catch (e: MirroredTypeException) {
                    loggerClass = e.typeMirror
                }
                if (loggerClass == null) AnnotationProcessor.messenger?.printMessage(Diagnostic.Kind.ERROR, "Logger class not found", clazz.element)
                statementBuilder.addStatement("\$T.INSTANCE.log(\$T.\$L, \$S + entry.getKey())",
                        ClassName.get(loggerClass),
                        levelClassName,
                        Level.SUCCESS,
                        "${clazz.getClassName()}: ")
            } else {
                AnnotationProcessor.messenger?.printMessage(Diagnostic.Kind.ERROR, "Something went wrong when generating checker", clazz.element)
            }

            statementBuilder.endControlFlow()
        }

        statementBuilder.endControlFlow()

        return statementBuilder.build()
    }

    private fun createPutListStatementFromMap(field: ModelClassField): CodeBlock {
        val statementBuilder = CodeBlock.builder()

        statementBuilder.beginControlFlow("if (data.containsKey(\$S))", field.key)

        val parameterType = (field.element.asType() as Type.ClassType).typarams_field[0]
        val parameterTypeName = TypeName.get(parameterType)

        if (isBundleable(parameterType.asElement() as TypeElement)) {
            if (isSet(TypeName.get(field.element.asType()))) {
                statementBuilder.add(createIteratorStatementFromMap(field, setClassName))
            } else {
                statementBuilder.add(createIteratorStatementFromMap(field, listClassName))
            }
            statementBuilder.add(createPutBundlesStatementFromMap(field, parameterTypeName))
        } else if (isParcelable(parameterType.asElement() as TypeElement)) {
            val parameterizedArrayListTypeName =
                ParameterizedTypeName.get(arrayListClassName, parameterTypeName)
            val arrayListName = "${field.element.simpleName}ArrayList"

            statementBuilder.addStatement(
                "\$T \$N = new \$T((\$T) data.get(\$S))",
                parameterizedArrayListTypeName,
                arrayListName,
                parameterizedArrayListTypeName,
                collectionClassName,
                field.key
            )

            statementBuilder.add(createPutParcelableArrayListStatement(field))
        } else if (isRawType(parameterTypeName)) {
            statementBuilder.add(createPutRawsStatementFromMap(field, parameterTypeName))
        } else if (isSerializable(field.element)) {
            statementBuilder.add(createPutSerializableStatementFromMap(field))
        }

        statementBuilder.endControlFlow()

        return statementBuilder.build()
    }

    private fun createPutRawsStatementFromMap(
            field: ModelClassField,
            parameterTypeName: TypeName?
    ): CodeBlock {
        val statementBuilder = CodeBlock.builder()

        statementBuilder.addStatement(
                "\$T.INSTANCE.put\$L(\$S, (\$T) \$L, \$L, \$N)",
                bundlerUtilClassName,
            "${RAW_BUNDLE_TYPE[parameterTypeName.toString()]}List",
            field.key,
            ArrayList::class.java,
            "data.get(\"${field.key}\")",
            createDefaultValueCaster(field),
            BUNDLE_NAME
        )

        return statementBuilder.build()
    }

    private fun createPutBundlesStatementFromMap(
            field: ModelClassField,
            parameterTypeName: TypeName?
    ): CodeBlock {
        val statementBuilder = CodeBlock.builder()

        statementBuilder.addStatement(
            "\$T<\$T> ${field.element.simpleName}Bundles = new \$T()",
            arrayListClassName,
            bundleClassName,
            arrayListClassName
        )

        statementBuilder.beginControlFlow("while(${field.element.simpleName}Iterator.hasNext()) ")

        statementBuilder.addStatement(
            "\$T current = ${field.element.simpleName}Iterator.next()",
            ParameterGenerator.createParameterizedParameter(
                    "data",
                    ClassName.get(Map::class.java),
                    TypeName.get(String::class.java),
                    ClassName.get("java.lang", "Object")
            ).type
        )

        statementBuilder.add(createPutBundleStatement(field, parameterTypeName))

        statementBuilder.endControlFlow()

        statementBuilder.addStatement(
            "bundle.putParcelableArrayList(\$S, \$L)",
            field.key,
            "${field.element.simpleName}Bundles"
        )

        return statementBuilder.build()
    }

    private fun createIteratorStatementFromMap(
            field: ModelClassField,
            collectionClassName: ClassName
    ): CodeBlock {
        val statementBuilder = CodeBlock.builder()

        statementBuilder
            .addStatement(
                "\$T<\$T> ${field.element.simpleName}Iterator = ((\$T)\$L).iterator()",
                iteratorClassName,
                ParameterGenerator.createParameterizedParameter(
                        "data",
                        ClassName.get(Map::class.java),
                        TypeName.get(String::class.java),
                        ClassName.get("java.lang", "Object")
                ).type,
                collectionClassName,
                "data.get(\"${field.key}\")"
            )

        return statementBuilder.build()
    }

    private fun createPutBundleStatementFromMap(
            field: ModelClassField,
            parameterTypeName: TypeName? = null
    ): CodeBlock {
        val statementBuilder = CodeBlock.builder()

        statementBuilder.beginControlFlow("if (data.containsKey(\$S))", field.key)

        val fieldBundlerClass = getBundlerClassName(parameterTypeName, field)

        if (parameterTypeName == null) {
            statementBuilder.addStatement(
                "bundle.putBundle(\$S, \$T.getBundle((\$T) \$L))",
                field.key,
                fieldBundlerClass,
                ParameterGenerator.createParameterizedParameter(
                        "data",
                        ClassName.get(Map::class.java),
                        TypeName.get(String::class.java),
                        ClassName.get("java.lang", "Object")
                ).type,
                "data.get(\"${field.key}\")"
            )
        } else {
            statementBuilder.addStatement(
                "${field.element.simpleName}Bundles.add(\$T.getBundle(current))",
                fieldBundlerClass
            )
        }

        statementBuilder.endControlFlow()

        return statementBuilder.build()
    }

    private fun createPutRawStatementFromMap(field: ModelClassField): CodeBlock {
        val statementBuilder = CodeBlock.builder()

        statementBuilder.addStatement(
                "\$T.INSTANCE.put\$L(\$S, (\$L) \$L, \$L, \$N)",
                bundlerUtilClassName,
            RAW_BUNDLE_TYPE[field.element.asType().toString()],
            field.key,
            CAST[field.element.asType().toString()],
            "data.get(\"${field.key}\")",
            createDefaultValueCaster(field),
            BUNDLE_NAME
        )

        return statementBuilder.build()
    }

    // this function is used to generate a put expression for parcelable data type
    private fun createPutParcelableStatement(field: ModelClassField): CodeBlock {
        val statementBuilder = CodeBlock.builder()

        statementBuilder.addStatement(
            "\$N.put\$L(\$S, \$L)",
            BUNDLE_NAME,
            "Parcelable",
            field.key,
            getValueStatement(field)
        )

        return statementBuilder.build()
    }

    // this function is used to generate a field get expression (getXXX())
    private fun createGetterStatement(field: ModelClassField): String {
        return if (field.isPrivate) {
            field.getter.toString()
        } else {
            field.element.simpleName.toString()
        }
    }


    // this function is used to generate put expression for primitive + string data type
    private fun createPutRawStatement(field: ModelClassField): CodeBlock {
        val statementBuilder = CodeBlock.builder()

        statementBuilder.addStatement(
                "\$T.INSTANCE.put\$L(\$S, \$L, \$L, \$N)",
                bundlerUtilClassName,
            RAW_BUNDLE_TYPE[field.element.asType().toString()],
            field.key,
            getValueStatement(field),
            createDefaultValueCaster(field),
            BUNDLE_NAME
        )

        return statementBuilder.build()
    }

    private fun getType(field: ModelClassField) = getValueStatement(field)

    // Some data may have an owner so that we need to specify the owner in order to get the data
    // We first check whether the data is owned by an instance
    // If true (not empty) then we need to make a getter expression for that owner instance (owner.getXXX())
    // If false (empty) then we can access that data directly by using it's name
    protected fun getValueStatement(field: ModelClassField): String {
        val ownerName = getOwnerName(field)
        return if (ownerName.isEmpty()) {
            field.element.simpleName.toString()
        } else {
            "${ownerName}.${createGetterStatement(field)}"
        }
    }

    private fun getOwnerName(field: ModelClassField): String {
        return if (!isEventParam()) {
            getOwner(field).split(".").last().decapitalize()
        } else {
            ""
        }
    }

    private fun isEventParam() = clazz is AnnotatedEventClass

    // this function is used to generate a put expression for a map data type field
    private fun createPutMapStatement(field: ModelClassField): CodeBlock {
        val statementBuilder = CodeBlock.builder()
        val keyType = (field.element.asType() as Type.ClassType).typeArguments[0]
        val valueType = (field.element.asType() as Type.ClassType).typeArguments[1]
        val keyTypeName = TypeName.get(keyType)
        val valueTypeName = TypeName.get(valueType)

        if (isBundleable(valueType.asElement())) {
            statementBuilder.addStatement(
                "\$T<\$T> \$L = \$L.values()",
                collectionClassName,
                valueType,
                "${field.element.simpleName}Values",
                getValueStatement(field)
            )

            statementBuilder
                .addStatement(
                    "\$T<\$T> ${field.element.simpleName}Iterator = \$L.iterator()",
                    iteratorClassName,
                    valueTypeName,
                    "${field.element.simpleName}Values"
                )
            statementBuilder.add(createPutBundlesStatement(field, valueTypeName))
        } else if (isParcelable(valueType.asElement())) {
            val parameterizedArrayListTypeName =
                ParameterizedTypeName.get(arrayListClassName, valueTypeName)
            val arrayListName = "${field.element.simpleName}ArrayList"
            statementBuilder.addStatement(
                "\$T \$N = new \$T(\$L.values())",
                parameterizedArrayListTypeName,
                arrayListName,
                parameterizedArrayListTypeName,
                getValueStatement(field)
            )
            statementBuilder.add(createPutParcelableArrayListStatement(field))
        } else if (isRawType(valueTypeName)) {
            val parameterizedMapEntryTypeName = ParameterizedTypeName.get(entryClassName, keyTypeName, valueTypeName)
            val parameterizedIteratorTypeName = ParameterizedTypeName.get(iteratorClassName, parameterizedMapEntryTypeName)

            if (field.element.getAnnotation(DefinedInCollections::class.java) != null) {
                statementBuilder.addStatement(
                        "\$T \$N = \$N.entrySet().iterator()",
                        parameterizedIteratorTypeName,
                        "${field.element.simpleName}Entries",
                        field.element.simpleName
                )
            }

            statementBuilder.beginControlFlow("while (\$N.hasNext())", "${field.element.simpleName}Entries")
                    .addStatement("\$T \$N = \$N.next()",
                            parameterizedMapEntryTypeName,
                            "entry",
                            "${field.element.simpleName}Entries")
                    .addStatement("\$N.put\$N(entry.getKey(), entry.getValue())",
                            BUNDLE_NAME,
                            RAW_BUNDLE_TYPE[valueType.toString()])

            val loggerAnnotation = clazz.element.getAnnotation(Logger::class.java)
            if (loggerAnnotation != null) {
                var loggerClass: TypeMirror? = null
                try {
                    loggerAnnotation.loggerClass
                } catch (e: MirroredTypeException) {
                    loggerClass = e.typeMirror
                }
                if (loggerClass == null) AnnotationProcessor.messenger?.printMessage(Diagnostic.Kind.ERROR, "Logger class not found", clazz.element)
                statementBuilder.addStatement("\$T.INSTANCE.log(\$T.\$L, \$S + entry.getKey())",
                        ClassName.get(loggerClass),
                        levelClassName,
                        Level.SUCCESS,
                        "${clazz.getClassName()}: ")
            } else {
                AnnotationProcessor.messenger?.printMessage(Diagnostic.Kind.ERROR, "Something went wrong when generating checker", clazz.element)
            }

            statementBuilder.endControlFlow()
        }

        return addNullCheck(field, statementBuilder.build(), "ParcelableArrayList")
    }

    // this function is used to generate a put expression for data type field that implement list or set interface
    private fun createPutListStatement(field: ModelClassField): CodeBlock {
        val statementBuilder = CodeBlock.builder()
        val parameterType = (field.element.asType() as Type.ClassType).typarams_field[0]
        val parameterTypeName = TypeName.get(parameterType)

        if (isBundleable(parameterType.asElement() as TypeElement)) {
            statementBuilder.add(createIteratorStatement(field, parameterTypeName))
            statementBuilder.add(createPutBundlesStatement(field, parameterTypeName))
        } else if (isParcelable(parameterType.asElement() as TypeElement)) {
            val parameterizedArrayListTypeName =
                ParameterizedTypeName.get(arrayListClassName, parameterTypeName)
            val arrayListName = "${field.element.simpleName}ArrayList"

            statementBuilder.addStatement(
                "\$T \$N = new \$T(\$N)",
                parameterizedArrayListTypeName,
                arrayListName,
                parameterizedArrayListTypeName,
                getValueStatement(field)
            )

            statementBuilder.add(createPutParcelableArrayListStatement(field))
        } else if (isRawType(parameterTypeName)) {
            statementBuilder.add(createPutRawsStatement(field, parameterTypeName))
        } else if (isSerializable(field.element)) {
            statementBuilder.add(createPutSerializableStatement(field))
        }

        return statementBuilder.build()
    }

    private fun createPutRawsStatement(
            field: ModelClassField,
            parameterTypeName: TypeName
    ): CodeBlock {
        val statementBuilder = CodeBlock.builder()

        statementBuilder.addStatement(
                "\$T.INSTANCE.put\$L(\$S, \$L, \$L, \$N)",
                bundlerUtilClassName,
            "${RAW_BUNDLE_TYPE[parameterTypeName.toString()]}List",
            field.key,
            getValueStatement(field),
            createDefaultValueCaster(field),
            BUNDLE_NAME
        )

        return statementBuilder.build()
    }

    // this function is used to generate a put expression for an arraylist of parcelable
    private fun createPutParcelableArrayListStatement(
        field: ModelClassField
    ): CodeBlock {
        val statementBuilder = CodeBlock.builder()
        val arrayListName = "${field.element.simpleName}ArrayList"

        statementBuilder.addStatement(
            "\$N.putParcelableArrayList(\$S, \$N)",
            BUNDLE_NAME,
            field.key,
            arrayListName
        )

        return statementBuilder.build()
    }

    // this function is used to generate an expression to get the iterator of a collection data type
    // Ex: Iterator<Type> typeIterator = type.iterator();
    private fun createIteratorStatement(
            field: ModelClassField,
            parameterTypeName: TypeName
    ): CodeBlock {
        val statementBuilder = CodeBlock.builder()

        statementBuilder
            .addStatement(
                "\$T<\$T> ${field.element.simpleName}Iterator = \$L.iterator()",
                iteratorClassName,
                parameterTypeName,
                getValueStatement(field)
            )

        return statementBuilder.build()
    }

    // this function is used to generate a put expression for an array list of bundle
    private fun createPutBundlesStatement(
            field: ModelClassField,
            parameterTypeName: TypeName
    ): CodeBlock {
        val statementBuilder = CodeBlock.builder()

        statementBuilder.addStatement(
            "\$T<\$T> ${field.element.simpleName}Bundles = new \$T()",
            arrayListClassName,
            bundleClassName,
            arrayListClassName
        )

        statementBuilder.beginControlFlow("while(${field.element.simpleName}Iterator.hasNext()) ")

        statementBuilder.addStatement(
            "\$T current = ${field.element.simpleName}Iterator.next()",
            parameterTypeName
        )

        statementBuilder.add(createPutBundleStatement(field, parameterTypeName))

        statementBuilder.endControlFlow()

        statementBuilder.addStatement(
            "bundle.putParcelableArrayList(\$S, \$L)",
            field.key,
            "${field.element.simpleName}Bundles"
        )

        return statementBuilder.build()
    }

    // this function is used to generate a put exression for Bundle data type
    // or to generate an add expression
    // Ex: bundle.putBundle("key", Bundler.getBundle(data)) or someArrayListOfBundle.add(Bundler.getBundle(data))
    private fun createPutBundleStatement(
            field: ModelClassField,
            parameterTypeName: TypeName? = null
    ): CodeBlock {
        val statementBuilder = CodeBlock.builder()
        val fieldBundlerClass = getBundlerClassName(parameterTypeName, field)

        if (parameterTypeName == null) {
            statementBuilder.addStatement(
                "bundle.putBundle(\$S, \$T.getBundle(\$L))",
                field.key,
                fieldBundlerClass,
                getValueStatement(field)
            )
        } else {
            statementBuilder.addStatement(
                "${field.element.simpleName}Bundles.add(\$T.getBundle(current))",
                fieldBundlerClass
            )
        }

        return addNullCheck(field, statementBuilder.build(), "Bundle")
    }

    // this function is to get the generated bundler class name
    private fun getBundlerClassName(typeName: TypeName?, field: ModelClassField): ClassName {
        val fieldTypeFqName = typeName?.toString()?.split(".")
            ?: TypeName.get(field.element.asType()).toString().split(".")

        val fieldPackage = fieldTypeFqName.take(fieldTypeFqName.size - 1).joinToString(".")
        val fieldClassName = fieldTypeFqName.last()

        return ClassName.get(fieldPackage, "${fieldClassName}Bundler")
    }

    private fun addNullCheck(
            field: ModelClassField,
            codeBlock: CodeBlock,
            putType: String
    ): CodeBlock {
        val statementBuilder = CodeBlock.builder()

        if (field.isNullable) {
            statementBuilder
                .beginControlFlow(
                    "if(\$L == null)",
                    getValueStatement(field)
                )

            val defaultValueWithCast = createDefaultValueCaster(field)

            statementBuilder
                .addStatement(
                    "\$N.put\$L(\$S, \$L)",
                    BUNDLE_NAME,
                    putType,
                    field.element.simpleName,
                    defaultValueWithCast
                )
                .nextControlFlow("else")
        }

        statementBuilder.add(codeBlock)

        if (field.isNullable) {
            statementBuilder.endControlFlow()
        }

        return statementBuilder.build()
    }

    // this function is used to generate a cast expression for some data type that must be casted
    private fun createDefaultValueCaster(field: ModelClassField): String {
        return if (field.defaultValue is DefaultValueProviderFunction) {
            "${getOwnerName(field)}.${field.defaultValue.element.simpleName}()"
        } else if (isRawType(TypeName.get(field.element.asType()))) {
            val caster = when (field.element.asType().toString().split(".").last().toLowerCase()) {
                "byte" -> "(byte)"
                "short" -> "(short)"
                "int" -> "(int)"
                "long" -> "(long)"
                "float" -> "(float)"
                else -> ""
            }
            "$caster ${field.defaultValue}"
        } else if (field.defaultValue != null && field.defaultValue is Symbol.MethodSymbol) {
            if (isBundleable(field.element)) {
                val bundlerClassName = field.element.asType().toString().split(".").last().plus("Bundler")
                "$bundlerClassName.getBundle(new ${field.defaultValue})"
            } else {
                "new ${field.defaultValue}"
            }
        } else if (field.defaultValue is Constructor<*>) {
            "new ${field.defaultValue.toString().split(".").last()}"
        } else {
            val emptyConstructor =
                ElementFilter.constructorsIn((field.element.asType() as Type).asElement().enclosedElements)
                    .find { it.parameters.size == 0 }
            if (emptyConstructor == null) {
                AnnotationProcessor.messenger?.printMessage(Diagnostic.Kind.ERROR, "Must have an empty constructor!", (field.element.asType() as Type).asElement())
                ""
            } else {
                val bundlerClassName = field.element.asType().toString().split(".").last().plus("Bundler")
                "$bundlerClassName.getBundle(new $emptyConstructor)"
            }
        }
    }

    protected fun createCustomCheckerCondition(
            field: ModelClassField,
            checkerStatement: CodeBlock.Builder,
            isFromMap: Boolean
    ) {
        val checkerAnnotation = field.element.getAnnotation(CustomChecker::class.java)
        if (checkerAnnotation != null) {
            var checkerClass: TypeMirror? = null
            try {
                checkerAnnotation.checkerClass
            } catch (e: MirroredTypeException) {
                checkerClass = e.typeMirror
            }
            if (checkerClass == null) {
                AnnotationProcessor.messenger?.printMessage(Diagnostic.Kind.ERROR, "Checker class not found", clazz.element)
            }
            if (!isFromMap) {
                checkerStatement.beginControlFlow("if (${generateCheckerFormat(checkerAnnotation.functionName.size, objectTypeConditionCheckFormat)})", *getObjectTypeCheckerParams(ClassName.get(checkerClass), checkerAnnotation.functionName, field.element).toTypedArray())
            } else {
                checkerStatement.beginControlFlow("if (${generateCheckerFormat(checkerAnnotation.functionName.size, mapTypeConditionCheckFormat)})", *getMapTypeCheckerParams(field.key, ClassName.get(checkerClass), checkerAnnotation.functionName, field.element).toTypedArray())
            }
        } else {
            AnnotationProcessor.messenger?.printMessage(Diagnostic.Kind.ERROR, "Something went wrong when generating checker", clazz.element)
        }
    }

    private fun generateCheckerFormat(size: Int, format: String): String {
        val stringBuilder = StringBuilder()
        for (i in 0 until size) {
            if (stringBuilder.isNotBlank()) stringBuilder.append(" && ")
            stringBuilder.append(format)
        }
        return stringBuilder.toString()
    }

    private fun getObjectTypeCheckerParams(checkerClass: TypeName, functionNames: Array<out String>, element: VariableElement): List<Any> {
        val params = mutableListOf<Any>()
        functionNames.forEach {
            params.add(checkerClass)
            params.add(it)
            params.add(element)
        }

        return params
    }

    private fun getMapTypeCheckerParams(key: String, checkerClass: TypeName, functionNames: Array<out String>, element: VariableElement): List<Any> {
        val params = mutableListOf<Any>()
        functionNames.forEach {
            params.add(checkerClass)
            params.add(it)
            params.add(element)
            if (element.getAnnotation(DefinedInCollections::class.java) == null) {
                params.add("data.get(\"$key\")")
            } else {
                params.add("data.get(\"${element.simpleName}\")")
            }
        }
        return params
    }
}