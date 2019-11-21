package com.tokopedia.tokopoints.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.addpointsection.CategoriesItem

class AddPointGridAdapter(val item: ArrayList<CategoriesItem>, val listenerItemClick: AddPointGridViewHolder.ListenerItemClick) : RecyclerView.Adapter<AddPointGridViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddPointGridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tp_points_item_section_category, parent, false)
        return AddPointGridViewHolder(view, listenerItemClick)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: AddPointGridViewHolder, position: Int) {
        holder.bindGridItem(item[position])
    }
}
