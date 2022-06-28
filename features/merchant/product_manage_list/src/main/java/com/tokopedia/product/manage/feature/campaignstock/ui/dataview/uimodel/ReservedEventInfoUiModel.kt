package com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockTypeFactory
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReservedEventInfoUiModel(
    val eventType: String,
    val campaignName: String,
    val campaignIconUrl: String,
    val startTime: String,
    val endTime: String,
    val period: String,
    val periodStatus: PeriodStatus,
    val stock: String
) : Parcelable, Visitable<CampaignStockTypeFactory> {

    override fun type(typeFactory: CampaignStockTypeFactory): Int =
        typeFactory.type(this)

    enum class PeriodStatus {
        ONGOING, UPCOMING
    }

}