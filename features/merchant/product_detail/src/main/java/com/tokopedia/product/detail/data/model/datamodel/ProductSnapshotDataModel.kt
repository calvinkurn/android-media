package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.constant.ProductUpcomingTypeDef
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductSnapshotDataModel(
        var type: String = "",
        val name: String = "",

        var data: DynamicProductInfoP1? = null,

        var isAllowManage: Int = 0,
        var isWishlisted: Boolean = false,
        var shouldShowCod: Boolean = false,
        var shouldShowTradein: Boolean = false,

        //Upcoming section
        var upcomingNplData : UpcomingNplDataModel = UpcomingNplDataModel()

) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name
    override fun type(): String = type

    fun isUpcomingNplType() : Boolean  {
        return upcomingNplData.upcomingType.isNotEmpty() && upcomingNplData.upcomingType.equals(ProductUpcomingTypeDef.UPCOMING_NPL, true)
    }
    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int = typeFactory.type(this)

    fun showTradeIn(): Boolean {
        return shouldShowTradein && data?.data?.campaign?.shouldShowRibbonCampaign == false && !isUpcomingNplType()
    }

    fun showCod(): Boolean {
        return shouldShowCod && !shouldShowTradein && data?.data?.campaign?.shouldShowRibbonCampaign == false && !isUpcomingNplType()
    }
}