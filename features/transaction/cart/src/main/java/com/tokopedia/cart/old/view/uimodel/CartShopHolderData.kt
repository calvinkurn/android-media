package com.tokopedia.cart.old.view.uimodel

import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.old.domain.model.cartlist.ShopGroupAvailableData
import kotlinx.parcelize.Parcelize

@Parcelize
class CartShopHolderData : Parcelable {
    var shopGroupAvailableData: ShopGroupAvailableData? = null
    var isAllSelected: Boolean = false
        private set
    var isPartialSelected: Boolean = false
    var isCollapsible: Boolean = false
    var isCollapsed: Boolean = false
    var clickedCollapsedProductIndex: Int = RecyclerView.NO_POSITION
    var isNeedToRefreshWeight: Boolean = false

    fun setAllItemSelected(allSelected: Boolean) {
        this.isAllSelected = allSelected
        shopGroupAvailableData?.isChecked = allSelected
    }
}