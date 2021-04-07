package com.tokopedia.cart.view.uimodel

import android.os.Parcelable
import com.tokopedia.cart.domain.model.cartlist.ShopGroupAvailableData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartShopHolderData(
        var shopGroupAvailableData: ShopGroupAvailableData? = null,
        var isAllSelected: Boolean = false,
        var isPartialSelected: Boolean = false

) : Parcelable {

    fun setAllItemSelected(allSelected: Boolean) {
        this.isAllSelected = allSelected
        shopGroupAvailableData?.isChecked = allSelected
    }

}