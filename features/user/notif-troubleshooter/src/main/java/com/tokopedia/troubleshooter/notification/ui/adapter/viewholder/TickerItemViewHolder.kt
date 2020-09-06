package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.listener.ConfigItemListener
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerItemUIView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class TickerItemViewHolder(
        private val listener: ConfigItemListener,
        view: View
): AbstractViewHolder<TickerItemUIView>(view) {

    private val txtDescription: Typography? = view.findViewById(R.id.txtDescription)
    private val btnActivation: UnifyButton? = view.findViewById(R.id.btnActivation)

    override fun bind(element: TickerItemUIView?) {
        if (element == null) return
        txtDescription?.text = element.message
        btnActivation?.text = element.buttonText

        btnActivation?.setOnClickListener {
            listener.goToNotificationSettings()
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_ticker
    }

}