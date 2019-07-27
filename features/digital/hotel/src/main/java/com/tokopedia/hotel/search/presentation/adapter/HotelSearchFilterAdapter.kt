package com.tokopedia.hotel.search.presentation.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.hotel.R
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.item_hotel_search_filter.view.*

class HotelSearchFilterAdapter<E: HotelSearchFilterAdapter.HotelFilterItem>(
        private val mode: Int = MODE_SINGLE
): RecyclerView.Adapter<HotelSearchFilterAdapter.HotelSearchFilterViewHolder<E>>() {

    val selectedItems: MutableSet<String> = mutableSetOf()
    private val _item: MutableList<E> = mutableListOf()

    fun updateItems(items: List<E>, selected: Set<String>? = null){
        _item.clear()
        _item.addAll(items)
        selected?.let { selectedItems.clear(); selectedItems.addAll(it) }
        notifyDataSetChanged()
    }

    fun clearSelection(){
        selectedItems.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelSearchFilterViewHolder<E> {
        return HotelSearchFilterViewHolder(parent.inflateLayout(R.layout.item_hotel_search_filter))
    }

    override fun getItemCount(): Int = _item.size

    override fun onBindViewHolder(holder: HotelSearchFilterViewHolder<E>, position: Int) {
        val item = _item[position]
        holder.bindItem(item, selectedItems.contains(item.getItemId()))
        holder.itemView.setOnClickListener {
            if (selectedItems.contains(item.getItemId()))
                selectedItems.remove(item.getItemId())
            else
                selectedItems.add(item.getItemId())
            notifyDataSetChanged()
        }
    }

    class HotelSearchFilterViewHolder<T: HotelSearchFilterAdapter.HotelFilterItem>(view: View)
        : RecyclerView.ViewHolder(view){
        fun bindItem(item: T, isSelected: Boolean) {
            with(itemView){
                if (item.isRateItem())
                    image.visible()
                else
                    image.gone()

                title.text = item.getItemTitle()
                base_item_filter.isSelected = isSelected
                if (isSelected){
                    image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_stars_active_xxl))
                    title.setTextColor(ContextCompat.getColor(context, R.color.tkpd_main_green))
                } else {
                    image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_system_action_star_grayscale_24))
                    title.setTextColor(ContextCompat.getColor(context, R.color.light_secondary))
                }
            }
        }

    }

    companion object {
        const val MODE_SINGLE = 1
        const val MODE_MULTIPLE = 2
    }

    interface HotelFilterItem {
        fun getItemId(): String
        fun getItemTitle(): String
        fun isRateItem(): Boolean = false
    }
}