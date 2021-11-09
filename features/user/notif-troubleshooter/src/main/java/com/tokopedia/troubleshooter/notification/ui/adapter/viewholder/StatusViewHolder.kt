package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.databinding.ItemNotificationStatusBinding
import com.tokopedia.troubleshooter.notification.ui.state.StatusState
import com.tokopedia.troubleshooter.notification.ui.uiview.StatusUIView
import com.tokopedia.utils.view.binding.viewBinding

class StatusViewHolder(
        view: View
): AbstractViewHolder<StatusUIView>(view) {

    private val binding: ItemNotificationStatusBinding? by viewBinding()
    private val context by lazy { itemView.context }

    override fun bind(element: StatusUIView?) {
        if (element == null) return

        when (element.state) {
            is StatusState.Loading -> loading()
            is StatusState.Success -> success()
            is StatusState.Warning -> warning()
            else -> {}
        }
    }

    private fun loading() {
        binding?.pgLoader?.show()
        binding?.txtStatus?.text = context.getString(R.string.notif_status_waiting)
    }

    private fun success() {
        binding?.pgLoader?.hide()
        binding?.txtStatus?.text = context.getString(R.string.notif_status_checked)
        binding?.imgStatus?.loadImageDrawable(R.drawable.ic_ts_notif_status_sucess)
    }

    private fun warning() {
        binding?.pgLoader?.hide()
        binding?.txtStatus?.text = context.getString(R.string.notif_status_warning)
        binding?.imgStatus?.loadImageDrawable(R.drawable.ic_ts_notif_status_warning)
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_status
    }

}