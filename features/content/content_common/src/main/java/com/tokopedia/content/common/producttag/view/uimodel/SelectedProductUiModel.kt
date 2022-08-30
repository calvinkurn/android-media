package com.tokopedia.content.common.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on August 30, 2022
 */
data class SelectedProductUiModel(
    val id: String,
    val name: String,
    val cover: String,
    val price: String,
    val priceDiscount: String,
    val isDiscount: Boolean,
    val priceOriginal: String,
    val discount: String,
) {
    companion object {
        fun createOnlyId(id: String): SelectedProductUiModel {
            return SelectedProductUiModel(
                id = id,
                name = "",
                cover = "",
                price = "",
                priceDiscount = "",
                isDiscount = false,
                priceOriginal = "",
                discount = "",
            )
        }
    }
}