package com.tokopedia.topchat.chatsetting.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingDividerUiModel

class ChatSettingDividerViewHolder(
        itemView: View?
) : AbstractViewHolder<ChatSettingDividerUiModel>(itemView) {
    override fun bind(element: ChatSettingDividerUiModel) {

    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_setting_divider
    }
}