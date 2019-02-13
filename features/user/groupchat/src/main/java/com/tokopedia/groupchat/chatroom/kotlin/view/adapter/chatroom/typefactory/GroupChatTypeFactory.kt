package com.tokopedia.groupchat.chatroom.kotlin.view.adapter.chatroom.typefactory

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel.chatroom.*
import com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel.interupt.OverlayCloseViewModel
import com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel.interupt.OverlayViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PendingChatViewModel

/**
 * @author by nisie on 2/7/18.
 */

interface GroupChatTypeFactory {

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

    fun type(adminAnnouncementViewModel: AdminAnnouncementViewModel): Int

    fun type(myChatViewModel: ChatViewModel): Int

    fun type(pendingChatViewModel: PendingChatViewModel): Int

    fun type(userActionViewModel: UserActionViewModel): Int

    fun type(imageViewModel: ImageAnnouncementViewModel): Int

    fun type(voteAnnouncementViewModel: VoteAnnouncementViewModel): Int

    fun type(flashSaleViewModel: SprintSaleAnnouncementViewModel): Int

    fun type(groupChatPointsViewModel: GroupChatPointsViewModel): Int

    fun type(vibrateViewModel: VibrateViewModel): Int

    fun type(generatedMessageViewModel: GeneratedMessageViewModel): Int

    fun type(viewModel: OverlayViewModel): Int

    fun type(overlayCloseViewModel: OverlayCloseViewModel): Int
}
