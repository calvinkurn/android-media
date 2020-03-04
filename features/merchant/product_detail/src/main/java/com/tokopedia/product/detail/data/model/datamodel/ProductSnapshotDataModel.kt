package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductSnapshotDataModel(
        var type: String = "",
        val name: String = "",
        var dataLayout: List<ComponentData>? = null,

        var isAllowManage: Int = 0,
        var isWishlisted: Boolean = false,

        var media: List<ProductMediaDataModel>? = null,
        var dynamicProductInfoP1: DynamicProductInfoP1? = null,
        var nearestWarehouse: MultiOriginWarehouse? = null,
        var shouldShowCod: Boolean = false,
        var shouldShowTradein: Boolean = false,
        var shouldReinitVideoPicture: Boolean = true,

        var statusTitle: String = "",
        var statusMessage: String = "",
        var shopStatus: Int = SHOP_STATUS_ACTIVE
) : DynamicPdpDataModel {
    override fun name(): String = name
    override fun type(): String = type

    companion object {
        const val SHOP_STATUS_ACTIVE = 1
        val LAYOUT = R.layout.item_dynamic_pdp_snapshot
    }

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int = typeFactory.type(this)
}