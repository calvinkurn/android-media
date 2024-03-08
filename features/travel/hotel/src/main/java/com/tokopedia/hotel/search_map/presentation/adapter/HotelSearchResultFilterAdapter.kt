package com.tokopedia.hotel.search_map.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.ItemHotelSearchFilterBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.resources.common.R as resourcescommonR

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

        private val binding = ItemHotelSearchFilterBinding.bind(view)

        fun bindItem(item: HotelFilterItem, isSelected: Boolean) {
            with(binding){
                if (item.isRateItem)
                    image.visible()
                else
                    image.gone()

                hotelSelectionChipTitle.text = item.itemTitle
                baseItemFilter.isSelected = isSelected
                if (isSelected){
                    image.setImageDrawable(ContextCompat.getDrawable(root.context, R.drawable.ic_hotel_rating_stars))
                    hotelSelectionChipTitle.setTextColor(ContextCompat.getColor(root.context, unifyprinciplesR.color.Unify_GN500))
                } else {
                    image.setImageDrawable(ContextCompat.getDrawable(root.context, resourcescommonR.drawable.ic_system_action_star_grayscale_24))
                    hotelSelectionChipTitle.setTextColor(ContextCompat.getColor(root.context, unifyprinciplesR.color.Unify_NN950_68))
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
