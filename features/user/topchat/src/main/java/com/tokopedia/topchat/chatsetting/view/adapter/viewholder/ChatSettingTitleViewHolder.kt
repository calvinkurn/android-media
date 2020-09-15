package com.tokopedia.topchat.chatsetting.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingTitleUiModel
import com.tokopedia.unifyprinciples.Typography

class ChatSettingTitleViewHolder(
        itemView: View?
) : AbstractViewHolder<ChatSettingTitleUiModel>(itemView) {

    private val icon: ImageView? = itemView?.findViewById(R.id.iv_icon)
    private val title: Typography? = itemView?.findViewById(R.id.tv_title)

    override fun bind(element: ChatSettingTitleUiModel) {
        bindIcon(element)
        bindTitle(element)
    }

    private fun bindIcon(element: ChatSettingTitleUiModel) {
        icon?.setBackgroundResource(element.icon)
    }

    private fun bindTitle(element: ChatSettingTitleUiModel) {
        title?.setText(element.title)
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_setting_title
    }
}