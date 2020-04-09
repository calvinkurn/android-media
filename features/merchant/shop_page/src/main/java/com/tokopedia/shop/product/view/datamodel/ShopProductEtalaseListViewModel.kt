package com.tokopedia.shop.product.view.datamodel


import android.os.Parcelable
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory

data class ShopProductEtalaseListViewModel(
        val etalaseModelList: List<BaseShopProductEtalaseViewModel> = listOf(),
        var selectedEtalaseId: String = "",
        var selectedEtalaseName: String = "",
        var selectedEtalaseBadge: String = ""
) : BaseShopProductViewModel {
    var isButtonEtalaseMoreShown: Boolean = false
    var recyclerViewState: Parcelable? = null

    override fun type(typeFactory: ShopProductAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
