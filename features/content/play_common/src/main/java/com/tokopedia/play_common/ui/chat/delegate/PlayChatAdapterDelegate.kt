package com.tokopedia.play_common.ui.chat.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.ui.chat.viewholder.PlayChatViewHolder

/**
 * Created by jegul on 09/06/20
 */
class PlayChatAdapterDelegate(
        val typographyType: Int
) : TypedAdapterDelegate<PlayChatUiModel, PlayChatUiModel, PlayChatViewHolder>(PlayChatViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: PlayChatUiModel, holder: PlayChatViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayChatViewHolder {
        return PlayChatViewHolder(basicView, typographyType)
    }
}