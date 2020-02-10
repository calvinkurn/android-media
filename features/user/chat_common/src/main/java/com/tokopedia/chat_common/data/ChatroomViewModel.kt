package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author by nisie on 14/12/18.
 */
class ChatroomViewModel(val listChat: ArrayList<Visitable<*>> = ArrayList(),
                        val headerModel: ChatRoomHeaderViewModel = ChatRoomHeaderViewModel(),
                        val canLoadMore: Boolean = false,
                        val replyable: Boolean = false,
                        var blockedStatus: BlockedStatus = BlockedStatus()) {

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

    fun getInterlocutorName(): CharSequence {
        var name = headerModel.name
        if (name.length > 12) {
            name = name.substring(0, 12) + "..."
        }
        return name
    }

}