package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantTypeViewHolder
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantTypeViewHolder.ViewHolderState

class VariantTypeAdapter(private val clickListener: OnVariantTypeClickListener)
    : RecyclerView.Adapter<VariantTypeViewHolder>(), VariantTypeViewHolder.OnVariantTypeViewHolderClickListener {

    interface OnVariantTypeClickListener {
        fun onVariantTypeSelected(adapterPosition: Int, variantDetail: VariantDetail)
        fun onVariantTypeDeselected(adapterPosition: Int, variantDetail: VariantDetail): Boolean
        fun onVariantTypeChanged(selectedCount: Int)
    }

    private var items: MutableList<VariantDetail> = mutableListOf()
    private var maxSelectedItems = 0
    private var selectedItems: ArrayList<ViewHolderState> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantTypeViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_variant_type, parent, false)
        return VariantTypeViewHolder(rootView, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VariantTypeViewHolder, position: Int) {
        holder.bindData(items[position], selectedItems[position])
    }

    override fun onVariantTypeSelected(position: Int) {
        if (position < 0 || position > selectedItems.lastIndex) return
        // from normal to selected
        selectedItems[position] = ViewHolderState.SELECTED
        // disable unselected items when maximum selected items reached
        val selectedCount = getSelectedCount()
        manageUnselectedItems(selectedCount)
        // execute the callback function
        clickListener.onVariantTypeSelected(position, items[position])
        clickListener.onVariantTypeChanged(selectedCount)
    }

    override fun onVariantTypeDeselected(position: Int): Boolean {
        if (position < 0 || position > selectedItems.lastIndex) return false
        // execute the callback function
        val isConfirmed = clickListener.onVariantTypeDeselected(position, items[position])
        // from selected to normal if confirmed
        if (isConfirmed) selectedItems[position] = ViewHolderState.NORMAL
        // disable unselected items when maximum selected items reached
        val selectedCount = getSelectedCount()
        manageUnselectedItems(selectedCount)
        clickListener.onVariantTypeChanged(selectedCount)
        return isConfirmed
    }

    fun addData(variantDetail: VariantDetail) {
        items.add(variantDetail)
        selectedItems.add(ViewHolderState.SELECTED)
        notifyDataSetChanged()
    }

    fun setData(items: List<VariantDetail>) {
        this.items = items.toMutableList()
        selectedItems = ArrayList(items.map { ViewHolderState.NORMAL })
        notifyDataSetChanged()
    }

    fun setMaxSelectedItems(max: Int) {
        maxSelectedItems = max
    }

    fun getItem(position: Int): VariantDetail {
        return items[position]
    }

    fun deselectItem(adapterPosition: Int) {
        selectedItems[adapterPosition] = ViewHolderState.NORMAL
        manageUnselectedItems(getSelectedCount())
    }

    fun getSelectedItems(): List<VariantDetail> {
        return items.filterIndexed { index, _ ->
            selectedItems.getOrNull(index) == ViewHolderState.SELECTED
        }
    }

    fun setSelectedItems(selectedVariantDetails: List<VariantDetail>) {
        items.forEachIndexed { position, variantDetail ->
            val isVariantIdExist = selectedVariantDetails.any {
                it.variantID == variantDetail.variantID
            }
            if (isVariantIdExist) {
                items.getOrNull(position)?.let {
                    selectedItems[position] = ViewHolderState.SELECTED
                }
            }
        }
        manageUnselectedItems(getSelectedCount())
    }

    private fun manageUnselectedItems(selectedCount: Int) {
        if (selectedCount >= maxSelectedItems) disableUnselectedItems()
        else enableUnselectedItems()
    }

    private fun getSelectedCount(): Int {
        return selectedItems.count { it == ViewHolderState.SELECTED }
    }

    private fun disableUnselectedItems() {
        selectedItems.forEachIndexed { index, viewHolderState ->
            if (viewHolderState == ViewHolderState.NORMAL) {
                selectedItems[index] = ViewHolderState.DISABLED
            }
        }
        notifyDataSetChanged()
    }

    private fun enableUnselectedItems() {
        selectedItems.forEachIndexed { index, viewHolderState ->
            if (viewHolderState == ViewHolderState.DISABLED) {
                selectedItems[index] = ViewHolderState.NORMAL
            }
        }
        notifyDataSetChanged()
    }
}