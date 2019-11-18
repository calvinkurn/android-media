package com.tokopedia.topchat.chatsetting.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsetting.data.ChatSetting
import kotlinx.android.synthetic.main.item_chat_setting.view.*

class ChatSettingViewHolder(itemView: View?) : AbstractViewHolder<ChatSetting>(itemView) {

    override fun bind(element: ChatSetting?) {
        if (element == null) return
        with (itemView) {
            tvTitle?.text = element.alias
        }
    }

    companion object {
        val LAYOUT = R.layout.item_chat_setting
    }
}