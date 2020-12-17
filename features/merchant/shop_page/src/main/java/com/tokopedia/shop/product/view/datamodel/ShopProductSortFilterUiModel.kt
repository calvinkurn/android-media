package com.tokopedia.shop.product.view.datamodel


import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory

data class ShopProductSortFilterUiModel(
        var selectedEtalaseId: String = "",
        var selectedEtalaseName: String = "",
        var selectedEtalaseBadge: String = "",
        var selectedSortId: String = "",
        var selectedSortName: String = "",
        var isShowSortFilter: Boolean = true,
        var filterIndicatorCounter: Int = 0
) : Visitable<BaseAdapterTypeFactory> {
    var scrollX: Int = 0

    override fun type(typeFactory: BaseAdapterTypeFactory): Int {
        return when (typeFactory) {
            is ShopProductAdapterTypeFactory ->
                typeFactory.type(this)
            is ShopHomeAdapterTypeFactory ->
                typeFactory.type(this)
            else ->
                -1
        }
    }
}
