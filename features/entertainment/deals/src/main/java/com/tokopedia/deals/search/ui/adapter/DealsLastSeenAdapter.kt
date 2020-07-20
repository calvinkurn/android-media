package com.tokopedia.deals.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.search.listener.DealsSearchListener
import com.tokopedia.deals.search.model.response.Item
import com.tokopedia.deals.search.ui.adapter.viewholder.LastSeenViewHolder

class DealsLastSeenAdapter(private val dealsSearchListener: DealsSearchListener) : RecyclerView.Adapter<LastSeenViewHolder>() {

    var brandList = listOf<Item>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastSeenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_deals_curated, parent, false)
        return LastSeenViewHolder(view,  dealsSearchListener)
    }

    override fun getItemCount(): Int = brandList.size

    override fun onBindViewHolder(holder: LastSeenViewHolder, position: Int) {
        val item = brandList[position]
        holder.bindData(item)
    }
}