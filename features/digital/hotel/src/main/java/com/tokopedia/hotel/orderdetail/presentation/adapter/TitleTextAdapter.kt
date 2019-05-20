package com.tokopedia.hotel.orderdetail.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.hotel.R
import com.tokopedia.hotel.orderdetail.data.model.TitleContent
import kotlinx.android.synthetic.main.item_hotel_detail_title_text.view.*

/**
 * @author by jessica on 13/05/19
 */

class TitleTextAdapter(val type: Int): RecyclerView.Adapter<TitleTextAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        lateinit var itemView: View
        when (type) {
            HORIZONTAL_LAYOUT -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_hotel_detail_title_text, parent, false)
            VERTICAL_LAYOUT -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_hotel_detail_title_text_vertical, parent, false)
            HORIZONTAL_LAYOUT_ORANGE -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_hotel_detail_title_text_horizontal_orange, parent, false)
            else -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_hotel_detail_title_text, parent, false)
        }
        return ViewHolder(itemView)
    }

    var titleTextList: MutableList<TitleContent> = arrayListOf()

    fun addData(list: MutableList<TitleContent>) {
        titleTextList = list
        notifyDataSetChanged()
    }

    fun addData(titleText: TitleContent) {
        titleTextList.add(titleText)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(titleTextList.get(position), position)
    }

    override fun getItemCount(): Int {
        return titleTextList.size
    }

    inner class ViewHolder(val itemview: View): RecyclerView.ViewHolder(itemview) {

        fun bind(data: TitleContent, position: Int) {
            with(itemview) {
                title.text = data.title
                text.text = data.content
            }
        }
    }

    companion object {
        const val HORIZONTAL_LAYOUT = 1
        const val VERTICAL_LAYOUT = 2
        const val HORIZONTAL_LAYOUT_ORANGE = 11
    }
}

