package com.tokopedia.content.product.picker.seller.model

/**
 * Created By : Jonathan Darwin on October 04, 2022
 */
sealed interface PagingType {

    data class Page(val page: Int) : PagingType

    data class Cursor(val cursor: String) : PagingType

    object Unknown : PagingType
}
