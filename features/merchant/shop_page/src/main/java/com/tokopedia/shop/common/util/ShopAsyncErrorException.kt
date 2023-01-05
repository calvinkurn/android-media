package com.tokopedia.shop.common.util

data class ShopAsyncErrorException(
    val asyncQueryType: AsyncQueryType,
    val throwable: Throwable
) : Throwable(throwable) {
    enum class AsyncQueryType {
        SHOP_PAGE_P1,
        SHOP_HEADER_WIDGET,
        SHOP_INITIAL_PRODUCT_LIST,
        SHOP_PAGE_GET_LAYOUT_V2
    }
}
