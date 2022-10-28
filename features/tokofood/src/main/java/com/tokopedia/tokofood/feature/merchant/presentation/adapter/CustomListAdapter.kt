package com.tokopedia.tokofood.feature.merchant.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokofood.databinding.TokofoodItemAddOnLayoutBinding
import com.tokopedia.tokofood.databinding.TokofoodItemOrderNoteLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CustomListItemType.ORDER_NOTE_INPUT
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CustomListItemType.PRODUCT_ADD_ON
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CustomListItemType.values
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomListItem
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.OrderNoteInputViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.ProductAddOnViewHolder

class CustomListAdapter(
        private val selectListener: ProductAddOnViewHolder.OnAddOnSelectListener,
        private val textChangeListener: OrderNoteInputViewHolder.OnNoteTextChangeListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var customListItems: MutableList<CustomListItem> = mutableListOf()

    override fun getItemViewType(position: Int): Int {
        return when (customListItems[position].listItemType) {
            PRODUCT_ADD_ON -> PRODUCT_ADD_ON.type
            ORDER_NOTE_INPUT -> ORDER_NOTE_INPUT.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (values().first { it.type == viewType }) {
            PRODUCT_ADD_ON -> {
                val binding = TokofoodItemAddOnLayoutBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                ProductAddOnViewHolder(binding, selectListener)
            }
            else -> {
                val binding = TokofoodItemOrderNoteLayoutBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                OrderNoteInputViewHolder(binding, textChangeListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            PRODUCT_ADD_ON.type -> {
                val viewHolder = holder as ProductAddOnViewHolder
                val addOnUiModel = customListItems[position].addOnUiModel
                addOnUiModel?.run { viewHolder.bindData(this, position) }
            }
            ORDER_NOTE_INPUT.type -> {
                val viewHolder = holder as OrderNoteInputViewHolder
                viewHolder.bindData(customListItems[position].orderNote, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return customListItems.size
    }

    fun getCustomListItems(): List<CustomListItem> {
        return customListItems.toList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCustomListItems(customListItems: List<CustomListItem>) {
        if (customListItems.isEmpty()) return
        this.customListItems.clear()
        this.customListItems.addAll(customListItems)
        notifyDataSetChanged()
    }

    fun updateAddOnSelection(isSelected: Boolean, addOnPositions: Pair<Int, Int>) {
        val dataSetPosition = addOnPositions.first
        val optionIndex = addOnPositions.second
        customListItems.getOrNull(dataSetPosition)?.addOnUiModel?.options?.get(optionIndex)?.run {
            if (selectionControlType == SelectionControlType.SINGLE_SELECTION) {
                customListItems[dataSetPosition].addOnUiModel?.options?.forEach { it.isSelected = false }
            }
            this.isSelected = isSelected
        }
        customListItems.getOrNull(dataSetPosition)?.addOnUiModel?.isSelected =
            isSelected || customListItems[dataSetPosition].addOnUiModel?.options?.any { it.isSelected } == true
    }

    fun updateOrderNote(orderNote:String, dataSetPosition: Int) {
        customListItems.getOrNull(dataSetPosition)?.orderNote = orderNote
    }
}