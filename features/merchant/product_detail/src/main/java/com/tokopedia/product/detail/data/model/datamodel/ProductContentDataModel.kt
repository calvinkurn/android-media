package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.constant.ProductUpcomingTypeDef
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * Created by Yehezkiel on 06/05/20
 */
data class ProductContentDataModel(
        val type: String = "",
        val name: String = "",
        var data: DynamicProductInfoP1? = null,
        var isWishlisted: Boolean = false,

        //Ribbon Data
        var shouldShowCod: Boolean = false,
        var shouldShowTradein: Boolean = false,
        var isAllowManage: Int = 0,

        //Upcoming Data
        var upcomingNplData: UpcomingNplDataModel = UpcomingNplDataModel()
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    fun isUpcomingNplType() : Boolean  {
        return upcomingNplData.upcomingType.isNotEmpty() && upcomingNplData.upcomingType.equals(ProductUpcomingTypeDef.UPCOMING_NPL, true)
    }

    fun showTradeIn(): Boolean {
        return shouldShowTradein && data?.data?.campaign?.shouldShowRibbonCampaign == false && !isUpcomingNplType()
    }

    fun showCod(): Boolean {
        return shouldShowCod && !shouldShowTradein && data?.data?.campaign?.shouldShowRibbonCampaign == false && !isUpcomingNplType()
    }

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}