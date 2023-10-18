package com.tokopedia.product.addedit.common.util

import com.tokopedia.abstraction.common.utils.view.MethodChecker

object StringValidationUtil {
    private const val HTML_SPACE_ENTITY = "&nbsp;"

    fun isAllowedString(string: String): Boolean {
        return string.any { it.isLetter() } &&
                string.all { it.isLetterOrDigit() || it.isAllowedSymbol()}
    }

    fun Char.isAllowedSymbol(): Boolean {
        return "!/-%_*|#$&@() ".contains(this)
    }

    fun String.filterDigit() = filter { it.isDigit() }

    fun String.fromHtmlWithSpaceAndLinebreak(): String {
        return MethodChecker.fromHtmlPreserveLineBreak(replace(" ", HTML_SPACE_ENTITY)).toString()
    }

}
