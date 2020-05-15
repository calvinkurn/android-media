package com.tokopedia.analytic.processor

import com.squareup.javapoet.*
import javax.annotation.processing.FilerException
import javax.lang.model.element.Modifier

class PutHelperGenerator {
    companion object {
        val bundleUtilClassName = ClassName.get("com.analytic.util", "BundlerUtil")
    }
    val classBuilder = TypeSpec.classBuilder("BundlerUtil").addModifiers(Modifier.PUBLIC)

    fun generate() {
        classBuilder.addMethod(generatePutByte())
        classBuilder.addMethod(generatePutShort())
        classBuilder.addMethod(generatePutInt())
        classBuilder.addMethod(generatePutDouble())
        classBuilder.addMethod(generatePutFloat())
        classBuilder.addMethod(generatePutLong())
        classBuilder.addMethod(generatePutChar())
        classBuilder.addMethod(generatePutBoolean())
        classBuilder.addMethod(generatePutString())
        classBuilder.addMethod(generatePutByteList())
        classBuilder.addMethod(generatePutShortList())
        classBuilder.addMethod(generatePutIntList())
        classBuilder.addMethod(generatePutDoubleList())
        classBuilder.addMethod(generatePutFloatList())
        classBuilder.addMethod(generatePutLongList())
        classBuilder.addMethod(generatePutCharList())
        classBuilder.addMethod(generatePutBooleanList())
        classBuilder.addMethod(generateStringList())
        writeToFile()
    }

    private fun generatePutByteList(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putByteList")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameterizedParameter("value", ClassGenerator.listClassName, TypeName.BYTE.box()))
            .addParameter(ParameterGenerator.createParameterizedParameter("defaultValue", ClassGenerator.listClassName, TypeName.BYTE.box()))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .beginControlFlow("if (value == null) ")
            .addStatement("\$T arr = defaultValue.stream().mapToInt(i -> i).toArray()", ArrayTypeName.of(TypeName.INT))
            .addStatement("bundle.putIntArray(key, arr)")
            .nextControlFlow("else ")
            .addStatement("\$T arr = value.stream().mapToInt(i -> i).toArray()", ArrayTypeName.of(TypeName.INT))
            .addStatement("bundle.putIntArray(key, arr)")
            .endControlFlow()

        return methodBuilder.build()
    }

    private fun generatePutShortList(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putShortList")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameterizedParameter("value", ClassGenerator.listClassName, TypeName.SHORT.box()))
            .addParameter(ParameterGenerator.createParameterizedParameter("defaultValue", ClassGenerator.listClassName, TypeName.SHORT.box()))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .beginControlFlow("if (value == null) ")
            .addStatement("\$T arr = defaultValue.stream().mapToInt(i -> i).toArray()", ArrayTypeName.of(TypeName.INT))
            .addStatement("bundle.putIntArray(key, arr)")
            .nextControlFlow("else ")
            .addStatement("\$T arr = value.stream().mapToInt(i -> i).toArray()", ArrayTypeName.of(TypeName.INT))
            .addStatement("bundle.putIntArray(key, arr)")
            .endControlFlow()

        return methodBuilder.build()
    }

    private fun generatePutIntList(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putIntList")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameterizedParameter("value", ClassGenerator.listClassName, ClassName.get(Integer::class.java)))
            .addParameter(ParameterGenerator.createParameterizedParameter("defaultValue", ClassGenerator.listClassName, ClassName.get(Integer::class.java)))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .addStatement("\$T arrayList", ArrayList::class.java)
            .beginControlFlow("if (value == null) ")
            .addStatement("arrayList = new \$T(defaultValue)", ArrayList::class.java)
            .nextControlFlow("else ")
            .addStatement("arrayList = new \$T(defaultValue)", ArrayList::class.java)
            .endControlFlow()
            .addStatement("bundle.putIntegerArrayList(key, arrayList)")

        return methodBuilder.build()
    }

    private fun generatePutDoubleList(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putDoubleList")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameterizedParameter("value", ClassGenerator.listClassName, TypeName.DOUBLE.box()))
            .addParameter(ParameterGenerator.createParameterizedParameter("defaultValue", ClassGenerator.listClassName, TypeName.DOUBLE.box()))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .beginControlFlow("if (value == null) ")
            .addStatement("\$T arr = defaultValue.stream().mapToDouble(i -> i).toArray()", ArrayTypeName.of(TypeName.DOUBLE))
            .addStatement("bundle.putDoubleArray(key, arr)")
            .nextControlFlow("else ")
            .addStatement("\$T arr = value.stream().mapToDouble(i -> i).toArray()", ArrayTypeName.of(TypeName.DOUBLE))
            .addStatement("bundle.putDoubleArray(key, arr)")
            .endControlFlow()

        return methodBuilder.build()
    }

