package com.tokopedia.autocompletecomponent.initialstate.productline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch

class InitialStateProductListAdapter(
    private val listener: ProductLineListener,
) : RecyclerView.Adapter<InitialStateProductLineViewHolder>() {

    private var data: List<BaseItemInitialStateSearch> = ArrayList()

    fun setData(data: List<BaseItemInitialStateSearch>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InitialStateProductLineViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.layout_autocomplete_product_list_item,
                parent,
                false
            )
        return InitialStateProductLineViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: InitialStateProductLineViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}