package com.tokopedia.hotel.destination.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.data.model.RecentSearch

/**
 * @author by jessica on 01/04/19
 */

class RecentSearchAdapter(val listener: RecentSearchClickListener): RecyclerView.Adapter<ViewHolder>() {

    var recentSearchList: List<RecentSearch> = listOf()

    fun setData(list: List<RecentSearch>) {
        recentSearchList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_search, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recentSearchList.get(position))
    }

    override fun getItemCount(): Int {
        return recentSearchList.size
    }
}

class ViewHolder(val itemview: View): RecyclerView.ViewHolder(itemview) {

    var textView: TextView

    init {
        textView = itemView.findViewById(R.id.autocomplete_chips_item)
    }

    fun bind(data: RecentSearch) {
        textView.text = data.name
    }
}

