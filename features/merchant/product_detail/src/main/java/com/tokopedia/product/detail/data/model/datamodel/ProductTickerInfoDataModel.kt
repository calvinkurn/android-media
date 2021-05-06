package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
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
        var isOutOfStock: Boolean = false,
        var isUpcomingType:Boolean = false

) : DynamicPdpDataModel {

    override fun type(): String = type
    override fun name(): String = name

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override val impressHolder: ImpressHolder = ImpressHolder()

    fun shouldRemoveComponent(): Boolean {
        return (statusInfo == null || (statusInfo?.shopStatus == ProductShopStatusTypeDef.OPEN && statusInfo?.isIdle != true)) &&
                !isOutOfStock && !isProductWarehouse
    }

    fun isOos(): Boolean {
        return isOutOfStock && !isProductInCampaign && isProductWarehouse
    }

    fun isProductInactive(): Boolean {
        return isProductWarehouse && !isOutOfStock
    }

    fun getComponentTrackData(adapterPosition: Int) = ComponentTrackDataModel(type, name, adapterPosition + 1)

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductTickerInfoDataModel) {
            isProductWarehouse == newData.isProductWarehouse
                    && isOutOfStock == newData.isOutOfStock
                    && isProductInCampaign == newData.isProductInCampaign
                    && isStatusInfoTheSame(newData.statusInfo)
                    && isClosedInfoTheSame(newData.closedInfo)
                    && generalTickerInfoDataModel?.size == newData.generalTickerInfoDataModel?.size
                    && isUpcomingType == newData.isUpcomingType
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        return null
    }

    private fun isStatusInfoTheSame(newStatusInfo: ShopInfo.StatusInfo?): Boolean {
        if (statusInfo == null && newStatusInfo == null) return true
        return statusInfo?.isIdle == newStatusInfo?.isIdle
                && statusInfo?.shopStatus == newStatusInfo?.shopStatus
                && statusInfo?.statusMessage == newStatusInfo?.statusMessage
                && statusInfo?.statusTitle == newStatusInfo?.statusTitle
    }

    private fun isClosedInfoTheSame(newClosedInfo: ShopInfo.ClosedInfo?): Boolean {
        if (closedInfo == null && newClosedInfo == null) return true
        return closedInfo?.closeDetail?.openDateUnix == newClosedInfo?.closeDetail?.openDateUnix
    }
}