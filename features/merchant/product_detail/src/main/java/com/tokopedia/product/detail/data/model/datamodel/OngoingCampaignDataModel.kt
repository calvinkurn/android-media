package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class OngoingCampaignDataModel(
    private val type: String = "",
    private val name: String = "",
    override val impressHolder: ImpressHolder = ImpressHolder(),
    var data: ProductContentMainData? = null,
    var upcomingNplData: UpcomingNplDataModel = UpcomingNplDataModel()
) : DynamicPdpDataModel {
    override fun tabletSectionPosition(): TabletPosition = TabletPosition.LEFT

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int = typeFactory.type(this)

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return newData is OngoingCampaignDataModel &&
            data?.hashCode() == newData.data?.hashCode() &&
            upcomingNplData.hashCode() == newData.upcomingNplData.hashCode()
    }

    override fun newInstance(): DynamicPdpDataModel = this.copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null

    fun isNpl(): Boolean {
        return upcomingNplData.upcomingType.isNotEmpty()
    }
}
