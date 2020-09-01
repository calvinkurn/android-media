package com.tokopedia.troubleshooter.notification.ui.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.FooterUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.WarningTitleUIVIew

interface TroubleshooterTypeFactory : AdapterTypeFactory {
    fun type(config: ConfigUIView): Int
    fun type(ticker: TickerUIView): Int
    fun type(title: WarningTitleUIVIew): Int
    fun type(footer: FooterUIView): Int
}