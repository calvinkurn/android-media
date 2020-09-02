package com.tokopedia.troubleshooter.notification.ui.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerUIView

interface TroubleshooterTypeFactory : AdapterTypeFactory {
    fun type(config: ConfigUIView): Int
    fun type(ticker: TickerUIView): Int
}