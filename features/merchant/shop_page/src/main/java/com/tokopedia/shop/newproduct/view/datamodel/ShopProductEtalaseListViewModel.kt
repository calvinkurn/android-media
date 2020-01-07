package com.tokopedia.shop.newproduct.view.datamodel


import android.os.Parcelable
import com.tokopedia.shop.newproduct.view.adapter.ShopProductAdapterTypeFactory
import java.util.ArrayList

data class ShopProductEtalaseListViewModel(
        val etalaseModelList: List<BaseShopProductEtalaseViewModel> = listOf(),
        var selectedEtalaseId: String = "",
        var selectedEtalaseName: String = "",
        var selectedEtalaseBadge: String = "",
        val defaultSelectedEtalaseId : String = "",
        var isButtonEtalaseMoreShown: Boolean = false,
        var recyclerViewState: Parcelable? = null
) : BaseShopProductViewModel {
    override fun type(typeFactory: ShopProductAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
