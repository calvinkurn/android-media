package com.tokopedia.autocomplete.initialstate.productline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener

class InitialStateProductListAdapter(private val listener: InitialStateItemClickListener) : RecyclerView.Adapter<InitialStateProductLineViewHolder>() {

    private var data: List<BaseItemInitialStateSearch> = ArrayList()

    fun setData(data: List<BaseItemInitialStateSearch>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InitialStateProductLineViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_autocomplete_product_list_item, parent, false)
        return InitialStateProductLineViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: InitialStateProductLineViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}