package com.tokopedia.product.addedit.common.util

object StringValidationUtil {

    fun isAllowedString(string: String): Boolean {
        return string.any { it.isLetter() } &&
                string.all { it.isLetterOrDigit() || it.isAllowedSymbol()}
    }

    fun Char.isAllowedSymbol(): Boolean {
        return "!/-%_*|#$&@() ".contains(this)
    }
}