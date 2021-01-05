package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.constant.ProductUpcomingTypeDef
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.common.data.model.pdplayout.IsFreeOngkir
import com.tokopedia.product.detail.common.data.model.pdplayout.Price
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * Created by Yehezkiel on 06/05/20
 */
data class ProductContentDataModel(
        val type: String = "",
        val name: String = "",
        var data: ProductContentMainData? = null,
        var isWishlisted: Boolean = false,

        //Ribbon Data
        var shouldShowTradein: Boolean = false,

        //Upcoming Data
        var upcomingNplData: UpcomingNplDataModel = UpcomingNplDataModel(),
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    fun isUpcomingNplType(): Boolean {
        return upcomingNplData.upcomingType.isNotEmpty() && upcomingNplData.upcomingType.equals(ProductUpcomingTypeDef.UPCOMING_NPL, true)
    }

    fun showTradeIn(): Boolean {
        return shouldShowTradein && data?.campaign?.shouldShowRibbonCampaign == false && !isUpcomingNplType()
    }

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductContentDataModel) {
            areMainContentTheSame(newData.data)
                    && shouldShowTradein == newData.shouldShowTradein
                    && areUpcomingNplTheSame(newData.upcomingNplData)
                    && isWishlisted == newData.isWishlisted
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        val bundle = Bundle()
        return if (newData is ProductContentDataModel) {
            if (!areMainContentTheSame(newData.data) && shouldShowTradein == newData.shouldShowTradein && areUpcomingNplTheSame(newData.upcomingNplData)) {
                //Update the whole component
                return null
            }

            if (isWishlisted != newData.isWishlisted) {
                bundle.putInt(ProductDetailConstant.DIFFUTIL_PAYLOAD, ProductDetailConstant.PAYLOAD_WISHLIST)
                return bundle
            }
            null
        } else {
            null
        }
    }

    private fun areMainContentTheSame(newData: ProductContentMainData?): Boolean {
        return data?.productName == newData?.productName
                && data?.price == newData?.price
                && data?.isProductActive == newData?.isProductActive
                && data?.stockWording == newData?.stockWording
                && areMainCampaignTheSame(newData?.campaign)
    }

    private fun areMainCampaignTheSame(newCampaign: CampaignModular?): Boolean {
        val currentCampaign = data?.campaign
        return currentCampaign.hashCode() == newCampaign.hashCode()
    }

    private fun areUpcomingNplTheSame(newUpcomingNplData: UpcomingNplDataModel): Boolean {
        return upcomingNplData.upcomingType == newUpcomingNplData.upcomingType
                && upcomingNplData.ribbonCopy == newUpcomingNplData.ribbonCopy
                && upcomingNplData.startDate == newUpcomingNplData.startDate
    }
}

data class ProductContentMainData(
        var campaign: CampaignModular = CampaignModular(),
        var freeOngkir: IsFreeOngkir = IsFreeOngkir(),
        var cashbackPercentage: Int = 0,
        var price: Price = Price(),
        var stockWording: String = "",
        var isVariant: Boolean = false,
        var productName: String = "",
        var isProductActive: Boolean = false
)