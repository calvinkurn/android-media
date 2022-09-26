package com.tokopedia.content.common.producttag.view.uimodel.config

/**
 * Created By : Jonathan Darwin on August 23, 2022
 */
data class ContentProductTagConfig(
    val isMultipleSelectionProduct: Boolean,
    val isFullPageAutocomplete: Boolean,
    val maxSelectedProduct: Int,
    val backButton: BackButton,
    val isShowActionBarDivider: Boolean,
    val appLinkAfterAutocomplete: String,
) {
    enum class BackButton(val value: Int) {
        Back(1),
        Close(2);

        companion object {
            fun mapFromValue(v: Int): BackButton {
                return values().firstOrNull { it.value == v } ?: Back
            }
        }
    }
}