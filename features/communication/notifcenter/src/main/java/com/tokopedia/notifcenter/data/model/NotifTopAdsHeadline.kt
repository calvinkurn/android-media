package com.tokopedia.notifcenter.data.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.view.adapter.typefactory.NotificationTypeFactory
import com.tokopedia.topads.sdk.domain.model.CpmModel

data class NotifTopAdsHeadline(
    val cpmModel: CpmModel
) : Visitable<NotificationTypeFactory> {
    override fun type(typeFactory: NotificationTypeFactory): Int {
        return typeFactory.type(this)
    }
}
