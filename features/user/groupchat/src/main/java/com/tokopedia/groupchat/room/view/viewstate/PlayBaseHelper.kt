package com.tokopedia.groupchat.room.view.viewstate

import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel

/**
 * @author : Steven 28/05/19
 */
abstract class PlayBaseHelper(var viewModel: ChannelInfoViewModel?) {

    open fun assignViewModel(model: ChannelInfoViewModel) {
        this.viewModel = model
    }
}