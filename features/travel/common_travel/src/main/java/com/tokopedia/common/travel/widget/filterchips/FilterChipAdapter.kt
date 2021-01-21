package com.tokopedia.common.travel.widget.filterchips

import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.common.travel.R
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * @author by jessica on 18/04/19
 */

class FilterChipAdapter(val list: List<String>, val listener: OnClickListener,
                        private val onResetChipListener: ResetChipListener,
                        @ColorRes val selectedColor: Int = com.tokopedia.unifyprinciples.R.color.Unify_G300)
    : RecyclerView.Adapter<FilterChipAdapter.ViewHolder>() {

    var selectOnlyOneChip = false
    var initialPositionSelected: Int? = null
    var canDiselectAfterSelect = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.widget_filter_chip_item, parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            chips.chipSize = ChipsUnify.SIZE_MEDIUM

            chips.chipText = list[position]
            if (initialPositionSelected != null && position == initialPositionSelected) selectChip()
            else chips.chipType = ChipsUnify.TYPE_NORMAL

            chips.setOnClickListener {
                onChipsClicked(chips)
            }
        }
    }

    fun onChipsClicked(chips: ChipsUnify) {
        if (selectOnlyOneChip && !chips.isSelected) onResetChipListener.onResetChip()
        if (canDiselectAfterSelect) chips.isSelected = !chips.isSelected
        else if (!chips.isSelected) chips.isSelected = true
        if (selectedColor > 0) {
            if (chips.isSelected) chips.chipType = ChipsUnify.TYPE_SELECTED
            else chips.chipType = ChipsUnify.TYPE_NORMAL
        }
        listener.onChipClickListener(chips.chipText.toString(), chips.isSelected)
    }

    fun resetChipSelected() {
        initialPositionSelected = null
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val chips: ChipsUnify = view.findViewById(R.id.chips)

        fun selectChip() {
            onChipsClicked(chips)
        }
    }

    interface OnClickListener{
        fun onChipClickListener(string: String, isSelected: Boolean)
    }

    interface ResetChipListener{
        fun onResetChip()
    }
}