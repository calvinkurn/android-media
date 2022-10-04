package com.tokopedia.productbundlewidget.model

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.tokopedia.product_bundle.common.data.model.request.Bundle

class GetBundleParamBuilder(fragment: Fragment) {
    private val storeOwner: ViewModelStoreOwner = fragment
    private val lifecycleOwner: LifecycleOwner = fragment.viewLifecycleOwner
    private var productId: String = ""
    private var warehouseId: String = ""
    private var shopId: String = ""
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

    fun setBundleId(bundleIds: List<String>) = apply {
        this.bundleIds = bundleIds.toMutableList()
    }

    fun addBundleId(bundleIds: List<String>) = apply {
        this.bundleIds.addAll(bundleIds)
    }

    fun build(): GetBundleParam {
        val bundleList = bundleIds.map {
            Bundle(ID = it, WarehouseID = warehouseId)
        }
        return GetBundleParam(
            storeOwner = storeOwner,
            lifecycleOwner = lifecycleOwner,
            productId = if (bundleList.isEmpty()) productId else "",
            warehouseId = if (bundleList.isEmpty()) warehouseId else "",
            shopId = if (bundleList.isEmpty()) shopId else "",
            bundleList = bundleList,
            widgetType = widgetType
        )
    }
}
