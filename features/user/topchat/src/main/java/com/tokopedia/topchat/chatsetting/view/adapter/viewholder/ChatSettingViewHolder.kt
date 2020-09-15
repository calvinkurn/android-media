package com.tokopedia.topchat.chatsetting.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsetting.data.uimodel.ItemChatSettingUiModel
import com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class ChatSettingViewHolder constructor(
        itemView: View?,
        val listener: ChatSettingListener
) : AbstractViewHolder<ItemChatSettingUiModel>(itemView) {

    private val title: Typography? = itemView?.findViewById(R.id.tvTitle)
    private val description: Typography? = itemView?.findViewById(R.id.tvDesc)
    private val label: Label? = itemView?.findViewById(R.id.label_chat_setting)

    interface ChatSettingListener {
        fun isTabSeller(): Boolean
        fun eventClickChatSetting(element: ItemChatSettingUiModel)
        fun goToSellerMigrationPage()
    }

    override fun bind(element: ItemChatSettingUiModel) {
        initTitle(element)
        initDescription(element)
        initLabel(element)
        initClickListener(element)
    }

    private fun initTitle(element: ItemChatSettingUiModel) {
        title?.shouldShowWithAction(element.alias.isNotEmpty()) {
            title.text = element.alias
        }
    }

    private fun initLabel(element: ItemChatSettingUiModel) {
        label?.showWithCondition(!GlobalConfig.isSellerApp() && isSellerTabTemplateChat(element.alias))
    }

    private fun initDescription(element: ItemChatSettingUiModel) {
        description?.text = element.description
    }

    private fun initClickListener(element: ItemChatSettingUiModel) {
        itemView.setOnClickListener {
            if (!GlobalConfig.isSellerApp() && isSellerTabTemplateChat(element.alias)) {
                listener.goToSellerMigrationPage()
            } else {
                listener.eventClickChatSetting(element)
                val intent = RouteManager.getIntent(itemView.context, element.link).apply {
                    putExtra(TemplateChatActivity.PARAM_IS_SELLER, listener.isTabSeller())
                }
                it.context.startActivity(intent)
            }
        }
    }

    private fun isSellerTabTemplateChat(alias: String): Boolean = listener.isTabSeller() && alias == TEMPLATE_CHAT_TITLE

    companion object {
        const val TEMPLATE_CHAT_TITLE = "Template Chat"

        val LAYOUT = R.layout.item_chat_setting
    }
}