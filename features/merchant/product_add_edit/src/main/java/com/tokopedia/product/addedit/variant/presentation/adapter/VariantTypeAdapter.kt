package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantTypeViewHolder
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel

class VariantTypeAdapter(private val clickListener: OnVariantTypeClickListener)
    : RecyclerView.Adapter<VariantTypeViewHolder>(), VariantTypeViewHolder.OnVariantTypeViewHolderClickListener {

    interface OnVariantTypeClickListener {
        fun onVariantTypeSelected(adapterPosition: Int, variantDetail: VariantDetail)
        fun onVariantTypeDeselected(adapterPosition: Int, variantDetail: VariantDetail)
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

    override fun onVariantTypeClicked(position: Int, state: VariantTypeViewHolder.ViewHolderState) {
        selectedItems[position] = state
        if (getSelectedCount() >= maxSelectedItems) {
            disableUnselectedItem()
        } else {
            enableUnselectedItem()
        }

        when (state) {
            // handle variant type selection
            VariantTypeViewHolder.ViewHolderState.SELECTED -> {
                clickListener.onVariantTypeSelected(position, items[position])
            }
            // handle variant type deselection
            VariantTypeViewHolder.ViewHolderState.NORMAL -> clickListener.onVariantTypeDeselected(position, items[position])
            else -> {
            }
        }
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

    fun setSelectedItems(selections: List<SelectionInputModel>) {
        items.forEachIndexed { position, variantDetail ->
            val isVariantIdExist = selections.any {
                it.variantId == variantDetail.variantID.toString()
            }
            if (isVariantIdExist) {
                items.getOrNull(position)?.let {
                    selectedItems[position] = VariantTypeViewHolder.ViewHolderState.SELECTED
                    clickListener.onVariantTypeSelected(position, it)
                }
            }
        }
    }

    private fun getSelectedCount(): Int {
        return selectedItems.count { it == VariantTypeViewHolder.ViewHolderState.SELECTED }
    }

    private fun disableUnselectedItem() {
        selectedItems.forEachIndexed { index, viewHolderState ->
            if (viewHolderState == VariantTypeViewHolder.ViewHolderState.NORMAL) {
                selectedItems[index] = VariantTypeViewHolder.ViewHolderState.DISABLED
            }
        }
        notifyDataSetChanged()
    }

    private fun enableUnselectedItem() {
        selectedItems.forEachIndexed { index, viewHolderState ->
            if (viewHolderState == VariantTypeViewHolder.ViewHolderState.DISABLED) {
                selectedItems[index] = VariantTypeViewHolder.ViewHolderState.NORMAL
            }
        }
        notifyDataSetChanged()
    }
}