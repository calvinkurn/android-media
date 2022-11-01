package com.tokopedia.productbundlewidget.model

import com.tokopedia.product_bundle.common.data.model.request.Bundle

class GetBundleParamBuilder {
    private var productId: String = ""
    private var warehouseId: String = ""
    private var shopId: String = ""
    private var pageSource: String = ""
    private var bundleIds: MutableList<String> = mutableListOf()
    private var widgetType: WidgetType = WidgetType.TYPE_1

    fun setProductId(productId: String) = apply {
        this.productId = productId
    }

    fun setWarehouseId(warehouseId: String) = apply {
        this.warehouseId = warehouseId
    }

    fun setShopId(shopId: String) = apply {
        this.shopId = shopId
    }

    fun setWidgetType(widgetType: WidgetType) = apply {
        this.widgetType = widgetType
    }

    fun setWidgetType(widgetType: Int) = apply {
        this.widgetType = WidgetType.values().firstOrNull {
            it.typeCode == widgetType
        } ?: return@apply
    }

    fun setBundleId(bundleIds: List<String>) = apply {
        this.bundleIds = bundleIds.toMutableList()
    }

    fun addBundleId(bundleIds: List<String>) = apply {
        this.bundleIds.addAll(bundleIds)
    }

    fun setPageSource(source: String) = apply {
        pageSource = source
    }

    fun build(): GetBundleParam {
        val bundleList = bundleIds.map {
            Bundle(ID = it, WarehouseID = warehouseId)
        }
        return GetBundleParam(
            productId = if (bundleList.isEmpty()) productId else "",
            warehouseId = if (bundleList.isEmpty()) warehouseId else "",
            shopId = if (bundleList.isEmpty()) shopId else "",
            bundleList = bundleList,
            widgetType = widgetType,
            pageSource = pageSource
        )
    }
}
