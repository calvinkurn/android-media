package com.tokopedia.hotel.roomlist.widget

import android.support.annotation.ColorRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.design.text.Chips
import com.tokopedia.hotel.R

/**
 * @author by jessica on 18/04/19
 */

class ChipAdapter(val list: List<String>, val listener: OnClickListener,
                  val onResetChipListener: ResetChipListener,
                  @ColorRes val selectedColor: Int = com.tokopedia.design.R.color.black_56)
    : RecyclerView.Adapter<ChipAdapter.ViewHolder>() {

    var selectOnlyOneChip = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_chip, parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            chips.text = list.get(position)
            chips.isSelected = false
            chips.setOnClickListener {
                if (selectOnlyOneChip && !chips.isSelected) onResetChipListener.onResetChip()
                chips.isSelected = !chips.isSelected
                if (selectedColor > 0) {
                    if (chips.isSelected) setTextColor(selectedColor)
                    else setTextColor(com.tokopedia.design.R.color.black_56)
                }
                listener.onChipClickListener(chips.text.toString(), chips.isSelected)
            }
        }
    }

    fun resetChipSelected() {
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