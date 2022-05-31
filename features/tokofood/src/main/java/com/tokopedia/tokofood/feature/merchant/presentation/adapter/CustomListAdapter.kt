package com.tokopedia.tokofood.feature.merchant.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokofood.databinding.TokofoodItemAddOnLayoutBinding
import com.tokopedia.tokofood.databinding.TokofoodItemOrderNoteLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CustomListItemType.*
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomListItem
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.OrderNoteInputViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.ProductAddOnViewHolder

class CustomListAdapter(private val selectListener: ProductAddOnViewHolder.OnAddOnSelectListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
                OrderNoteInputViewHolder(binding)
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
                viewHolder.bindData(customListItems[position].orderNote)
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
        this.customListItems = customListItems.toMutableList()
        notifyDataSetChanged()
    }

    fun updateAddOnSelection(isSelected: Boolean, addOnPositions: Pair<Int, Int>) {
        val dataSetPosition = addOnPositions.first
        val optionIndex = addOnPositions.second
        customListItems[dataSetPosition].addOnUiModel?.isSelected = isSelected
        customListItems[dataSetPosition].addOnUiModel?.options?.get(optionIndex)?.run {
            if (selectionControlType == SelectionControlType.SINGLE_SELECTION) {
                customListItems[dataSetPosition].addOnUiModel?.options?.forEach { it.isSelected = false }
            }
            this.isSelected = isSelected
        }
    }
}