package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.constant.ProductShopStatusTypeDef
import com.tokopedia.product.detail.data.model.ticker.GeneralTickerDataModel
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

/**
 * Created by Yehezkiel on 08/06/20
 */
data class ProductTickerInfoDataModel(
        val type: String = "",
        val name: String = "",
        var generalTickerInfoDataModel: List<GeneralTickerDataModel.TickerDetailDataModel>? = null,
        var statusInfo: ShopInfo.StatusInfo? = null,
        var closedInfo: ShopInfo.ClosedInfo? = ShopInfo.ClosedInfo(),
        var isProductWarehouse: Boolean = false,
        var isProductInCampaign: Boolean = false,
        var isOutOfStock: Boolean = false

) : DynamicPdpDataModel {

    override fun type(): String = type
    override fun name(): String = name

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override val impressHolder: ImpressHolder = ImpressHolder()

    fun shouldRemoveComponent(): Boolean {
        return generalTickerInfoDataModel?.isNullOrEmpty() == true &&
        return (statusInfo == null || (statusInfo?.shopStatus == ProductShopStatusTypeDef.OPEN && statusInfo?.isIdle != true)) &&
                !isOutOfStock && !isProductWarehouse
    }

    fun isOos(): Boolean {
        return isOutOfStock && !isProductInCampaign
    }

    fun isProductInactive(): Boolean {
        return isProductWarehouse && !isOutOfStock
    }

    fun getComponentTrackData(adapterPosition: Int) = ComponentTrackDataModel(type, name, adapterPosition + 1)
}