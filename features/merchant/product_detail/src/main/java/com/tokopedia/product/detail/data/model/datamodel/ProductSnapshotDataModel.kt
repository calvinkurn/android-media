package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.common.data.model.product.Media
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

data class ProductSnapshotDataModel(
        var dataLayout: List<ComponentData> = listOf(),
        var media: List<Media> = listOf(),
        var productInfoP1: ProductInfo = ProductInfo(),
        var shopInfo: ShopInfo = ShopInfo(),
        var nearestWarehouse: MultiOriginWarehouse? = null,
        var type: String = "",
        var shouldShowCod: Boolean = false
) : DynamicPDPDataModel {

    override fun type(): String = type

    companion object {
        val LAYOUT = R.layout.item_dynamic_pdp_snapshot
    }

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int = typeFactory.type(this)
}