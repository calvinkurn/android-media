package com.tokopedia.chat_common.view.viewmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatRoomHeaderUiModel constructor(
        var name: String = "",
        var label: String = "",
        var senderId: String = "",
        var role: String = "",
        var mode: Int = 1,
        var keyword: String = "",
        var image: String = "",
        var lastTimeOnline: String = "",
        var isOnline: Boolean = false,
        var shopId: Long = 0,
        val isOfficial: Boolean = false,
        val isGold: Boolean = false,
        var shopType: Int = 0,
        val badge: String = ""
) : Parcelable {

    fun isOfficialAccountTokopedia(): Boolean = label == Companion.TAG_OFFICIAL

    object Companion {
        const val MODE_DEFAULT_GET_CHAT: Int = 1
        const val ROLE_USER: String = "user"
        const val ROLE_SHOP: String = "shop"
        const val ROLE_OFFICIAL: String = "administrator"
        const val TAG_OFFICIAL: String = "Official"
        const val SHOP_TYPE_REGULAR = "reguler"
        const val SHOP_TYPE_GOLD_MERCHANT = "gold_merchant"
        const val SHOP_TYPE_OFFICIAL_STORE = "official_Store"
        const val SHOP_TYPE_TOKONOW = "tokonow"
    }


}
