package com.tokopedia.tokopoints.view.merchantcoupon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.merchantcoupon.ProductCategoriesFilterItem

class MerchantCouponFilterAdapter(private var clickListener: OnFilterTypeClickListener) : RecyclerView.Adapter<MerchantCouponFilterViewholder>(), MerchantCouponFilterViewholder.FilterCallback {


    interface OnFilterTypeClickListener {
        fun onFilterTypeSelected(adapterPosition: Int, productCategoriesFilterItem: ProductCategoriesFilterItem)
        fun onFilterTypeDeselected(adapterPosition: Int, productCategoriesFilterItem: ProductCategoriesFilterItem): Boolean
    }


    private var items: List<ProductCategoriesFilterItem> = listOf()
    private var maxSelectedItems = 0
    private var selectedItems: ArrayList<MerchantCouponFilterViewholder.ViewHolderState> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MerchantCouponFilterViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tp_layout_filter_item, parent, false)
        return MerchantCouponFilterViewholder(view, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MerchantCouponFilterViewholder, position: Int) {
        holder.bind(items[position], selectedItems[position])
    }

    override fun filterClickEnableListener(position: Int) {
        if (position < 0 || position > selectedItems.lastIndex) return
        // from normal to selected
        selectedItems[position] = MerchantCouponFilterViewholder.ViewHolderState.SELECTED
        // disable unselected items when maximum selected items reached
        manageUnSelectedItems(getSelectedCount())
        // execute the callback function
        clickListener.onFilterTypeSelected(position, items[position])
    }

    override fun filterClickDisableListener(position: Int): Boolean {
        if (position < 0 || position > selectedItems.lastIndex) return false
        // execute the callback function
        val isConfirmed = clickListener.onFilterTypeDeselected(position, items[position])
        // from selected to normal if confirmed
        if (isConfirmed) selectedItems[position] = MerchantCouponFilterViewholder.ViewHolderState.NORMAL
        // disable unselected items when maximum selected items reached
        manageSelectedItems(getSelectedCount())
        return isConfirmed
    }

    private fun manageSelectedItems(selectedCount: Int) {
        disableselectedItems()
    }

    private fun manageUnSelectedItems(selectedCount: Int) {
        enableSelectedItem()
    }

    private fun getSelectedCount(): Int {
        return selectedItems.count { it == MerchantCouponFilterViewholder.ViewHolderState.SELECTED }
    }

    private fun disableUnselectedItems() {
        selectedItems.forEachIndexed { index, viewHolderState ->
            if (viewHolderState == MerchantCouponFilterViewholder.ViewHolderState.NORMAL) {
                selectedItems[index] = MerchantCouponFilterViewholder.ViewHolderState.DISABLED
            }
        }
        notifyDataSetChanged()
    }

    private fun disableselectedItems() {
        selectedItems.forEachIndexed { index, viewHolderState ->
            if (viewHolderState == MerchantCouponFilterViewholder.ViewHolderState.SELECTED) {
                selectedItems[index] = MerchantCouponFilterViewholder.ViewHolderState.NORMAL
            }
        }
        notifyDataSetChanged()
    }

    private fun enableSelectedItem() {
        selectedItems.forEachIndexed { index, viewHolderState ->
            if (viewHolderState == MerchantCouponFilterViewholder.ViewHolderState.NORMAL) {
                selectedItems[index] = MerchantCouponFilterViewholder.ViewHolderState.SELECTED
            }
        }
        notifyDataSetChanged()
    }

    fun setData(items: List<ProductCategoriesFilterItem>) {
        this.items = items
        selectedItems = ArrayList(items.map { MerchantCouponFilterViewholder.ViewHolderState.NORMAL })
        notifyDataSetChanged()
    }
}