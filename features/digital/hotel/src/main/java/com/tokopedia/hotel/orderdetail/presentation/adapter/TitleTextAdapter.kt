package com.tokopedia.hotel.orderdetail.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.hotel.R
import com.tokopedia.hotel.orderdetail.data.model.TitleText
import kotlinx.android.synthetic.main.item_hotel_detail_title_text.view.*

/**
 * @author by jessica on 13/05/19
 */

class TitleTextAdapter: RecyclerView.Adapter<TitleTextAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_hotel_detail_title_text, parent, false)
        return ViewHolder(itemView)
    }

    var titleTextList: MutableList<TitleText> = arrayListOf()

    fun addData(list: MutableList<TitleText>) {
        titleTextList = list
        notifyDataSetChanged()
    }

    fun addData(titleText: TitleText) {
        titleTextList.add(titleText)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(titleTextList.get(position), position)
    }

    override fun getItemCount(): Int {
        return titleTextList.size
    }

    inner class ViewHolder(val itemview: View): RecyclerView.ViewHolder(itemview) {

        fun bind(data: TitleText, position: Int) {
            with(itemview) {
                title.text = data.title
                text.text = data.text
            }
        }
    }
}

