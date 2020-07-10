package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.StatusState
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getDrawable as drawable

class ConfigViewHolder(
        view: View
): AbstractViewHolder<ConfigUIView>(view) {

    private val pgLoader = view.findViewById<ProgressBar>(R.id.pgLoader)
    private val imgStatus = view.findViewById<ImageView>(R.id.imgStatus)
    private val txtTitle = view.findViewById<TextView>(R.id.txtTitle)

    private val context by lazy { itemView.context }

    override fun bind(element: ConfigUIView?) {
        if (element == null) return
        viewState(element)
        txtTitle?.text = element.title
        pgLoader?.show()
    }

    private fun viewState(element: ConfigUIView) {
        when (element.state) {
            is ConfigState.PushNotification -> troubleshootStatus(element.status)
            is ConfigState.Notification -> troubleshootStatus(element.status)
            is ConfigState.Categories -> troubleshootStatus(element.status)
            is ConfigState.Ringtone -> troubleshootStatus(element.status)
        }
    }

    private fun troubleshootStatus(status: StatusState) {
        when (status) {
            is StatusState.Success -> iconStatus(R.drawable.ic_green_checked)
            is StatusState.Error -> iconStatus(R.drawable.ic_red_error)
        }
    }

    private fun iconStatus(resource: Int) {
        val status = drawable(context, resource)
        imgStatus?.setImageDrawable(status)
        imgStatus?.show()
        pgLoader?.invisible()
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_config
    }

}