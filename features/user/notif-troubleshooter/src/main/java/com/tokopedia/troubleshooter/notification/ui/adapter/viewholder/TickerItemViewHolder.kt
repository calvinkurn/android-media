package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.databinding.ItemNotificationTickerBinding
import com.tokopedia.troubleshooter.notification.ui.state.ConfigState
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerItemUIView
import com.tokopedia.troubleshooter.notification.util.gotoAudioSetting
import com.tokopedia.troubleshooter.notification.util.gotoNotificationSetting
import com.tokopedia.utils.view.binding.viewBinding

class TickerItemViewHolder(
        view: View
): AbstractViewHolder<TickerItemUIView>(view) {

    private val binding: ItemNotificationTickerBinding? by viewBinding()
    private val context by lazy { itemView.context }

    override fun bind(element: TickerItemUIView?) {
        if (element == null) return
        binding?.txtDescription?.text = element.message
        binding?.btnActivation?.text = element.buttonText
        binding?.btnActivation?.setOnClickListener {
            when (element.type) {
                is ConfigState.Device -> {
                    context?.gotoNotificationSetting()
                }
                is ConfigState.Notification -> {
                    val appLink = "$USER_NOTIFICATION_SETTING$PUSH_NOTIFICATION_NS_QUERY"
                    context?.startActivity(RouteManager.getIntent(context, appLink))
                }
                is ConfigState.Ringtone -> {
                    context?.gotoAudioSetting()
                }
                else -> {}
            }
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_ticker

        private const val PUSH_NOTIFICATION_NS_QUERY = "?push_notification=true"
    }

}