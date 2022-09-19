package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import com.tokopedia.play.R
import com.tokopedia.play.view.custom.PlayChatListView
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 03/08/20
 */
class ChatListViewComponent(
    container: ViewGroup,
    @IdRes idRes: Int
) : ViewComponent(container, idRes) {

    private val playChatListView: PlayChatListView = findViewById(R.id.view_chat_list)

    fun setChatList(chatList: List<PlayChatUiModel>) {
        playChatListView.setChatList(chatList)
    }
}