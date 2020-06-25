package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.product.detail.view.util.ProductDetailUtil

/**
 * Created by Yehezkiel on 06/05/20
 */
data class ProductContentDataModel(
        val type: String = "",
        val name: String = "",
        var data: DynamicProductInfoP1? = null,
        var nearestWarehouseDataModel: ProductSnapshotDataModel.NearestWarehouseDataModel? = null,
        var isWishlisted: Boolean = false,

        //Ribbon Data
        var shouldShowCod: Boolean = false,
        var shouldShowTradein: Boolean = false,
        var isAllowManage: Int = 0
) : DynamicPdpDataModel {
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    fun showTradeIn(): Boolean {
        return shouldShowTradein && data?.data?.campaign?.shouldShowRibbonCampaign == false
    }

    fun showCod(): Boolean {
        return shouldShowCod && !shouldShowTradein && data?.data?.campaign?.shouldShowRibbonCampaign == false
    }

    fun getNearestWarehouse(): ProductSnapshotDataModel.NearestWarehouseDataModel = nearestWarehouseDataModel
            ?: ProductSnapshotDataModel.NearestWarehouseDataModel()

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}