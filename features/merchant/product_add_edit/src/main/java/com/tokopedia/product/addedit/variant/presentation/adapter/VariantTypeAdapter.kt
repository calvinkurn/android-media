package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantTypeViewHolder

class VariantTypeAdapter(private val clickListener: OnVariantTypeClickListener)
    : RecyclerView.Adapter<VariantTypeViewHolder>(), VariantTypeViewHolder.OnVariantTypeViewHolderClickListener {

    interface OnVariantTypeClickListener {
        fun onVariantTypeSelected(adapterPosition: Int, variantDetail: VariantDetail)
        fun onVariantTypeDeselected(adapterPosition: Int, variantDetail: VariantDetail): Boolean
    }

    private var items: List<VariantDetail> = listOf()
    private var maxSelectedItems = 0
    private var selectedItems: ArrayList<VariantTypeViewHolder.ViewHolderState> = arrayListOf()

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
        // execute the callback function
        clickListener.onVariantTypeSelected(position, items[position])
        // from normal to selected
        selectedItems[position] = VariantTypeViewHolder.ViewHolderState.SELECTED
        // disable unselected items when maximum selected items reached
        manageUnselectedItems(getSelectedCount())
    }

    override fun onVariantTypeDeselected(position: Int): Boolean {
        // execute the callback function
        val isConfirmed = clickListener.onVariantTypeDeselected(position, items[position])
        // from selected to normal if confirmed
        if (isConfirmed) selectedItems[position] = VariantTypeViewHolder.ViewHolderState.NORMAL
        // disable unselected items when maximum selected items reached
        manageUnselectedItems(getSelectedCount())
        return isConfirmed
    }

    fun setData(items: List<VariantDetail>) {
        this.items = items
        selectedItems = ArrayList(items.map { VariantTypeViewHolder.ViewHolderState.NORMAL })
        notifyDataSetChanged()
    }

    fun setMaxSelectedItems(max: Int) {
        maxSelectedItems = max
    }

    fun getItem(position: Int): VariantDetail {
        return items[position]
    }

    fun getItems(): List<VariantDetail> {
        return items
    }

    fun deselectItem(adapterPosition: Int) {
        selectedItems[adapterPosition] = VariantTypeViewHolder.ViewHolderState.NORMAL
        manageUnselectedItems(getSelectedCount())
    }

    fun getPositionsByIds(variantIds: List<Int>): List<Int> {
        val positions = mutableListOf<Int>()
        items.forEachIndexed { index, variantDetail ->
            val variantId = variantDetail.variantID
            if (variantIds.contains(variantId)) positions.add(index)
        }
        return positions
    }

    private fun manageUnselectedItems(selectedCount: Int) {
        if (selectedCount >= maxSelectedItems) disableUnselectedItems()
        else enableUnselectedItems()
    }

    private fun getSelectedCount(): Int {
        return selectedItems.count { it == VariantTypeViewHolder.ViewHolderState.SELECTED }
    }

    private fun disableUnselectedItems() {
        selectedItems.forEachIndexed { index, viewHolderState ->
            if (viewHolderState == VariantTypeViewHolder.ViewHolderState.NORMAL) {
                selectedItems[index] = VariantTypeViewHolder.ViewHolderState.DISABLED
            }
        }
        notifyDataSetChanged()
    }

    private fun enableUnselectedItems() {
        selectedItems.forEachIndexed { index, viewHolderState ->
            if (viewHolderState == VariantTypeViewHolder.ViewHolderState.DISABLED) {
                selectedItems[index] = VariantTypeViewHolder.ViewHolderState.NORMAL
            }
        }
        notifyDataSetChanged()
    }
}