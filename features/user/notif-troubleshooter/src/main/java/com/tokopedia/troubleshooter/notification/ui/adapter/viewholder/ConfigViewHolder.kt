package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.listener.ConfigItemListener
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.StatusState
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getDrawable as drawable

open class ConfigViewHolder(
        private val listener: ConfigItemListener,
        view: View
): AbstractViewHolder<ConfigUIView>(view) {

    private val pgLoader = view.findViewById<LoaderUnify>(R.id.pgLoader)
    private val imgStatus = view.findViewById<ImageView>(R.id.imgStatus)
    private val txtTitle = view.findViewById<TextView>(R.id.txtTitle)
    private val context by lazy { itemView.context }

    override fun bind(element: ConfigUIView?) {
        if (element == null) return
        txtTitle?.text = context?.getString(element.title)
        pgLoader?.show()
        viewState(element)
    }

    private fun viewState(element: ConfigUIView) {
        with(element) {
            troubleshootStatus(this)

            when (state) {
                is ConfigState.Notification -> {
                    itemView.setOnClickListener {
                        listener.goToNotificationSettings()
                    }
                }
                is ConfigState.Device -> {
                    itemView.setOnClickListener {
                        listener.goToNotificationSettings()
                    }
                }
                is ConfigState.Ringtone -> {
                    itemView.setOnClickListener {
                        ringtone?.let { listener.onRingtoneTest(it) }
                    }
                }
                is ConfigState.PushNotification -> {}
            }
        }
    }

    private fun troubleshootStatus(element: ConfigUIView) {
        when (element.status) {
            is StatusState.Success -> {
                visibility(R.drawable.ic_ts_notif_checked)
            }
            is StatusState.Error -> {
                visibility(R.drawable.ic_ts_notif_failed)
            }
        }

        val message = ConfigUIView.itemMessage(element)
        txtTitle?.text = context.getString(message)
    }

    private fun visibility(resource: Int) {
        imgStatus?.setImageDrawable(drawable(context, resource))
        imgStatus?.show()
        pgLoader?.hide()
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_config
    }

}