package com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel.chatroom

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.room.domain.pojo.BaseGroupChatPojo
import com.tokopedia.groupchat.chatroom.kotlin.view.adapter.chatroom.typefactory.GroupChatTypeFactory

/**
 * @author by nisie on 3/29/18.
 */

class VibrateViewModel : BaseGroupChatPojo(), Visitable<GroupChatTypeFactory> {

    override fun type(typeFactory: GroupChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        val TYPE = "is_vibrate"
    }
}
