package com.tokopedia.hotel.search.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.item_hotel_search_filter.view.*

class HotelSearchResultFilterAdapter(private val mode: Int = MODE_SINGLE, val listener: ActionListener? = null)
    : RecyclerView.Adapter<HotelSearchResultFilterAdapter.HotelSearchResultFilterViewHolder>() {

    var selectedItems: MutableSet<String> = mutableSetOf()
    private val _item: MutableList<HotelFilterItem> = mutableListOf()

    fun updateItems(items: List<HotelFilterItem>, selected: Set<String>? = null){
        _item.clear()
        _item.addAll(items)
        selected?.let { selectedItems.clear(); selectedItems.addAll(it) }
        notifyDataSetChanged()
    }

    fun clearSelection(){
        selectedItems.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelSearchResultFilterViewHolder {
        return HotelSearchResultFilterViewHolder(parent.inflateLayout(R.layout.item_hotel_search_filter))
    }

    override fun getItemCount(): Int = _item.size

    override fun onBindViewHolder(holder: HotelSearchResultFilterViewHolder, position: Int) {
        val item = _item[position]
        holder.bindItem(item, selectedItems.contains(item.itemId))
        holder.itemView.setOnClickListener {
            if (mode == MODE_SINGLE) selectedItems.clear()
            if (selectedItems.contains(item.itemId))
                selectedItems.remove(item.itemId)
            else
                selectedItems.add(item.itemId)
            listener?.onSelectedFilterChanged(selectedItems.toList())
            notifyDataSetChanged()
        }
    }

    inner class HotelSearchResultFilterViewHolder(view: View)
        : RecyclerView.ViewHolder(view){
        fun bindItem(item: HotelFilterItem, isSelected: Boolean) {
            with(itemView){
                if (item.isRateItem)
                    image.visible()
                else
                    image.gone()

                hotel_selection_chip_title.text = item.itemTitle
                base_item_filter.isSelected = isSelected
                if (isSelected){
                    image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hotel_rating_stars))
                    hotel_selection_chip_title.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                } else {
                    image.setImageDrawable(ContextCompat.getDrawable(context, com.tokopedia.resources.common.R.drawable.ic_system_action_star_grayscale_24))
                    hotel_selection_chip_title.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                }
            }
        }

    }

    interface ActionListener {
        fun onSelectedFilterChanged(selectedItems: List<String>)
    }

    companion object {
        const val MODE_SINGLE = 1
        const val MODE_MULTIPLE = 2
    }

    data class HotelFilterItem (
            val itemId: String = "",
            val itemTitle: String = "",
            val isRateItem: Boolean = false
    )
}