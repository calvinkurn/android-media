package com.tokopedia.content.product.picker.ugc.view.uimodel

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
data class PagedDataUiModel<T: Any>(
    val dataList: List<T>,
    val hasNextPage: Boolean,
    val nextCursor: String,
)
