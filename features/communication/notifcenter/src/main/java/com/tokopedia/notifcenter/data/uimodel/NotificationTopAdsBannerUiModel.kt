package com.tokopedia.notifcenter.data.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.view.adapter.typefactory.NotificationTypeFactory
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

data class NotificationTopAdsBannerUiModel constructor(
        val ads: ArrayList<TopAdsImageViewModel>
) : Visitable<NotificationTypeFactory> {

    override fun type(typeFactory: NotificationTypeFactory): Int {
        return typeFactory.type(this)
    }

}
