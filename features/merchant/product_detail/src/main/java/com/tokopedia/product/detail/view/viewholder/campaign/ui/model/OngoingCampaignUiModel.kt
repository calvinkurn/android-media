package com.tokopedia.product.detail.view.viewholder.campaign.ui.model

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentMainData
import com.tokopedia.product.detail.data.model.datamodel.TabletPosition
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class OngoingCampaignUiModel(
    private val type: String = "",
    private val name: String = "",
    override val impressHolder: ImpressHolder = ImpressHolder(),
    var shouldShowCampaign: Boolean = false,
    var shouldShowTradeIn: Boolean = false,
    var data: ProductContentMainData? = null
) : DynamicPdpDataModel {
    override val tabletSectionPosition: TabletPosition
        get() = TabletPosition.LEFT

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int = typeFactory.type(this)

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return newData is OngoingCampaignUiModel &&
            shouldShowCampaign == newData.shouldShowCampaign &&
            shouldShowTradeIn == newData.shouldShowTradeIn &&
            data?.hashCode() == newData.data?.hashCode()
    }

    override fun newInstance(): DynamicPdpDataModel = this.copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        if (newData !is OngoingCampaignUiModel) return null
        val showTradeInChanged = shouldShowTradeIn != newData.shouldShowTradeIn

        return if (showTradeInChanged) {
            Bundle().apply {
                putInt(ProductDetailConstant.DIFFUTIL_PAYLOAD, PAYLOAD_WISHLIST)
            }
        } else {
            null
        }
    }

    companion object {
        const val PAYLOAD_WISHLIST = 1
    }
}
