package com.tokopedia.cart.view.uimodel

import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.domain.model.cartlist.ShopGroupAvailableData
import kotlinx.android.parcel.Parcelize

@Parcelize
class CartShopHolderData : Parcelable {
    var shopGroupAvailableData: ShopGroupAvailableData? = null
    var isAllSelected: Boolean = false
        private set
    var isPartialSelected: Boolean = false
    var isCollapsed: Boolean = false
    var isCollapsible: Boolean = false
    var showMoreWording: String = ""
    var showLessWording: String = ""
    var clickedCollapsedProductIndex: Int = RecyclerView.NO_POSITION

    fun setAllItemSelected(allSelected: Boolean) {
        this.isAllSelected = allSelected
        shopGroupAvailableData?.isChecked = allSelected
    }
}