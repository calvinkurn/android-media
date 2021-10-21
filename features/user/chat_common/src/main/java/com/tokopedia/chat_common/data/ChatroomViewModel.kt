package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author by nisie on 14/12/18.
 */
class ChatroomViewModel constructor(
        val listChat: ArrayList<Visitable<*>> = ArrayList(),
        val headerModel: ChatRoomHeaderViewModel = ChatRoomHeaderViewModel(),
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
        var shopType = "reguler"
        if (headerModel.isGold) {
            shopType = "gold_merchant"
        } else if (headerModel.isOfficial) {
            shopType = "official_Store"
        }
        return shopType
    }
    val badgeUrl get() = headerModel.badge

    val role get() = headerModel.role.toLowerCase(Locale.getDefault())

    fun isSeller(): Boolean {
        return role == ChatRoomHeaderViewModel.Companion.ROLE_USER
    }

    fun isChattingWithSeller(): Boolean {
        return role.contains(ChatRoomHeaderViewModel.Companion.ROLE_SHOP)
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