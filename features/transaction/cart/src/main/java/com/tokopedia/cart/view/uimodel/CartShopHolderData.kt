package com.tokopedia.cart.view.uimodel

import android.os.Parcelable
import com.tokopedia.cart.domain.model.cartlist.ShopGroupAvailableData
import kotlinx.android.parcel.Parcelize

@Parcelize
class CartShopHolderData : Parcelable {
    var shopGroupAvailableData: ShopGroupAvailableData? = null
    var isAllSelected: Boolean = false
        private set
    var isPartialSelected: Boolean = false

    fun setAllItemSelected(allSelected: Boolean) {
        this.isAllSelected = allSelected
        shopGroupAvailableData?.isChecked = allSelected
    }
}