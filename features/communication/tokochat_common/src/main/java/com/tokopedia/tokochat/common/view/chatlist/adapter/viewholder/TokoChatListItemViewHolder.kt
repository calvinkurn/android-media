package com.tokopedia.tokochat.common.view.chatlist.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokochat.common.util.TokoChatTimeUtil.getRelativeTime
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.getFirstNameDriver
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.getSourceIcon
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
        bindOrderType(element)
        bindMessage(element)
        bindBusiness(element)
        bindListener(element)
    }

    private fun bindDriver(element: TokoChatListItemUiModel) {
        binding?.tokochatListTvDriverName?.text = getFirstNameDriver(element.driverName)
        if (imageUrl != element.imageUrl) {
            binding?.tokochatListIvDriver?.loadImage(element.imageUrl)
            imageUrl = element.imageUrl
        }
        binding?.tokochatListIvLogo?.loadImage(getSourceIcon(element.serviceType))
    }

    private fun bindOrderType(element: TokoChatListItemUiModel) {
        val orderName = " ${getString(
            R.string.tokochat_list_driver_order_type,
            element.getStringOrderType()
        )}"
        binding?.tokochatListTvOrderType?.text = orderName
    }

    private fun bindMessage(element: TokoChatListItemUiModel) {
        val isMessageNotEmpty = element.message.isNotBlank()
        if (isMessageNotEmpty) {
            binding?.tokochatListTvMessage?.text = element.message
        } else {
            binding?.tokochatListTvMessage?.text = getString(R.string.tokochat_list_default_message)
        }
        bindCounter(element, isMessageNotEmpty)
        bindTime(element, isMessageNotEmpty)
    }

    private fun bindBusiness(element: TokoChatListItemUiModel) {
        binding?.tokochatListTvBusinessName?.apply {
            if (element.getBusinessName().isNotBlank()) {
                text = element.getBusinessName()
                show()
            } else {
                hide()
            }
        }
    }

    private fun bindTime(element: TokoChatListItemUiModel, shouldShow: Boolean) {
        binding?.tokochatListTvTime?.apply {
            text = getRelativeTime(
                timeMillis = element.createAt
            )
            showWithCondition(shouldShow)
        }
    }

    private fun bindCounter(element: TokoChatListItemUiModel, shouldShow: Boolean) {
        binding?.tokochatListCounter?.apply {
            if (element.counter > Int.ZERO && shouldShow) {
                show()
                setNotification(
                    notif = element.counter.toString(),
                    notificationType = NotificationUnify.COUNTER_TYPE,
                    NotificationUnify.COLOR_PRIMARY
                )
            } else {
                hide()
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
