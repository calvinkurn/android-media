package com.tokopedia.deals.ui.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.data.entity.Category
import com.tokopedia.deals.ui.search.listener.DealsSearchListener
import com.tokopedia.deals.ui.search.ui.adapter.viewholder.CuratedViewHolder

class DealsCuratedAdapter(private val dealsSearchListener: DealsSearchListener) : RecyclerView.Adapter<CuratedViewHolder>() {

    var curatedList = listOf<Category>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuratedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_deals_curated, parent, false)
        return CuratedViewHolder(view, dealsSearchListener)
    }

    override fun getItemCount(): Int = curatedList.size

    override fun onBindViewHolder(holder: CuratedViewHolder, position: Int) {
        val item = curatedList[position]
        holder.bindData(item)
    }
}
