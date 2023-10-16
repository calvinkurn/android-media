package com.tokopedia.content.product.picker.seller.model.paged

/**
 * Created by kenny.hadisaputra on 07/02/22
 */
data class PagedDataUiModel<T: Any>(
    val dataList: List<T>,
    val hasNextPage: Boolean,
    val cursor: String = "",
)
