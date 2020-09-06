package com.tokopedia.troubleshooter.notification.ui.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerItemUIView

interface TickerTypeFactory : AdapterTypeFactory {
    fun type(ticker: TickerItemUIView): Int
}