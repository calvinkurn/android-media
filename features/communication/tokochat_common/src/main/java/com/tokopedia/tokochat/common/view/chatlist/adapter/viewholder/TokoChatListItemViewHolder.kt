package com.tokopedia.tokochat.common.view.chatlist.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokochat.common.util.TokoChatUrlUtil.IC_TOKOFOOD_SOURCE
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.TOKOFOOD_SERVICE_TYPE
import com.tokopedia.tokochat.common.view.chatlist.listener.TokoChatListItemListener
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatListItemChatListBinding
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.utils.view.binding.viewBinding

class TokoChatListItemViewHolder(
    view: View,
    private val listener: TokoChatListItemListener
) : BaseViewHolder(view) {

    private val binding: TokochatListItemChatListBinding? by viewBinding()
    private var imageUrl: String = ""

    fun bind(element: TokoChatListItemUiModel) {
        bindDriver(element)
        bindMessage(element)
        bindTime(element)
        bindCounter(element)
        bindListener(element)
    }

    private fun bindDriver(element: TokoChatListItemUiModel) {
        binding?.tokochatListTvDriverName?.text = element.driverName
        if (imageUrl != element.imageUrl) {
            binding?.tokochatListIvDriver?.loadImage(element.imageUrl)
            imageUrl = element.imageUrl
        }
        val logoUrl = when (element.serviceType) {
            TOKOFOOD_SERVICE_TYPE -> IC_TOKOFOOD_SOURCE
            else -> ""
        }
        binding?.tokochatListIvLogo?.loadImage(logoUrl)
    }

    private fun bindMessage(element: TokoChatListItemUiModel) {
        binding?.tokochatListTvMessage?.apply {
            if (element.message.isNotBlank()) {
                text = element.message
                show()
            } else {
                hide()
            }
        }
        binding?.tokochatListTvBusinessName?.apply {
            if (element.business.isNotBlank()) {
                text = element.business
                show()
            } else {
                hide()
            }
        }
    }

    private fun bindTime(element: TokoChatListItemUiModel) {
        binding?.tokochatListTvTime?.text = element.getRelativeTime()
        binding?.tokochatListCounter?.setNotification(
            notif = element.counter.toString(),
            notificationType = NotificationUnify.COUNTER_TYPE,
            NotificationUnify.COLOR_PRIMARY
        )
    }

    private fun bindCounter(element: TokoChatListItemUiModel) {
        binding?.tokochatListCounter?.apply {
            if (element.counter > Int.ZERO) {
                setNotification(
                    notif = element.counter.toString(),
                    notificationType = NotificationUnify.COUNTER_TYPE,
                    NotificationUnify.COLOR_PRIMARY
                )
                show()
            } else {
                invisible()
            }
        }
    }

    private fun bindListener(element: TokoChatListItemUiModel) {
        binding?.tokochatListLayoutItem?.setOnClickListener {
            listener.onClickChatItem(element)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.tokochat_list_item_chat_list
    }
}
