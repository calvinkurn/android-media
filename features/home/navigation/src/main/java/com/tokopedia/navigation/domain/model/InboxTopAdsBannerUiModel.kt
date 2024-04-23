package com.tokopedia.navigation.domain.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.navigation.presentation.adapter.InboxTypeFactory
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel

class InboxTopAdsBannerUiModel constructor(
        var ads: List<TopAdsImageUiModel>? = null
) : Visitable<InboxTypeFactory> {

    val impressHolder: ImpressHolder = ImpressHolder()
    var requested = false

    override fun type(typeFactory: InboxTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun hasAds(): Boolean {
        return ads != null
    }
}
