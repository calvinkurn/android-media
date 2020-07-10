package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.listener.ConfigItemListener
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.StatusState
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getDrawable as drawable

open class ConfigViewHolder(
        private val listener: ConfigItemListener,
        view: View
): AbstractViewHolder<ConfigUIView>(view) {

    private val pgLoader = view.findViewById<ProgressBar>(R.id.pgLoader)
    private val imgStatus = view.findViewById<ImageView>(R.id.imgStatus)
    private val txtTitle = view.findViewById<TextView>(R.id.txtTitle)
    private val txtMessage = view.findViewById<TextView>(R.id.txtMessage)
    private val context by lazy { itemView.context }

    override fun bind(element: ConfigUIView?) {
        if (element == null) return

        viewState(element)
        txtTitle?.text = element.title
        pgLoader?.show()
    }

    private fun viewState(element: ConfigUIView) {
        with(element) {
            when (state) {
                is ConfigState.PushNotification -> pushNotification(element)
                is ConfigState.Notification -> notificationSetting(status)
                is ConfigState.Categories -> notificationCategories(element)
                is ConfigState.Ringtone -> notificationRingtone(status, ringtone)
            }
        }
    }

    private fun pushNotification(element: ConfigUIView) {
        troubleshootStatus(element.status)

        if (element.message.isNotEmpty()) {
            txtMessage?.show()
            txtMessage?.text = element.message
        }
    }

    private fun notificationSetting(status: StatusState) {
        troubleshootStatus(status)

        itemView.setOnClickListener {
            listener.goToNotificationSettings()
        }
    }

    private fun notificationCategories(element: ConfigUIView) {
        if (!element.visibility) itemView.hide()
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
            is StatusState.Success -> {
                iconStatusVisibility(R.drawable.ic_green_checked)
            }
            is StatusState.Error -> {
                iconStatusVisibility(R.drawable.ic_red_error)
            }
        }
    }

    private fun iconStatusVisibility(resource: Int) {
        val status = drawable(context, resource)
        imgStatus?.setImageDrawable(status)
        imgStatus?.show()
        pgLoader?.invisible()
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_config
    }

}