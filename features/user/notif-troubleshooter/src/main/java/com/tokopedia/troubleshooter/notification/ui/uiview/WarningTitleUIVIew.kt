package com.tokopedia.troubleshooter.notification.ui.uiview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TroubleshooterTypeFactory

data class WarningTitleUIVIew(val title: String): Visitable<TroubleshooterTypeFactory> {

    override fun type(typeFactory: TroubleshooterTypeFactory): Int {
        return typeFactory.type(this)
    }

}