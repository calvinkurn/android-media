package com.tokopedia.tokopoints.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.addpointsection.SectionsItem

class AddPointsAdapter (val item:ArrayList<SectionsItem>,val listenerItemClick: AddPointAdapterVH.ListenerItemClick): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tp_item_section_category, parent, false)
        return AddPointAdapterVH(view,listenerItemClick)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val addPointAdapterVH=holder as AddPointAdapterVH
        addPointAdapterVH.bind(item[position])
    }
}
