package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigUIView

class ConfigViewHolder(
        view: View
): AbstractViewHolder<ConfigUIView>(view) {

    private val pgLoader = view.findViewById<ProgressBar>(R.id.pgLoader)
    private val imgStatus = view.findViewById<ImageView>(R.id.imgStatus)
    private val txtTitle = view.findViewById<TextView>(R.id.txtTitle)

    override fun bind(element: ConfigUIView?) {
        if (element == null) return

        txtTitle.text = element.title
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_config
    }

}