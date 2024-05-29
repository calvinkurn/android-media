package com.tokopedia.topchat.chatroom.domain

enum class TopChatRoomMessageTypeEnum(val typeString: String) {
    NORMAL("normal"),
    PROMO("promo"),
    PROMO_V2("promo_v2"),
    FLASH_SALE("flash_sale"),
    FLASH_SALE_V2("flash_sale_v2"),
    NEW_PRODUCT("produk_baru"),
    NEW_PRODUCT_V2("produk_baru_v2"),
    SPECIAL_RELEASE("rilisan_spesial"),
    PRODUCT_BUNDLING("product_bundling"),
    CART_ABANDONMENT("cart_abandonment"),
    REVIEW_REMINDER("review_reminder");

    companion object {
        fun fromValue(value: String): TopChatRoomMessageTypeEnum {
            return values().find { it.typeString == value } ?: NORMAL // default
        }
    }
}
