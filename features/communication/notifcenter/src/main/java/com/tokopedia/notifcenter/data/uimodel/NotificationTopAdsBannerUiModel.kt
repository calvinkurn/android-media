package com.tokopedia.notifcenter.data.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.view.adapter.typefactory.NotificationTypeFactory
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel

data class NotificationTopAdsBannerUiModel constructor(
        val ads: ArrayList<TopAdsImageUiModel>
) : Visitable<NotificationTypeFactory> {

    override fun type(typeFactory: NotificationTypeFactory): Int {
        return typeFactory.type(this)
    }

}
