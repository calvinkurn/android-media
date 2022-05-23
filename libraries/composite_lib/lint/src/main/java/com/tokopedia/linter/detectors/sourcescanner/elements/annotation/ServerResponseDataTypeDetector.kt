package com.tokopedia.linter.detectors.sourcescanner.elements.annotation

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Severity
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.PsiType
import com.intellij.psi.PsiVariable
import com.tokopedia.linter.detectors.sourcescanner.SourceCodeDetector
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UElement
import org.jetbrains.uast.ULiteralExpression
import org.jetbrains.uast.UNamedExpression


object ServerResponseDataTypeDetector {
    const val TYPE_OBJECT = "java.lang.Object"
    const val TYPE_STRING = "string"
    const val TYPE_INT = "int"
    const val TYPE_LONG = "long"
    const val TYPE_CHAR = "char"
    const val TYPE_FLOAT = "float"
    const val TYPE_DOUBLE = "double"
    const val TYPE_BOOLEAN = "boolean"
    const val TYPE_SHORT = "short"
    const val TYPE_BYTE = "byte"
    const val TYPE_NULL = "null"
    const val TYPE_INTEGER_WRAPPER = "java.lang.Integer"
    const val TYPE_BOOLEAN_WRAPPER = "java.lang.Boolean"
    const val TYPE_BYTE_WRAPPER = "java.lang.Byte"
    const val TYPE_SHORT_WRAPPER = "java.lang.Short"
    const val TYPE_LONG_WRAPPER = "java.lang.Long"
    const val TYPE_DOUBLE_WRAPPER = "java.lang.Double"
    const val TYPE_STRING_WRAPPER = "java.lang.String"
    const val TYPE_FLOAT_WRAPPER = "java.lang.Float"
    const val TYPE_CHARACTER_WRAPPER = "java.lang.Character"


    private val IdTypeStringMap = mapOf("^id_|_id\$|^id\$|Id\$|id\$|^[a-z]+ID|(?i)(?<= |^)ID(?= |\$)" to TYPE_STRING_WRAPPER)
    private val IdTypeLongMap = mapOf("^id_|_id\$|^id\$|Id\$|id\$|^[a-z]+ID|(?i)(?<= |^)ID(?= |\$)" to TYPE_LONG)
    private val IdTypeJavaLongMap = mapOf("^id_|_id\$|^id\$|Id\$|id\$|^[a-z]+ID|(?i)(?<= |^)ID(?= |\$)" to TYPE_LONG_WRAPPER)
    private val priceTypeDoubleMap = mapOf("^price|price\$" to TYPE_DOUBLE)
    private val priceTypeJavaDoubleMap = mapOf("^price|price\$" to TYPE_DOUBLE_WRAPPER)
    private val priceTypeStringMap = mapOf("^price|price\$" to TYPE_STRING_WRAPPER)


    const val DATA_TYPE_IMPORT_ID = "Invalid Data Type"
    const val BRIEF_DESCRIPTION = "This Data Type is not allowed for this object"
    const val EXPLAINATION = "Data Type for this field should be as per rules"


    val WRONG_DATA_TYPE: Issue = Issue.create(
            DATA_TYPE_IMPORT_ID,
            briefDescription = BRIEF_DESCRIPTION,
            explanation = EXPLAINATION,
            category = Category.CORRECTNESS,
            priority = 3,
            severity = Severity.ERROR,
            implementation = SourceCodeDetector.IMPLEMENTATION
    ).setAndroidSpecific(true)


    fun checkAndCreateError(context: JavaContext, annotation: UAnnotation) {

        val parent: UElement = annotation.uastParent ?: return
        val elements = (parent as PsiVariable)
        var attribute = getAttributeName(annotation)
        if(attribute != null) {
            val type = getRequiredTypes(attribute)
            if (type.isNotEmpty() and !isRequiredType(type, elements.type)) {
                var message = if (type.size > 1) "${type[0]} or ${type[1]}" else type[0]
                message = message.replace(TYPE_STRING_WRAPPER, TYPE_STRING)
                message = message.replace(TYPE_DOUBLE_WRAPPER, TYPE_DOUBLE)
                context.report(
                        WRONG_DATA_TYPE,
                        annotation,
                        context.getLocation(annotation),
                        "Please use data type as ${message} for variable $attribute"
                )
            }
        }
    }


    fun getRequiredTypes(attribute: String): List<String> {
        return (checkIsKeys(IdTypeLongMap, attribute).values.toList()
                + checkIsKeys(IdTypeStringMap, attribute).values.toList()
                + checkIsKeys(IdTypeJavaLongMap, attribute).values.toList()
                + checkIsKeys(priceTypeDoubleMap, attribute).values.toList()
                + checkIsKeys(priceTypeJavaDoubleMap, attribute).values.toList()
                + checkIsKeys(priceTypeStringMap, attribute).values.toList())
    }

    private fun isRequiredType(list: List<String>, type: PsiType): Boolean {
        if (list.isEmpty()) {
            return true
        }
        if (type is PsiPrimitiveType && isAllowedType(type, list)) {
            return true

        } else if (type is PsiClassType && isAllowedClassType(type, list)) {
            return true
        }
        return false;
    }

    var checkIsKeys =
            { map: Map<String, String>, attribute: String -> (map.filter { attribute.contains(it.key.toRegex()) }) }

    fun getAttributeName(annotation: UAnnotation): String? {
        val attributes: List<UNamedExpression> = annotation.attributeValues
        return if (attributes.size == 1) {
            val attribute = attributes[0]
            val value = attribute.expression
            if (value is ULiteralExpression) {
                val v = value.value
                return if (v is String) {
                    v;

                } else
                    null
            } else {
                null
            }
        } else {
            return null
        }
    }

    private fun isAllowedType(type: PsiType, allowedType: List<String>): Boolean {
        if (type !is PsiPrimitiveType) {
            return false
        }
        val resolvedClass = type.name
        return allowedType.contains(resolvedClass)
    }

    private fun isAllowedClassType(type: PsiType, allowedType: List<String>): Boolean {
        if (type !is PsiClassType) {
            return false
        }
        val resolvedClass = type.resolve()
        return allowedType.contains(resolvedClass?.qualifiedName)
    }

}