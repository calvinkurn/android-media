package com.tokopedia.hotel.roomlist.widget

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.design.text.Chips
import com.tokopedia.hotel.R

/**
 * @author by jessica on 18/04/19
 */

class ChipAdapter(val list: List<String>, val listener: OnClickListener, val selectedColor: Int): RecyclerView.Adapter<ChipAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_chip, parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.chips.text = list.get(position)
        holder.chips.setOnClickListener {
            holder.chips.isSelected = !holder.chips.isSelected
            if (selectedColor != 0) {
                if (holder.chips.isSelected) holder.setTextColor(selectedColor)
                else holder.setTextColor(com.tokopedia.design.R.color.black_56)
            }
            listener.onChipClickListener(holder.chips.text.toString())
        }
    }

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val chips: Chips = view.findViewById(R.id.chips)

        fun setTextColor(txtColor: Int) {
            chips.setTextColor(view.getResources().getColor(txtColor))
        }
    }

    interface OnClickListener{
        fun onChipClickListener(string: String)
    }
}