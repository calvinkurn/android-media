package com.tokopedia.topchat.chatsetting.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingTitleUiModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class ChatSettingTitleViewHolder(
        itemView: View?
) : AbstractViewHolder<ChatSettingTitleUiModel>(itemView) {

    private val icon: ImageView? = itemView?.findViewById(R.id.iv_icon)
    private val title: Typography? = itemView?.findViewById(R.id.tv_title)
    private val label: Label? = itemView?.findViewById(R.id.label_chat_setting)

    override fun bind(element: ChatSettingTitleUiModel) {
        bindIcon(element)
        bindTitle(element)
        initLabel(element)
    }

    private fun bindIcon(element: ChatSettingTitleUiModel) {
        icon?.setBackgroundResource(element.icon)
    }

    private fun bindTitle(element: ChatSettingTitleUiModel) {
        title?.setText(element.title)
    }

    private fun initLabel(element: ChatSettingTitleUiModel) {
        label?.showWithCondition(!GlobalConfig.isSellerApp() && isTitleSeller(element.title))
    }

    private fun isTitleSeller(title: Int): Boolean {
        return title == R.string.title_topchat_as_seller
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_setting_title
    }
}