package com.tokopedia.troubleshooter.notification.ui.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigUIView

interface TroubleshooterTypeFactory : AdapterTypeFactory {
    fun type(config: ConfigUIView): Int
}