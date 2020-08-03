package com.tokopedia.topchat.chatsetting.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsetting.data.ChatSetting
import com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity
import kotlinx.android.synthetic.main.item_chat_setting.view.*

class ChatSettingViewHolder(itemView: View?, val listener: ChatSettingListener)
    : AbstractViewHolder<ChatSetting>(itemView) {

    interface ChatSettingListener {
        fun isTabSeller(): Boolean
        fun eventClickChatSetting(element: ChatSetting)
        fun goToSellerMigrationPage()
    }

    override fun bind(element: ChatSetting?) {
        if (element == null) return
        with(itemView) {
            tvTitle?.text = element.alias
            labelSellerMigration.showWithCondition(!GlobalConfig.isSellerApp() && isSellerTabTemplateChat(element.alias))

            setOnClickListener {
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
    }

    private fun isSellerTabTemplateChat(alias: String): Boolean = listener.isTabSeller() && alias == TEMPLATE_CHAT_TITLE

    companion object {
        const val TEMPLATE_CHAT_TITLE = "Template Chat"

        val LAYOUT = R.layout.item_chat_setting
    }
}