    private fun generatePutFloatList(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putFloatList")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameterizedParameter("value", ClassGenerator.listClassName, TypeName.FLOAT.box()))
            .addParameter(ParameterGenerator.createParameterizedParameter("defaultValue", ClassGenerator.listClassName, TypeName.FLOAT.box()))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .beginControlFlow("if (value == null) ")
            .addStatement("\$T arr = defaultValue.stream().mapToDouble(i -> i).toArray()", ArrayTypeName.of(TypeName.DOUBLE))
            .addStatement("bundle.putDoubleArray(key, arr)")
            .nextControlFlow("else ")
            .addStatement("\$T arr = value.stream().mapToDouble(i -> i).toArray()", ArrayTypeName.of(TypeName.DOUBLE))
            .addStatement("bundle.putDoubleArray(key, arr)")
            .endControlFlow()

        return methodBuilder.build()
    }

    private fun generatePutLongList(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putLongList")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameterizedParameter("value", ClassGenerator.listClassName, TypeName.LONG.box()))
            .addParameter(ParameterGenerator.createParameterizedParameter("defaultValue", ClassGenerator.listClassName, TypeName.LONG.box()))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .beginControlFlow("if (value == null) ")
            .addStatement("\$T arr = defaultValue.stream().mapToLong(i -> i).toArray()", ArrayTypeName.of(TypeName.LONG))
            .addStatement("bundle.putLongArray(key, arr)")
            .nextControlFlow("else ")
            .addStatement("\$T arr = value.stream().mapToLong(i -> i).toArray()", ArrayTypeName.of(TypeName.LONG))
            .addStatement("bundle.putLongArray(key, arr)")
            .endControlFlow()

        return methodBuilder.build()
    }

    private fun generatePutCharList(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putCharList")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameterizedParameter("value", ClassGenerator.listClassName, TypeName.CHAR.box()))
            .addParameter(ParameterGenerator.createParameterizedParameter("defaultValue", ClassGenerator.listClassName, TypeName.CHAR.box()))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .addStatement("char[] arr")
            .beginControlFlow("if (value == null) ")
            .addStatement("arr = new char[defaultValue.size()]")
            .beginControlFlow("for (int i = 0; i < defaultValue.size(); i++) ")
            .addStatement("arr[i] = defaultValue.get(i)")
            .endControlFlow()
            .nextControlFlow("else ")
            .addStatement("arr = new char[value.size()]")
            .beginControlFlow("for (int i = 0; i < value.size(); i++) ")
            .addStatement("arr[i] = value.get(i)")
            .endControlFlow()
            .endControlFlow()
            .addStatement("bundle.putCharArray(key, arr)")

        return methodBuilder.build()
    }

    private fun generatePutBooleanList(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putBooleanList")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameterizedParameter("value", ClassGenerator.listClassName, TypeName.BOOLEAN.box()))
            .addParameter(ParameterGenerator.createParameterizedParameter("defaultValue", ClassGenerator.listClassName, TypeName.BOOLEAN.box()))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .addStatement("boolean[] arr")
            .beginControlFlow("if (value == null) ")
            .addStatement("arr = new boolean[defaultValue.size()]")
            .beginControlFlow("for (int i = 0; i < defaultValue.size(); i++) ")
            .addStatement("arr[i] = defaultValue.get(i)")
            .endControlFlow()
            .nextControlFlow("else ")
            .addStatement("arr = new boolean[value.size()]")
            .beginControlFlow("for (int i = 0; i < value.size(); i++) ")
            .addStatement("arr[i] = value.get(i)")
            .endControlFlow()
            .endControlFlow()
            .addStatement("bundle.putBooleanArray(key, arr)")

        return methodBuilder.build()
    }

    private fun generateStringList(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putStringList")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameterizedParameter("value", ClassGenerator.listClassName, ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameterizedParameter("defaultValue", ClassGenerator.listClassName, ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .addStatement(
                "\$T<\$T> \$N = new \$T<\$T>()",
                ArrayList::class.java,
                String::class.java,
                "arrayList",
                ArrayList::class.java,
                String::class.java
            )
            .beginControlFlow("if (value == null) ")
            .addStatement("arrayList.addAll(defaultValue)")
            .nextControlFlow("else ")
            .addStatement("arrayList.addAll(value)")
            .endControlFlow()
            .addStatement("bundle.putStringArrayList(key, arrayList)")

