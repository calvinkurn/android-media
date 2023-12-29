package com.tokopedia.carouselproductcard.paging

import com.tokopedia.productcard.ProductCardModel
import kotlin.math.ceil
import kotlin.math.max

data class CarouselPagingGroupProductModel(
    val group: CarouselPagingGroupModel,
    val productItemList: List<ProductCardModel> = listOf(),
) {

    fun getPageCount(itemPerPage: Int): Int =
        max(ceil(productItemList.size / itemPerPage.toDouble()).toInt(), MIN_PAGE_COUNT)

    companion object {
        private const val MIN_PAGE_COUNT = 1
    }
}
