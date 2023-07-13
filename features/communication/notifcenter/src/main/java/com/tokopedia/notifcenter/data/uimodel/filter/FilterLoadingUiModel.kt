package com.tokopedia.notifcenter.data.uimodel.filter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.view.adapter.typefactory.NotificationFilterTypeFactory

class FilterLoadingUiModel : Visitable<NotificationFilterTypeFactory> {
    override fun type(typeFactory: NotificationFilterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
