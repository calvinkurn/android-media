package com.tokopedia.notifcenter.data.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.view.adapter.typefactory.NotificationTypeFactory

class BigDividerUiModel : Visitable<NotificationTypeFactory> {

    override fun type(typeFactory: NotificationTypeFactory): Int {
        return typeFactory.type(this)
    }

}