        return methodBuilder.build()
    }

    private fun generatePutByte(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putByte")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameter("value", TypeName.BYTE.box()))
            .addParameter(ParameterGenerator.createParameter("defaultValue", TypeName.BYTE.box()))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .beginControlFlow("if (value == null) ")
            .addStatement("bundle.putByte(key, defaultValue)")
            .nextControlFlow("else ")
            .addStatement("bundle.putByte(key, value)")
            .endControlFlow()

        return methodBuilder.build()
    }

    private fun generatePutShort(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putShort")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameter("value", TypeName.SHORT.box()))
            .addParameter(ParameterGenerator.createParameter("defaultValue", TypeName.SHORT.box()))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .beginControlFlow("if (value == null) ")
            .addStatement("bundle.putShort(key, defaultValue)")
            .nextControlFlow("else ")
            .addStatement("bundle.putShort(key, value)")
            .endControlFlow()

        return methodBuilder.build()
    }

    private fun generatePutInt(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putInt")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameter("value", TypeName.INT.box()))
            .addParameter(ParameterGenerator.createParameter("defaultValue", TypeName.INT.box()))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .beginControlFlow("if (value == null) ")
            .addStatement("bundle.putInt(key, defaultValue)")
            .nextControlFlow("else ")
            .addStatement("bundle.putInt(key, value)")
            .endControlFlow()

        return methodBuilder.build()
    }

    private fun generatePutDouble(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putDouble")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameter("value", TypeName.DOUBLE.box()))
            .addParameter(ParameterGenerator.createParameter("defaultValue", TypeName.DOUBLE.box()))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .beginControlFlow("if (value == null) ")
            .addStatement("bundle.putDouble(key, defaultValue)")
            .nextControlFlow("else ")
            .addStatement("bundle.putDouble(key, value)")
            .endControlFlow()

        return methodBuilder.build()
    }

    private fun generatePutFloat(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putFloat")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameter("value", TypeName.FLOAT.box()))
            .addParameter(ParameterGenerator.createParameter("defaultValue", TypeName.FLOAT.box()))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .beginControlFlow("if (value == null) ")
            .addStatement("bundle.putFloat(key, defaultValue)")
            .nextControlFlow("else ")
            .addStatement("bundle.putFloat(key, value)")
            .endControlFlow()

        return methodBuilder.build()
    }

    private fun generatePutLong(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putLong")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameter("value", TypeName.LONG.box()))
            .addParameter(ParameterGenerator.createParameter("defaultValue", TypeName.LONG.box()))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .beginControlFlow("if (value == null) ")
            .addStatement("bundle.putLong(key, defaultValue)")
            .nextControlFlow("else ")
            .addStatement("bundle.putLong(key, value)")
            .endControlFlow()

        return methodBuilder.build()
    }

    private fun generatePutChar(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putChar")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameter("value", ClassName.get(Character::class.java)))
            .addParameter(ParameterGenerator.createParameter("defaultValue", ClassName.get(Character::class.java)))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .beginControlFlow("if (value == null) ")
            .addStatement("bundle.putChar(key, defaultValue)")
            .nextControlFlow("else ")
            .addStatement("bundle.putChar(key, value)")
            .endControlFlow()

        return methodBuilder.build()
    }

    private fun generatePutBoolean(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putBoolean")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameter("value", TypeName.BOOLEAN.box()))
            .addParameter(ParameterGenerator.createParameter("defaultValue", TypeName.BOOLEAN.box()))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .beginControlFlow("if (value == null) ")
            .addStatement("bundle.putBoolean(key, defaultValue)")
            .nextControlFlow("else ")
            .addStatement("bundle.putBoolean(key, value)")
            .endControlFlow()

        return methodBuilder.build()
    }

    private fun generatePutString(): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("putString")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        methodBuilder
            .addParameter(ParameterGenerator.createParameter("key", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameter("value", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameter("defaultValue", ClassName.get(String::class.java)))
            .addParameter(ParameterGenerator.createParameter("bundle", ClassName.get("android.os", "Bundle")))

        methodBuilder
            .beginControlFlow("if (value == null) ")
            .addStatement("bundle.putString(key, defaultValue)")
            .nextControlFlow("else ")
            .addStatement("bundle.putString(key, value)")
            .endControlFlow()

        return methodBuilder.build()
    }

    private fun writeToFile() {
        val file = JavaFile
            .builder("com.analytic.util", classBuilder.build())
            .indent("    ")
            .build()

        try {
            AnnotationProcessor.filer?.run {
                file.writeTo(this)
            }
        } catch (ignored: FilerException) {}
    }
}