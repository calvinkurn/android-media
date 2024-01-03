package com.tokopedia.product.detail.view.viewholder.campaign.ui.model

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentMainData
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class OngoingCampaignUiModel(
    private val type: String = "",
    private val name: String = "",
    override val impressHolder: ImpressHolder = ImpressHolder(),
    var shouldShow: Boolean = true,
    var data: ProductContentMainData? = null
) : DynamicPdpDataModel {
    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int = typeFactory.type(this)

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return newData is OngoingCampaignUiModel &&
            shouldShow == newData.shouldShow &&
            data?.hashCode() == newData.data?.hashCode()
    }

    override fun newInstance(): DynamicPdpDataModel = this.copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null
}
