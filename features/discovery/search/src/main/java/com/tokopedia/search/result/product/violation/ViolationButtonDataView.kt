package com.tokopedia.search.result.product.violation

import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.unifycomponents.UnifyButton

typealias ViolationButtonTypeVariant = Pair<Int, Int>

data class ViolationButtonDataView(
    val ctaUrl: String = "",
    val text: String = "",
    val type: Int = BUTTON_TYPE_DEFAULT_VALUE,
    val variant: Int = BUTTON_VARIANT_DEFAULT_VALUE
) {
    companion object {
        const val BUTTON_TYPE_DEFAULT_VALUE = UnifyButton.Type.MAIN
        const val BUTTON_VARIANT_DEFAULT_VALUE = UnifyButton.Variant.GHOST

        private const val BUTTON_TYPE_GHOST_MAIN = "ghostMain"
        private const val BUTTON_TYPE_FILLED_MAIN = "filledMain"

        private fun convertTypeVariantStringToViolationButtonTypeVariant(
            buttonTypeVariant: String
        ): ViolationButtonTypeVariant {
            return when (buttonTypeVariant) {
                BUTTON_TYPE_FILLED_MAIN -> ViolationButtonTypeVariant(
                    UnifyButton.Type.MAIN,
                    UnifyButton.Variant.FILLED
                )
                else -> ViolationButtonTypeVariant(
                    BUTTON_TYPE_DEFAULT_VALUE,
                    BUTTON_VARIANT_DEFAULT_VALUE
                )
            }
        }

        fun create(
            violation: SearchProductModel.Violation
        ): ViolationButtonDataView {
            val (buttonType, buttonVariant) = convertTypeVariantStringToViolationButtonTypeVariant(violation.buttonType)
            return ViolationButtonDataView(violation.ctaUrl, violation.buttonText, buttonType, buttonVariant)
        }
    }

    val isVisible: Boolean by lazy(LazyThreadSafetyMode.NONE) {
        text.isNotEmpty() && ctaUrl.isNotEmpty()
    }
}