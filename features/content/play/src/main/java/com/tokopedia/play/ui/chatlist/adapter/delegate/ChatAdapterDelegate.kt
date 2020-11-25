package com.tokopedia.play.ui.chatlist.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.ui.chat.viewholder.PlayChatViewHolder
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 04/12/19
 */
class ChatAdapterDelegate : TypedAdapterDelegate<PlayChatUiModel, PlayChatUiModel, PlayChatViewHolder>(com.tokopedia.play_common.R.layout.item_play_chat) {

    override fun onBindViewHolder(item: PlayChatUiModel, holder: PlayChatViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayChatViewHolder {
        return PlayChatViewHolder(basicView, Typography.BODY_3)
    }
}