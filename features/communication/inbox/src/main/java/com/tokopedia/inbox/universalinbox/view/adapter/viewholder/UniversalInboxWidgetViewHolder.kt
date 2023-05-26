package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.inbox.databinding.UniversalInboxWidgetItemBinding
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxWidgetListener
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxWidgetViewHolder(
    itemView: View,
    private val listener: UniversalInboxWidgetListener
): BaseViewHolder(itemView) {

    private val binding: UniversalInboxWidgetItemBinding? by viewBinding()

    fun bind(uiModel: UniversalInboxWidgetUiModel) {
        bindIcon(uiModel)
        bindText(uiModel)
        bindCounter(uiModel)
        bindListener(uiModel)
    }

    private fun bindIcon(uiModel: UniversalInboxWidgetUiModel) {
        binding?.inboxIconWidget?.setImage(newIconId = uiModel.icon)
    }

    private fun bindText(uiModel: UniversalInboxWidgetUiModel) {
        binding?.inboxTvTitleWidget?.text = uiModel.title
        binding?.inboxTvSubtextWidget?.text = uiModel.subtext
    }

    private fun bindCounter(uiModel: UniversalInboxWidgetUiModel) {
        if (uiModel.counter > Int.ONE) {
            val strCounter = uiModel.counter.toString()
            if (strCounter.isNotEmpty()) {
                binding?.inboxNotificationIconWidget?.setNotification(
                    notif = strCounter,
                    notificationType = NotificationUnify.COUNTER_TYPE,
                    colorType = NotificationUnify.COLOR_PRIMARY
                )
                binding?.inboxNotificationIconWidget?.show()
            } else {
                binding?.inboxNotificationIconWidget?.hide()
            }
        } else {
            binding?.inboxNotificationIconWidget?.hide()
        }
    }

    private fun bindListener(uiModel: UniversalInboxWidgetUiModel) {
        binding?.inboxLayoutWidget?.setOnClickListener {
            listener.onClickWidget(uiModel)
        }
    }
}
