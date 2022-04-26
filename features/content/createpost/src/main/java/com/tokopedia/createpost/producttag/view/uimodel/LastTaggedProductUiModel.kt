package com.tokopedia.createpost.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
data class LastTaggedProductUiModel(
    val products: List<Product>,
    val nextCursor: String,
)