package com.tokopedia.stories.widget.domain

/**
 * Created by kenny.hadisaputra on 08/08/23
 */
enum class StoriesEntryPoint(
    val key: String,
    internal val sourceName: String,
    internal val trackingEventCategory: String
) {
    ShopPage("shop_page", "shop-page", "shop page - buyer"),
    ProductDetail("product_detail", "pdp-page", "product detail page"),
    TopChatList("top_chat_list", "topchat-page", "inbox-chat"),
    TopChatRoom("top_chat_room", "topchat-page", "message room")
}
