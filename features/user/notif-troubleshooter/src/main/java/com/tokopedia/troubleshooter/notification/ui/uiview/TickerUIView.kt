package com.tokopedia.troubleshooter.notification.ui.uiview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TroubleshooterTypeFactory

data class TickerUIView(
        val title: Int = 0,
        val message: CharSequence = ""
): Visitable<TroubleshooterTypeFactory> {

    override fun type(typeFactory: TroubleshooterTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun showTicker(message: CharSequence): TickerUIView {
            return TickerUIView(
                    title = R.string.notif_ticker_title,
                    message = message
            )
        }
    }

}