package com.tokopedia.groupchat.room.view.viewstate

import android.view.View
import com.google.android.youtube.player.YouTubePlayer
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author : Steven 28/05/19
 */
abstract class PlayBaseHelper(var viewModel: ChannelInfoViewModel?) {

    open fun assignViewModel(model: ChannelInfoViewModel) {
        this.viewModel = model
    }
}