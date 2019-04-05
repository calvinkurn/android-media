package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory
import java.util.*

/**
 * @author by nisie on 3/22/18.
 */

class SprintSaleAnnouncementViewModel(val campaignId: String, createdAt: Long, updatedAt: Long,
                                      messageId: String,
                                      senderId: String, senderName: String, senderIconUrl: String,
                                      isInfluencer: Boolean, isAdministrator: Boolean, var redirectUrl: String?,
                                      val listProducts: ArrayList<SprintSaleProductViewModel>,
                                      val campaignName: String, val startDate: Long, val endDate: Long,
                                      val sprintSaleType: String) : BaseChatViewModel("", createdAt, updatedAt, messageId, senderId, senderName, senderIconUrl, isInfluencer, isAdministrator), Visitable<GroupChatTypeFactory> {

    override fun type(typeFactory: GroupChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {

        const val SPRINT_SALE_UPCOMING = "flashsale_upcoming"
        const val SPRINT_SALE_START = "flashsale_start"
        const val SPRINT_SALE_FINISH = "flashsale_end"
    }
}