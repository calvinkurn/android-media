package com.tokopedia.analytic.processor

import com.squareup.javapoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.Type
import com.tokopedia.analytic.annotation.DefinedInCollections
import com.tokopedia.analytic.annotation.Key
import com.tokopedia.analytic.processor.utils.*
import com.tokopedia.annotation.defaultvalues.*
import org.jetbrains.annotations.NotNull
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement
import javax.lang.model.util.ElementFilter
import javax.tools.Diagnostic

class ModelClassField(
    val element: VariableElement,
    val key: String,
    val defaultValue: Any?,
    val isNullable: Boolean,
    val getter: ExecutableElement?,
    val isPrivate: Boolean
) {
    companion object {
        val keys = HashMap<String, HashMap<String, Pair<VariableElement, String>>>()

        val DEFAULT_VALUE = mapOf(
            "java.lang.String" to "\"\"",
            "kotlin.String" to "\"\"",
            "kotlin.Byte" to 0,
            "java.lang.Byte" to 0,
            "kotlin.Short" to 0,
            "java.lang.Short" to 0,
            "kotlin.Int" to 0,
            "java.lang.Integer" to 0,
            "kotlin.Long" to 0L,
            "java.lang.Long" to 0L,
            "kotlin.Double" to 0.0,
            "java.lang.Double" to 0.0,
            "kotlin.Float" to 0.0f,
            "java.lang.Float" to 0.0f,
            "kotlin.Boolean" to false,
            "java.lang.Boolean" to false,
            "kotlin.Char" to "\'\u0000\'",
            "java.lang.Character" to "\'\u0000\'",
            "kotlin.Array<kotlin.Byte>" to "new byte[0]",
            "kotlin.Array<kotlin.Short>" to "new short[0]",
            "kotlin.Array<kotlin.Int>" to "new int[0]",
            "kotlin.Array<kotlin.Double>" to "new double[0]",
            "kotlin.Array<kotlin.Float>" to "new float[0]",
            "kotlin.Array<kotlin.Long>" to "new long[0]",
            "kotlin.Array<kotlin.Char>" to "new char[0]",
            "kotlin.Array<kotlin.Boolean>" to "new boolean[0]",
            "kotlin.Array<kotlin.String>" to "new String[0]"
        )

        fun getClassFields(clazz: AnnotatedModelClass): Map<String, ModelClassField> {
            val fields = mutableMapOf<String, ModelClassField>()
            val elements = ElementFilter.fieldsIn(clazz.element.enclosedElements)

            val keySet = HashMap<String, Pair<VariableElement, String>>()

            elements.forEach { it ->
                if (it.kind == ElementKind.FIELD && (clazz.nameAsKey || isElementKeyDefined(it as VariableElement))) {
                    val defaultAnnotation = it.getAnnotation(Default::class.java)
                    val key = getKey(it, clazz.nameAsKey)
                    val defaultValue =
                        getDefaultValue(it as VariableElement, defaultAnnotation, clazz.defaultAll)
                            ?: getDefaultValueProvider(clazz, key) ?: getEmptyConstructor(it)

                    val isNullable = isElementNullable(it)

                    val getter = ElementFilter.methodsIn(clazz.element.enclosedElements)
                        .find { func -> func.simpleName.toString() == "get${it.simpleName.toString().capitalize()}" }

                    val isPrivate = it.modifiers.find { it == Modifier.PRIVATE }

                    if (getter == null && isPrivate != null) {
                        AnnotationProcessor.messenger?.printMessage(Diagnostic.Kind.ERROR, "Must have a getter or must not private!", it)
                    }

                    val fieldTypeName = TypeName.get(it.asType())

                    if (it.simpleName.toString() == "CREATOR") {
                        return@forEach
                    }

                    if (!isRawType(fieldTypeName)) {
                        val type: Type
                        it as Symbol
                        if (isList(fieldTypeName) || isSet(fieldTypeName)) {
                            type = (it.asType() as Type.ClassType).typarams_field[0]
                        } else if (isMap(fieldTypeName)) {
                            type = (it.asType() as Type.ClassType).typarams_field[1]
                        } else {

                            type = it.asType()
                        }

                        if (!isRawType(TypeName.get(type)) && !isBundleable(type.asElement()) && !isParcelable(
                                type.asElement()
                            ) && !isSerializable(it)
                        ) {
                            AnnotationProcessor.messenger?.printMessage(
                                Diagnostic.Kind.ERROR,
                                "Must be annotated with @BundleThis or parcelable or serializable!",
                                (it.asType() as Type).asElement())
                            return@forEach
                        }
                    }

                    if (isElementValid(
                            isNullable,
                            defaultAnnotation,
                            defaultValue,
                            it,
                            clazz.nameAsKey
                        )
                    ) {
                        key?.let { key ->
                            fields[key] = ModelClassField(
                                it,
                                key,
                                defaultValue,
                                isNullable,
                                getter,
                                isPrivate != null
                            )

                            val ownerFqName = (it as Symbol).owner.toString()
                            AnnotationProcessor.foundParams.add("${ownerFqName}.${(it as VariableElement).simpleName}")
                            keySet[key] = Pair<VariableElement, String>(it, it.asType().toString())
                        }
                    } else {
                        val name = it
                        throw Exception("Property ${name.simpleName} on class ${clazz.getClassName()} must have default value")
                    }
                } else if (it.kind == ElementKind.FIELD && it.getAnnotation(DefinedInCollections::class.java) != null) {
                    val getter = ElementFilter.methodsIn(clazz.element.enclosedElements)
                            .find { func -> func.simpleName.toString() == "get${it.simpleName.toString().capitalize()}" }
                    val isPrivate = it.modifiers.find { it == Modifier.PRIVATE }
                    fields["collection${it.simpleName}"] = ModelClassField(it, "", "", false, getter, isPrivate != null)
                }
            }

            keys.putIfAbsent((clazz.element as Symbol.ClassSymbol).fullname.toString(), keySet)

            return fields
        }

        private fun getEmptyConstructor(field: VariableElement): Any? {
            return if ((field.asType() as Type).isInterface) {
                if (field.asType().toString().contains("[java.util.List|java.util.Set]".toRegex())) {
                    ArrayList::class.java.constructors.find { it.parameters.isEmpty() }
                } else {
                    null
                }
            } else {
                ElementFilter.constructorsIn((field.asType() as Type).asElement().enclosedElements)
                    .find { it.parameters.isEmpty() }
            }
        }

        private fun getDefaultValueProvider(
                clazz: AnnotatedModelClass,
                key: String?
        ): DefaultValueProviderFunction? {
            return clazz.defaultValueProvider.find {
                it.forKey == key
            }
        }

        private fun getKey(it: VariableElement, nameAsKey: Boolean): String? {
            if (nameAsKey) {
                return it.simpleName.toString()
            } else {
                if (it.getAnnotation(Key::class.java) != null) {
                    return it.getAnnotation(Key::class.java).key
                } else {
                    return null
                }
            }
        }

        private fun isElementValid(
            nullable: Boolean,
            defaultAnnotation: Default?,
            defaultValue: Any?,
            it: VariableElement,
            nameAsKey: Boolean
        ) =
            (!nullable || (defaultAnnotation != null || defaultValue != null)) && !hasDuplicateDefaultAnnotation(
                it,
                defaultAnnotation
            ) && !hasDuplicateKey(it, nameAsKey)

        private fun hasDuplicateKey(it: VariableElement, nameAsKey: Boolean): Boolean {
            if (!nameAsKey) return false
            if (it.getAnnotation(Key::class.java) != null) {
                AnnotationProcessor.messenger?.printMessage(Diagnostic.Kind.ERROR, "Property ${it.simpleName} has duplicate key definition")
            }

            return false
        }

        private fun hasDuplicateDefaultAnnotation(
            it: VariableElement,
            defaultAnnotation: Default?
        ): Boolean {
            if (defaultAnnotation == null) return false
            if (it.getAnnotation(DefaultValueString::class.java) != null ||
                it.getAnnotation(DefaultValueLong::class.java) != null ||
                it.getAnnotation(DefaultValueChar::class.java) != null ||
                it.getAnnotation(DefaultValueDouble::class.java) != null ||
                it.getAnnotation(DefaultValueFloat::class.java) != null ||
                it.getAnnotation(DefaultValueInt::class.java) != null ||
                it.getAnnotation(DefaultValueShort::class.java) != null ||
                it.getAnnotation(DefaultValueBoolean::class.java) != null ||
                it.getAnnotation(DefaultValueByte::class.java) != null
            ) {
                throw Exception("Property ${it.simpleName} has two default value definition")
            }
            return false
        }

        private fun isElementNullable(it: VariableElement) =
            !(it.asType() as Type).isPrimitive && it.getAnnotation(NotNull::class.java) == null

        private fun getDefaultValue(
            it: VariableElement,
            defaultAnnotation: Default?,
            defaultAll: Boolean
        ) = when {
            it.getAnnotation(DefaultValueByte::class.java) != null -> it.getAnnotation(
                DefaultValueByte::class.java
            ).value
            it.getAnnotation(DefaultValueBoolean::class.java) != null -> it.getAnnotation(
                DefaultValueBoolean::class.java
            ).value
            it.getAnnotation(DefaultValueShort::class.java) != null -> "\"${it.getAnnotation(
                DefaultValueShort::class.java
            ).value}F\""
            it.getAnnotation(DefaultValueInt::class.java) != null -> it.getAnnotation(
                DefaultValueInt::class.java
            ).value
            it.getAnnotation(DefaultValueFloat::class.java) != null -> it.getAnnotation(
                DefaultValueDouble::class.java
            ).value
            it.getAnnotation(DefaultValueDouble::class.java) != null -> it.getAnnotation(
                DefaultValueDouble::class.java
            ).value
            it.getAnnotation(DefaultValueChar::class.java) != null -> "\'${it.getAnnotation(
                DefaultValueChar::class.java
            ).value}\'"
            it.getAnnotation(DefaultValueLong::class.java) != null -> it.getAnnotation(
                DefaultValueLong::class.java
            ).value
            it.getAnnotation(DefaultValueString::class.java) != null -> "\"${it.getAnnotation(
                DefaultValueString::class.java
            ).value}\""
            else -> getDefaultValueBasedOnType(it, defaultAnnotation, defaultAll)
        }

        private fun getDefaultValueBasedOnType(
            it: VariableElement,
            defaultAnnotation: Default?,
            defaultAll: Boolean
        ) =
            if (defaultAll) {
                if (defaultAnnotation != null && !defaultAnnotation.value) {
                    null
                } else {
                    if ((it.asType() is Type.ArrayType)) {
                        if (DEFAULT_VALUE.containsKey(it.asType().asTypeName().toString())) {
                            DEFAULT_VALUE.containsKey(it.asType().asTypeName().toString())
                        } else {
                            "new ${it.asType()}[]"
                        }
                    } else {
                        DEFAULT_VALUE[it.asType().asTypeName().toString()]
                    }
                }
            } else {
                if (defaultAnnotation != null && defaultAnnotation.value) {
                    if ((it.asType() is Type.ArrayType)) {
                        if (DEFAULT_VALUE.containsKey(it.asType().asTypeName().toString())) {
                            DEFAULT_VALUE.containsKey(it.asType().asTypeName().toString())
                        } else {
                            "new ${it.asType()}[]"
                        }
                    } else {
                        DEFAULT_VALUE[it.asType().asTypeName().toString()]
                    }
                } else {
                    null
                }
            }

        private fun isElementKeyDefined(it: VariableElement) =
            it.getAnnotation(Key::class.java) != null

    }

}