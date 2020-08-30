package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.net.Uri
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
    private val txtMessage = view.findViewById<TextView>(R.id.txtMessage)
    private val context by lazy { itemView.context }

    override fun bind(element: ConfigUIView?) {
        if (element == null) return
        viewState(element)

        pgLoader?.show()
        txtTitle?.text = context?.getString(element.title)

        if (element.message.isNotEmpty()) {
            txtMessage?.text = element.message
            txtMessage?.show()
        }
    }

    private fun viewState(element: ConfigUIView) {
        with(element) {
            when (state) {
                is ConfigState.PushNotification -> pushNotification(element)
                is ConfigState.Notification -> notificationSetting(status)
                is ConfigState.Channel -> notificationChannel(element)
                is ConfigState.Ringtone -> notificationRingtone(status, ringtone)
            }
        }
    }

    private fun pushNotification(element: ConfigUIView) {
        troubleshootStatus(element.status)
    }

    private fun notificationSetting(status: StatusState) {
        troubleshootStatus(status)
        itemView.setOnClickListener {
            listener.goToNotificationSettings()
        }
    }

    private fun notificationChannel(element: ConfigUIView) {
        troubleshootStatus(element.status)
        itemView.setOnClickListener {
            listener.goToNotificationSettings()
        }
    }

    private fun notificationRingtone(status: StatusState, ringtone: Uri?) {
        troubleshootStatus(status)

        if (status == StatusState.Success) {
            txtTitle.text = context?.getString(R.string.notif_ringtone_success)
        } else if (status == StatusState.Error) {
            txtTitle.text = context?.getString(R.string.notif_ringtone_error)
        }

        itemView.setOnClickListener {
            ringtone?.let {
                listener.onRingtoneTest(ringtone)
            }
        }
    }

    private fun troubleshootStatus(status: StatusState) {
        when (status) {
            is StatusState.Success -> visibility(R.drawable.ic_ts_notif_checked)
            is StatusState.Error -> visibility(R.drawable.ic_ts_notif_failed)
        }
    }

    private fun visibility(resource: Int) {
        val status = drawable(context, resource)
        imgStatus?.setImageDrawable(status)
        imgStatus?.show()
        pgLoader?.hide()
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_config
    }

}