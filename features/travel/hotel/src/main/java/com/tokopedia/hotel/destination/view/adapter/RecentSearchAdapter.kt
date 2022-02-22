package com.tokopedia.hotel.destination.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.data.model.RecentSearch
import com.tokopedia.hotel.destination.view.widget.HotelDeletableItemView

/**
 * @author by jessica on 01/04/19
 */

class RecentSearchAdapter(val listener: RecentSearchListener) : RecyclerView.Adapter<RecentSearchAdapter.ViewHolder>() {

    var recentSearchList: MutableList<RecentSearch> = arrayListOf()

    fun setData(list: MutableList<RecentSearch>) {
        recentSearchList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_search, parent, false)
        return ViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recentSearchList.get(position), position)
    }

    override fun getItemCount(): Int {
        if (recentSearchList.isEmpty()) listener.isRecentSearchEmpty()
        return recentSearchList.size
    }

    fun deleteAllRecentSearch() {
        listener.onDeleteAllRecentSearch()
        recentSearchList.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val itemview: View, val listener: RecentSearchListener) : RecyclerView.ViewHolder(itemview) {

        var textView: HotelDeletableItemView = itemView.findViewById(R.id.autocomplete_chips_item)

        fun bind(data: RecentSearch, position: Int) {
            textView.setItemName(data.property.value)
            textView.setOnDeleteListener(object : HotelDeletableItemView.OnDeleteListener {
                override fun onDelete() {
                    removeItemFromView(position, data.uuid)
                }
            })
            textView.setOnTextClickListener(object : HotelDeletableItemView.OnTextClickListener {
                override fun onClick() {
                    listener.onItemClicked(data)
                }
            })
        }

        fun removeItemFromView(position: Int, uuid: String){
            if(position in 0 until itemCount) {
                recentSearchList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
                listener.onDeleteRecentSearchItem(uuid)
            }
        }
    }
}

