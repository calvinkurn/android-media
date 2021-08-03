package com.tokopedia.autocomplete.chipwidget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.autocomplete.R

class AutocompleteChipWidgetAdapter(private val listener: AutocompleteChipWidgetViewListener) : RecyclerView.Adapter<AutocompleteChipViewHolder>() {

    private var data: List<AutocompleteChipDataView> = ArrayList()

    fun setData(data: List<AutocompleteChipDataView>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutocompleteChipViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_autocomplete_chip, parent, false)
        return AutocompleteChipViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: AutocompleteChipViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}