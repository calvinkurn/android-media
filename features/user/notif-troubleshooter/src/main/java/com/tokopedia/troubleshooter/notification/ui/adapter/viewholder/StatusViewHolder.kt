package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.state.StatusState
import com.tokopedia.troubleshooter.notification.ui.uiview.StatusUIView
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography

class StatusViewHolder(
        view: View
): AbstractViewHolder<StatusUIView>(view) {

    private val imgStatus = view.findViewById<ImageView>(R.id.imgStatus)
    private val pgLoader = view.findViewById<LoaderUnify>(R.id.pgLoader)
    private val txtStatus = view.findViewById<Typography>(R.id.txtStatus)

    private val context by lazy { itemView.context }

    override fun bind(element: StatusUIView?) {
        if (element == null) return

        when (element.state) {
            is StatusState.Loading -> loading()
            is StatusState.Success -> success()
            is StatusState.Warning -> warning()
        }
    }

    private fun loading() {
        pgLoader?.show()
        txtStatus?.text = context.getString(R.string.notif_status_waiting)
    }

    private fun success() {
        pgLoader?.hide()
        txtStatus?.text = context.getString(R.string.notif_status_checked)
        imgStatus?.loadImageDrawable(R.drawable.ic_ts_notif_status_sucess)
    }

    private fun warning() {
        pgLoader?.hide()
        txtStatus?.text = context.getString(R.string.notif_status_warning)
        imgStatus?.loadImageDrawable(R.drawable.ic_ts_notif_status_warning)
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_status
    }

}