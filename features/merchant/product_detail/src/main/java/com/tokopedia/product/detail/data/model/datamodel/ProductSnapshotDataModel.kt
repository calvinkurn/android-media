package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductSnapshotDataModel(
        var type: String = "",
        val name: String = "",
        var dataLayout: List<ComponentData>? = null,

        var isAllowManage: Int = 0,
        var isWishlisted: Boolean = false,

        var media: List<ProductMediaDataModel>? = null,
        var dynamicProductInfoP1: DynamicProductInfoP1? = null,
        var shouldShowCod: Boolean = false,
        var shouldShowTradein: Boolean = false,

        //MultiOrigin
        var nearestWarehouseDataModel: NearestWarehouseDataModel? = null,

        var shouldRefreshViewPager: Boolean = true,
        var shouldRenderImageVariant: Boolean = true,
        var statusTitle: String = "",
        var statusMessage: String = "",
        var shopStatus: Int = SHOP_STATUS_ACTIVE
) : DynamicPdpDataModel {

    data class NearestWarehouseDataModel(
            var nearestWarehouseId: String = "",
            var nearestWarehousePrice: Int = 0,
            var nearestWarehouseStockWording: String = ""
    )

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name
    override fun type(): String = type

    companion object {
        const val SHOP_STATUS_ACTIVE = 1
        val LAYOUT = R.layout.item_dynamic_pdp_snapshot
    }

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int = typeFactory.type(this)

    fun showTradeIn(): Boolean {
        return shouldShowTradein && dynamicProductInfoP1?.data?.campaign?.activeAndHasId == false
    }

    fun showCod(): Boolean {
        return shouldShowCod && !shouldShowTradein && dynamicProductInfoP1?.data?.campaign?.activeAndHasId == false
    }

    fun getCampaignModular(): CampaignModular = dynamicProductInfoP1?.data?.campaign
            ?: CampaignModular()

    fun getNearestWarehouse(): NearestWarehouseDataModel = nearestWarehouseDataModel
            ?: NearestWarehouseDataModel()
}