package com.tokopedia.stories.widget.domain

/**
 * Created by kenny.hadisaputra on 08/08/23
 */
enum class StoriesEntryPoint(
    internal val key: String,
    internal val sourceName: String,
    internal val trackerName: String,
) {
    ShopPage("shop_page", "shop-page", "shop"),
    ShopPageReimagined("shop_page_reimagined", "shop-page", "shop"),
    ProductDetail("product_detail", "pdp-page", "pdp"),
    TopChatList("top_chat_list", "topchat-page", "chatlist"),
    TopChatRoom("top_chat_room", "topchat-page", "chatroom")
}
