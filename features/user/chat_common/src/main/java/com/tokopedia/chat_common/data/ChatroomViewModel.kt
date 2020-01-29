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

    val role get() = headerModel.role.toLowerCase(Locale.getDefault())

    fun isSeller(): Boolean {
        return role == ChatRoomHeaderViewModel.Companion.ROLE_USER
    }

    fun isChattingWithSeller(): Boolean {
        return role.contains(ChatRoomHeaderViewModel.Companion.ROLE_SHOP)
    }

}