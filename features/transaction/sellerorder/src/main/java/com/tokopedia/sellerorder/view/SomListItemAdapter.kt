package com.tokopedia.sellerorder.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.sellerorder.R
import kotlinx.android.synthetic.main.som_list_item.view.*

/**
 * Created by fwidjaja on 2019-08-26.
 */
class SomListItemAdapter : RecyclerView.Adapter<SomListItemAdapter.ViewHolder>() {
    var somItemList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.som_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return somItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tv_invoice.text = somItemList[position]
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}