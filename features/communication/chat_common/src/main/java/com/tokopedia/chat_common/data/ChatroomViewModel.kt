package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderUiModel
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderUiModel.Companion.SHOP_TYPE_GOLD_MERCHANT
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderUiModel.Companion.SHOP_TYPE_OFFICIAL_STORE
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderUiModel.Companion.SHOP_TYPE_REGULAR
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderUiModel.Companion.SHOP_TYPE_TOKONOW
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author by nisie on 14/12/18.
 */
class ChatroomViewModel constructor(
    val listChat: ArrayList<Visitable<*>> = ArrayList(),
    val headerModel: ChatRoomHeaderUiModel = ChatRoomHeaderUiModel(),
    val canLoadMore: Boolean = false,
    val replyable: Boolean = false,
    var blockedStatus: BlockedStatus = BlockedStatus(),
    val latestHeaderDate: String = "",
    val replyIDs: String = ""
) {

    val shopName: String get() {
        return headerModel.name
    }
    val shopType: String get() {
        var shopType = ""
        when (headerModel.shopType) {
            1 -> shopType = SHOP_TYPE_REGULAR
            2 -> shopType = SHOP_TYPE_GOLD_MERCHANT
            3 -> shopType = SHOP_TYPE_OFFICIAL_STORE
            4 -> shopType = SHOP_TYPE_TOKONOW
        }
        return shopType
    }
    val badgeUrl get() = headerModel.badge

    val role get() = headerModel.role.toLowerCase(Locale.getDefault())

    fun isSeller(): Boolean {
        return role == ChatRoomHeaderUiModel.Companion.ROLE_USER
    }

    fun isChattingWithSeller(): Boolean {
        return role.contains(ChatRoomHeaderUiModel.Companion.ROLE_SHOP)
    }

    fun hasBadge(): Boolean {
        return (headerModel.isGold || headerModel.isOfficial) && headerModel.badge.isNotEmpty()
    }

    fun getHeaderName(): String {
        return headerModel.name
    }

    fun hasAttachment(): Boolean {
        return replyIDs.isNotEmpty()
    }

}