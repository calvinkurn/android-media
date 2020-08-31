package com.tokopedia.troubleshooter.notification.ui.uiview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TroubleshooterTypeFactory

data class TickerUIView(
        val message: CharSequence = "",
        val buttonText: String = ""
): Visitable<TroubleshooterTypeFactory> {

    override fun type(typeFactory: TroubleshooterTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun ticker(
                message: CharSequence,
                buttonText: String
        ): TickerUIView {
            return TickerUIView(
                    message = message,
                    buttonText = buttonText
            )
        }
    }

}