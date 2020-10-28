package com.tokopedia.troubleshooter.notification.ui.uiview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TroubleshooterTypeFactory
import com.tokopedia.troubleshooter.notification.ui.state.StatusState

data class StatusUIView(
        val state: StatusState = StatusState.Loading
): Visitable<TroubleshooterTypeFactory> {

    override fun type(typeFactory: TroubleshooterTypeFactory): Int {
        return typeFactory.type(this)
    }

}