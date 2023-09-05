package com.tokopedia.stories.widget.domain

/**
 * Created by kenny.hadisaputra on 08/08/23
 */
enum class StoriesEntryPoint(val key: String, internal val sourceName: String) {
    ShopPage("shop_page", "shop-page"),
    ProductDetail("product_detail", "pdp-page"),
    TopChatList("top_chat_list", "topchat-page"),
    TopChatRoom("top_chat_room", "topchat-page")
}
sealed class StoriesEntrySource {
    abstract val id: String
    abstract val key: String
    abstract val sourceName: String

    data class ShopPage(val shopId: String) : StoriesEntrySource() {
        override val id: String
            get() = shopId

        override val sourceName: String
            get() = "shop-page"

        override val key: String
            get() = "shop_page"
    }

    data class ProductDetail(val productId: String) : StoriesEntrySource() {
        override val id: String
            get() = productId

        override val sourceName: String
            get() = "pdp-page"

        override val key: String
            get() = "product_detail"
    }
    data class TopChatList(val shopId: String) : StoriesEntrySource() {
        override val id: String
            get() = shopId

        override val sourceName: String
            get() = "topchat-page"

        override val key: String
            get() = "top_chat_list"
    }

    data class TopChatRoom(val shopId: String) : StoriesEntrySource() {
        override val id: String
            get() = shopId

        override val sourceName: String
            get() = "topchat-page"

        override val key: String
            get() = "top_chat_room"
    }
}
