package com.tokopedia.feedback_form.drawonpicture.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedback_form.drawonpicture.presentation.adapter.viewholder.BrushColorViewHolder

/**
 * @author by furqan on 01/10/2020
 */
class BrushColorAdapter(
        private val listener: BrushColorViewHolder.Listener,
        private var selectedColor: String = "",
        private val colorList: List<String> = arrayListOf())
    : RecyclerView.Adapter<BrushColorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrushColorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(BrushColorViewHolder.LAYOUT, parent, false)
        return BrushColorViewHolder(listener, view)
    }

    override fun getItemCount(): Int = colorList.size

    override fun onBindViewHolder(holder: BrushColorViewHolder, position: Int) {
        holder.bind(colorList[position], selectedColor)
    }

    fun setSelectedColor(color: String) {
        selectedColor = color
        notifyDataSetChanged()
    }
}