package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.swap
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantTypeViewHolder
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantTypeViewHolder.ViewHolderState

class VariantTypeAdapter(private val clickListener: OnVariantTypeClickListener)
    : RecyclerView.Adapter<VariantTypeViewHolder>(), VariantTypeViewHolder.OnVariantTypeViewHolderClickListener {

    interface OnVariantTypeClickListener {
        fun onVariantTypeSelected(adapterPosition: Int, variantDetail: VariantDetail)
        fun onVariantTypeDeselected(adapterPosition: Int, variantDetail: VariantDetail): Boolean
        fun onCustomVariantTypeCountChanged(count: Int)
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
        manageUnselectedItems(getSelectedCount())
        // execute the callback function
        clickListener.onVariantTypeSelected(position, items[position])
    }

    override fun onVariantTypeDeselected(position: Int): Boolean {
        if (position < 0 || position > selectedItems.lastIndex) return false
        // execute the callback function
        val isConfirmed = clickListener.onVariantTypeDeselected(position, items[position])
        // from selected to normal if confirmed
        if (isConfirmed) selectedItems[position] = ViewHolderState.NORMAL
        // disable unselected items when maximum selected items reached
        manageUnselectedItems(getSelectedCount())
        return isConfirmed
    }

    fun addData(variantDetail: VariantDetail, isSelected: Boolean = true) {
        items.add(variantDetail)
        if (isSelected) {
            selectedItems.add(ViewHolderState.SELECTED)
            onVariantTypeSelected(selectedItems.lastIndex) // set as selected, trigger onVariantTypeSelected
        } else {
            selectedItems.add(ViewHolderState.NORMAL)
            manageUnselectedItems(getSelectedCount())
        }
        clickListener.onCustomVariantTypeCountChanged(getCustomVariantCount())
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

    fun getItem(position: Int): VariantDetail? {
        return items.getOrNull(position)
    }

    fun getItems(): List<VariantDetail> = items

    fun deselectItem(adapterPosition: Int) {
        selectedItems[adapterPosition] = ViewHolderState.NORMAL
        manageUnselectedItems(getSelectedCount())
    }

    fun getSelectedItems(): List<VariantDetail> {
        return items.filterIndexed { index, _ ->
            selectedItems.getOrNull(index) == ViewHolderState.SELECTED
        }
    }

    fun getCustomVariantTypeItems(): List<VariantDetail> {
        return items.filter { it.isCustom }
    }

    fun getSelectedAdapterPosition(): List<Int> {
        val result: MutableList<Int> = mutableListOf()
        selectedItems.forEachIndexed { index, viewHolderState ->
            if (viewHolderState == ViewHolderState.SELECTED) {
                result.add(index)
            }
        }
        return result
    }

    fun isItemAtPositionSelected(position: Int) =
            selectedItems.getOrNull(position) == ViewHolderState.SELECTED

    fun setSelectedItems(selectedVariantDetails: List<VariantDetail>) {
        // select predefined variant
        items.forEachIndexed { position, variantDetail ->
            val isVariantIdExist = selectedVariantDetails.any {
                it.variantID == variantDetail.variantID && it.name == variantDetail.name
            }
            if (isVariantIdExist) {
                selectedItems.getOrNull(position)?.let {
                    selectedItems[position] = ViewHolderState.SELECTED
                }
            }
        }
        // add custom variant
        selectedVariantDetails.forEach { variantDetail ->
            val isVariantIdExist = items.any { it.name == variantDetail.name }
            if (!isVariantIdExist) {
                addData(variantDetail.apply { isCustom = true })
            }
        }
        // enable/ disable selection if reached max selection
        manageUnselectedItems(getSelectedCount())
    }

    fun swapSelectedItem() {
        val firstIndex = selectedItems.indexOfFirst { it == ViewHolderState.SELECTED }
        val lastIndex = selectedItems.indexOfLast { it == ViewHolderState.SELECTED }

        items.swap(firstIndex, lastIndex)
        selectedItems.swap(firstIndex, lastIndex)
        notifyItemChanged(firstIndex)
        notifyItemChanged(lastIndex)
    }

    fun replaceItem(index: Int, variantDetail: VariantDetail) {
        items[index] = variantDetail
        notifyItemChanged(index)
    }

    fun deleteItem(index: Int, variantDetail: VariantDetail) {
        if (variantDetail.isCustom) {
            items.removeAt(index)
            selectedItems.removeAt(index)
            manageUnselectedItems(getSelectedCount())
            clickListener.onCustomVariantTypeCountChanged(getCustomVariantCount())
        } else {
            deselectItem(index)
        }
        notifyDataSetChanged()
    }

    fun getSelectedCount(): Int {
        return selectedItems.count { it == ViewHolderState.SELECTED }
    }

    private fun manageUnselectedItems(selectedCount: Int) {
        if (selectedCount >= maxSelectedItems) disableUnselectedItems()
        else enableUnselectedItems()
    }

    private fun getCustomVariantCount(): Int {
        return items.count { it.isCustom }
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