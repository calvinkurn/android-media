package com.tokopedia.topchat.chatsetting.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsetting.data.uimodel.ItemChatSettingUiModel
import com.tokopedia.topchat.chatsetting.data.uimodel.ItemChatSettingUiModel.Companion.TYPE_GREEN
import com.tokopedia.topchat.chatsetting.data.uimodel.ItemChatSettingUiModel.Companion.TYPE_RED
import com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class ChatSettingViewHolder constructor(
        itemView: View?,
        val listener: ChatSettingListener
) : AbstractViewHolder<ItemChatSettingUiModel>(itemView) {

    private val title: Typography? = itemView?.findViewById(R.id.tvTitle)
    private val description: Typography? = itemView?.findViewById(R.id.tvDesc)
    private val label: NotificationUnify? = itemView?.findViewById(R.id.cs_nu_label)

    interface ChatSettingListener {
        fun isSeller(): Boolean
        fun eventClickChatSetting(element: ItemChatSettingUiModel)
        fun isNextItemDivider(adapterPosition: Int): Boolean
        fun isPreviousItemTitle(adapterPosition: Int): Boolean
    }

    override fun bind(element: ItemChatSettingUiModel) {
        initTitle(element)
        initDescription(element)
        initClickListener(element)
        initPadding(element)
        bindLabel(element)
    }

    private fun bindLabel(element: ItemChatSettingUiModel) {
        label?.shouldShowWithAction(
            element.label.isNotEmpty() && element.alias.isNotEmpty()
        ) {
            val labelColor = when (element.typeLabel) {
                TYPE_RED -> NotificationUnify.COLOR_PRIMARY
                TYPE_GREEN -> NotificationUnify.COLOR_SECONDARY
                else -> NotificationUnify.COLOR_PRIMARY
            }
            label.setNotification(element.label,labelColor, NotificationUnify.TEXT_TYPE)
        }
    }

    private fun initTitle(element: ItemChatSettingUiModel) {
        title?.shouldShowWithAction(element.alias.isNotEmpty()) {
            title.text = element.alias
        }
    }

    private fun initDescription(element: ItemChatSettingUiModel) {
        description?.text = element.description
    }

    private fun initClickListener(element: ItemChatSettingUiModel) {
        itemView.setOnClickListener {
            listener.eventClickChatSetting(element)
            val intent = RouteManager.getIntent(itemView.context, element.link).apply {
                putExtra(TemplateChatActivity.PARAM_IS_SELLER, listener.isSeller())
            }
            it.context.startActivity(intent)
        }
    }

    private fun initPadding(element: ItemChatSettingUiModel) {
        val top = if (title?.isVisible != true && listener.isPreviousItemTitle(adapterPosition)) {
            paddingTopNoTitle
        } else {
            paddingTop
        }
        val bottom = if (listener.isNextItemDivider(adapterPosition)) {
            paddingBottomLastSection
        } else {
            paddingBottom
        }
        itemView.setPadding(paddingStart, top, paddingEnd, bottom)
    }

    companion object {
        private val paddingStart = 16.toPx()
        private val paddingEnd = 16.toPx()
        private val paddingTop = 12.toPx()
        private val paddingBottom = 15.toPx()
        private val paddingTopNoTitle = 5.toPx()
        private val paddingBottomLastSection = 20.toPx()

        val LAYOUT = R.layout.item_chat_setting
    }
}