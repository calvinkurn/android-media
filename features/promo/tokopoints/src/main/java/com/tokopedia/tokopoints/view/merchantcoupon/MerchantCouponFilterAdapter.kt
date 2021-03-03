package com.tokopedia.tokopoints.view.merchantcoupon

import android.view.LayoutInflater
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
        selectedItems[position] = MerchantCouponFilterViewholder.ViewHolderState.SELECTED
        manageUnSelectedItems(position)
        clickListener.onFilterTypeSelected(itemCount-1, items[position])
    }

    override fun filterClickDisableListener(position: Int): Boolean {
        if (position < 0 || position > selectedItems.lastIndex) return false
        val isConfirmed = clickListener.onFilterTypeDeselected(position, items[position])
        if (isConfirmed) selectedItems[position] = MerchantCouponFilterViewholder.ViewHolderState.NORMAL
        manageSelectedItems(position)
        return isConfirmed
    }

    private fun manageSelectedItems(position: Int) {
        selectedItems[position] = MerchantCouponFilterViewholder.ViewHolderState.NORMAL
        notifyDataSetChanged()
    }

    private fun manageUnSelectedItems(position: Int) {
        selectedItems.forEachIndexed { index, viewHolderState ->
            if (viewHolderState == MerchantCouponFilterViewholder.ViewHolderState.SELECTED) {
                selectedItems[index] = MerchantCouponFilterViewholder.ViewHolderState.NORMAL
            }
        }
        selectedItems[position] = MerchantCouponFilterViewholder.ViewHolderState.SELECTED
        notifyDataSetChanged()
    }

    fun setData(items: List<ProductCategoriesFilterItem>) {
        this.items = items
        selectedItems = ArrayList(items.map { MerchantCouponFilterViewholder.ViewHolderState.NORMAL })
        notifyDataSetChanged()
    }
}