package com.tokopedia.troubleshooter.notification.ui.uiview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TickerTypeFactory
import com.tokopedia.troubleshooter.notification.ui.state.ConfigState
import com.tokopedia.troubleshooter.notification.ui.state.ConfigState.Undefined as Undefined

data class TickerItemUIView(
        val message: CharSequence = "",
        val type: ConfigState = Undefined,
        val buttonText: String = ""
): Visitable<TickerTypeFactory> {

    override fun type(typeFactory: TickerTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun ticker(
                type: ConfigState,
                message: CharSequence,
                buttonText: String
        ): TickerItemUIView {
            return TickerItemUIView(
                    type = type,
                    message = message,
                    buttonText = buttonText
            )
        }
    }

}