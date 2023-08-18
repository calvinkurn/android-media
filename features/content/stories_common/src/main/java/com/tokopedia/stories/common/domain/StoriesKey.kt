package com.tokopedia.stories.common.domain

/**
 * Created by kenny.hadisaputra on 08/08/23
 */
enum class StoriesKey(val key: String, internal val sourceName: String) {
    ShopPage("shop_page", "shop-page"),
    ProductDetail("product_detail", "pdp-page"),
    TopChatList("top_chat_list", "topchat-page"),
    TopChatRoom("top_chat_room", "topchat-page")
}
