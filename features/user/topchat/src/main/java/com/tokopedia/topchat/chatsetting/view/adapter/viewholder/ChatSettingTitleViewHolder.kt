package com.tokopedia.topchat.chatsetting.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingTitleUiModel

class ChatSettingTitleViewHolder(
        itemView: View?
) : AbstractViewHolder<ChatSettingTitleUiModel>(itemView) {
    override fun bind(element: ChatSettingTitleUiModel) {

    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_setting_title
    }
}