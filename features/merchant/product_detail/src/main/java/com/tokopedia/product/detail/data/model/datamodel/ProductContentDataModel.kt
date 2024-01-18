package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.analytics.performance.perf.performanceTracing.components.BlocksLoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.components.LoadableComponent
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.common.data.model.pdplayout.Price
import com.tokopedia.product.detail.common.data.model.pdplayout.ThematicCampaign
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.CampaignRibbon

/**
 * Created by Yehezkiel on 06/05/20
 */
data class ProductContentDataModel(
    val type: String = "",
    val name: String = "",
    var data: ProductContentMainData? = null,
    var isWishlisted: Boolean = false,
    var freeOngkirImgUrl: String = "",

    // Ribbon Data
    var isNpl: Boolean = false,
    var shouldShowShareWidget: Boolean = false
) : DynamicPdpDataModel,
    LoadableComponent by BlocksLoadableComponent(
        { false },
        "ProductContentDataModel"
    ) {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductContentDataModel) {
            data?.hashCode() == newData.data?.hashCode() &&
                isNpl == newData.isNpl &&
                isWishlisted == newData.isWishlisted &&
                freeOngkirImgUrl == newData.freeOngkirImgUrl &&
                data?.thematicCampaign?.campaignName == newData.data?.thematicCampaign?.campaignName
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        return if (newData is ProductContentDataModel) {
            if (data?.hashCode() != newData.data?.hashCode() || isNpl != newData.isNpl) {
                // Update the whole component
                return null
            }

            val bundle = Bundle()
            if (freeOngkirImgUrl != newData.freeOngkirImgUrl ||
                shouldShowShareWidget != newData.shouldShowShareWidget
            ) {
                bundle.putInt(ProductDetailConstant.DIFFUTIL_PAYLOAD, PAYLOAD_BOE_SHARE)
                return bundle
            }

            if (isWishlisted != newData.isWishlisted) {
                bundle.putInt(ProductDetailConstant.DIFFUTIL_PAYLOAD, PAYLOAD_WISHLIST)
                return bundle
            }
            null
        } else {
            null
        }
    }

    override fun isLoading(): Boolean {
        return data == null
    }

    companion object {
        const val PAYLOAD_WISHLIST = 1
        const val PAYLOAD_BOE_SHARE = 421321
    }
}

/**
 * Render Priority
 * https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2465759286/PDP+Campaign+Component#3.-Render-Priority
 */
data class ProductContentMainData(
    var campaign: CampaignModular = CampaignModular(),
    var thematicCampaign: ThematicCampaign = ThematicCampaign(),
    var cashbackPercentage: Int = 0,
    var price: Price = Price(),
    var stockWording: String = "",
    var isVariant: Boolean = false,
    var productName: String = "",
    var isProductActive: Boolean = false
) {

    val hasCampaign
        get() = campaign.campaignIdentifier != CampaignRibbon.NO_CAMPAIGN

    val hasThematicCampaign
        get() = hasCampaign && campaign.campaignIdentifier == CampaignRibbon.THEMATIC_CAMPAIGN

    val hasOngoingCampaign
        get() = !hasThematicCampaign
}
