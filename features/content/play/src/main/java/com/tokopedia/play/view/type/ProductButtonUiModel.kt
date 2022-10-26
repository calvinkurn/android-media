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

/**
 *
 * PRIMARY: colored background, white text and border
 * PRIMARY_DISABLED: disabled primary button
 * SECONDARY: white background, colored text and border
 * SECONDARY_DISABLED: disabled secondary button
 */
enum class ProductButtonColor(val value: String) {
    PRIMARY_BUTTON("PRIMARY"),
    PRIMARY_DISABLED_BUTTON("PRIMARY_DISABLED"),
    SECONDARY_BUTTON ("SECONDARY"),
    SECONDARY_DISABLED_BUTTON ("SECONDARY_DISABLED"),
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
