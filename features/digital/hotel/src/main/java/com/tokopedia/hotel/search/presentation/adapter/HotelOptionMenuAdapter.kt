package com.tokopedia.hotel.search.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.Sort
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.item_hotel_sort_option.view.*

class HotelOptionMenuAdapter(private val mode: Int,
                             private val sortMenu: List<Sort>): RecyclerView.Adapter<HotelOptionMenuAdapter.HotelOptionMenuViewHolder>() {
    private val isCheckable: Boolean
        get() = mode == MODE_CHECKED

    var selectedSort: Sort? = null
    var listener: OnSortMenuSelected? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelOptionMenuViewHolder {
        return HotelOptionMenuViewHolder(parent.inflateLayout(R.layout.item_hotel_sort_option))
    }

    override fun getItemCount(): Int = sortMenu.size

    override fun onBindViewHolder(holder: HotelOptionMenuViewHolder, position: Int) {
        val menu = sortMenu[position]
        holder.bind(menu, isCheckable && selectedSort?.name == menu.name)
    }

    inner class HotelOptionMenuViewHolder(view: View): RecyclerView.ViewHolder(view){

        fun bind(item: Sort, isSelected: Boolean){
            with(itemView){
                text_view_title.text = item.displayName
                if (isSelected)
                    image_view_check.visible()
                else
                    image_view_check.gone()

                setOnClickListener {
                    if (isCheckable){
                        selectedSort = item
                        notifyDataSetChanged()
                    }
                    listener?.onSelect(item)
                }
            }
        }
    }

    interface OnSortMenuSelected{
        fun onSelect(sort: Sort)
    }

    companion object {
        const val MODE_CHECKED = 1
        const val MODE_NORMAL = 0
    }
}