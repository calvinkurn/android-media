package com.tokopedia.play.view.type

/**
 * @author by astidhiyaa on 25/10/22
 */

/*** [Button Type]
 * ATC: add to cart button
 * GCR: global cart redirect (beli) button
 * OCC: one click checkout button (beli langsung) button
 */
data class ProductButtonUiModel(
    val text: String,
    val color: ProductButtonColor,
    val type: ProductButtonType,
) {
    companion object {
        val Default: ProductButtonUiModel
            get() = ProductButtonUiModel(
                text = "Beli",
                color = ProductButtonColor.PRIMARY_BUTTON,
                type = ProductButtonType.GCR,
            )
    }
}

fun ProductButtonUiModel?.orDefault() = this ?: ProductButtonUiModel.Default

enum class ProductButtonColor(val value: String) {
    PRIMARY_BUTTON("primary"),
    SECONDARY_BUTTON ("secondary"),
    SECONDARY_GRAY_BUTTON ("secondary_gray"),
    DISABLED_BUTTON("disabled"),
    UNKNOWN("");

    companion object {
        private val values = values()
        fun getByValue(value: String): ProductButtonColor {
            values.forEach {
                if (it.value.equals(value, true)) return it
            }
            return UNKNOWN
        }
    }
}

enum class ProductButtonType(val value: String) {
    ATC("atc"),
    GCR ("gcr"),
    OCC ("occ"),

    UNKNOWN("");

    companion object {
        private val values = values()
        fun getByValue(value: String): ProductButtonType {
            values.forEach {
                if (it.value.equals(value, true)) return it
            }
            return UNKNOWN
        }
    }
}
