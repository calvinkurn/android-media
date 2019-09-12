package com.tokopedia.common.travel.widget.filterchips

import android.support.annotation.ColorRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.common.travel.R
import com.tokopedia.design.text.Chips

/**
 * @author by jessica on 18/04/19
 */

class FilterChipAdapter(val list: List<String>, val listener: OnClickListener,
                  val onResetChipListener: ResetChipListener,
                  @ColorRes val selectedColor: Int = com.tokopedia.design.R.color.black_56)
    : RecyclerView.Adapter<FilterChipAdapter.ViewHolder>() {

    var selectOnlyOneChip = false
    var initialPositionSelected: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.widget_filter_chip_item, parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            chips.text = list.get(position)
            chips.isSelected = position == initialPositionSelected
            chips.setOnClickListener {
                if (selectOnlyOneChip && !chips.isSelected) onResetChipListener.onResetChip()
                if (initialPositionSelected != null) chips.isSelected = !chips.isSelected
                if (selectedColor > 0) {
                    if (chips.isSelected) setTextColor(selectedColor)
                    else setTextColor(com.tokopedia.design.R.color.black_56)
                }
                listener.onChipClickListener(chips.text.toString(), chips.isSelected)
            }
        }
    }

    fun resetChipSelected() {
        initialPositionSelected = null
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val chips: Chips = view.findViewById(R.id.chips)

        fun setTextColor(txtColor: Int) {
            chips.setTextColor(view.getResources().getColor(txtColor))
        }
    }

    interface OnClickListener{
        fun onChipClickListener(string: String, isSelected: Boolean)
    }

    interface ResetChipListener{
        fun onResetChip()
    }
}