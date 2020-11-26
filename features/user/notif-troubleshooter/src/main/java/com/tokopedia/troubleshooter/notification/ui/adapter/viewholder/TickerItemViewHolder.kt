package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.state.ConfigState
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerItemUIView
import com.tokopedia.troubleshooter.notification.util.gotoAudioSetting
import com.tokopedia.troubleshooter.notification.util.gotoNotificationSetting
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING as USER_NOTIFICATION_SETTING

class TickerItemViewHolder(
        view: View
): AbstractViewHolder<TickerItemUIView>(view) {

    private val txtDescription: Typography? = view.findViewById(R.id.txtDescription)
    private val btnActivation: UnifyButton? = view.findViewById(R.id.btnActivation)

    private val context by lazy { itemView.context }

    override fun bind(element: TickerItemUIView?) {
        if (element == null) return
        txtDescription?.text = element.message
        btnActivation?.text = element.buttonText

        btnActivation?.setOnClickListener {
            when (element.type) {
                is ConfigState.Device -> {
                    context.gotoNotificationSetting()
                }
                is ConfigState.Notification -> {
                    val appLink = "$USER_NOTIFICATION_SETTING$PUSH_NOTIFICATION_NS_QUERY"
                    context.startActivity(RouteManager.getIntent(context, appLink))
                }
                is ConfigState.Ringtone -> {
                    context?.gotoAudioSetting()
                }
            }
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_ticker

        private const val PUSH_NOTIFICATION_NS_QUERY = "?push_notification=true"
    }

}