package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.databinding.ItemNotificationConfigBinding
import com.tokopedia.troubleshooter.notification.ui.listener.ConfigItemListener
import com.tokopedia.troubleshooter.notification.ui.state.ConfigState
import com.tokopedia.troubleshooter.notification.ui.state.ConfigUIView
import com.tokopedia.troubleshooter.notification.ui.state.StatusState
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getDrawable as drawable

open class ConfigViewHolder(
        private val listener: ConfigItemListener,
        view: View
): AbstractViewHolder<ConfigUIView>(view) {

    private val binding: ItemNotificationConfigBinding? by viewBinding()
    private val context by lazy { itemView.context }

    override fun bind(element: ConfigUIView?) {
        if (element == null) return
        binding?.txtTitle?.text = context.getString(element.title)
        binding?.pgLoader?.show()

        viewState(element)
    }

    private fun viewState(element: ConfigUIView) {
        troubleshootStatus(element)

        if (element.state == ConfigState.Ringtone) {
            binding?.btnAction?.show()
            binding?.btnAction?.setOnClickListener {
                element.ringtone?.let { listener.onRingtoneTest(it) }
            }
        } else {
            binding?.btnAction?.hide()
        }
    }

    private fun troubleshootStatus(element: ConfigUIView) {
        val message = ConfigUIView.itemMessage(element)

        when (element.status) {
            is StatusState.Success -> {
                visibility(R.drawable.ic_ts_notif_checked)
            }
            is StatusState.Error -> {
                visibility(R.drawable.ic_ts_notif_failed)
            }
            is StatusState.Warning -> {
                visibility(R.drawable.ic_ts_notif_warning)
            }
            else -> {}
        }

        binding?.txtTitle?.text = context.getString(message)
    }

    private fun visibility(resource: Int) {
        binding?.imgStatus?.setImageDrawable(drawable(context, resource))
        binding?.imgStatus?.show()
        binding?.pgLoader?.hide()
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_config
    }

}