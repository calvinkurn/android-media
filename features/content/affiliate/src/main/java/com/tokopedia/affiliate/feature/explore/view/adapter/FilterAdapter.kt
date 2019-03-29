package com.tokopedia.affiliate.feature.explore.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterViewModel
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.showWithCondition
import java.util.*

/**
 * @author by yfsx on 07/01/19.
 */
class FilterAdapter(private val filterClickedListener: OnFilterClickedListener,
                    private val layout: Int,
                    private val page: String) : RecyclerView.Adapter<FilterAdapter.Holder>() {
    companion object {
        const val PAGE_EXPLORE = "PAGE_EXPLORE"
        const val PAGE_FILTER = "PAGE_FILTER"
    }

    private var allFilterList: List<FilterViewModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val filter = allFilterList[position]
        initView(holder, filter)
        initViewListener(holder)
    }

    private fun initView(holder: Holder, filter: FilterViewModel) {
        holder.icon.showWithCondition(filter.image.isNotBlank())
        holder.icon.loadImage(filter.image)
        holder.title.text = filter.name
        holder.itemView.isSelected = filter.isSelected
    }

    private fun initViewListener(holder: Holder) {
        holder.itemView.setOnClickListener {
            val isSelected = allFilterList[holder.adapterPosition].isSelected
            allFilterList.filter {
                it.isSelected
            }.forEachIndexed { index, model ->
                model.isSelected = false
                notifyItemChanged(index)
            }
            allFilterList[holder.adapterPosition].isSelected = !isSelected
            notifyItemChanged(holder.adapterPosition)
            filterClickedListener.onItemClicked(
                    getOnlySelectedFilter(),
                    allFilterList[holder.adapterPosition]
            )
        }
    }

    override fun getItemCount(): Int {
        if (page.equals(PAGE_EXPLORE)) {
            if (allFilterList.size > 5) {
                return 5
            }
        }
        return allFilterList.size
    }

    fun clearAllData() {
        allFilterList = ArrayList()
        notifyDataSetChanged()
    }

    fun setList(filterList: List<FilterViewModel>) {
        this.allFilterList = filterList
        notifyDataSetChanged()
    }

    fun resetAllFilters() {
        for (item in allFilterList) {
            item.isSelected = false
        }
        notifyDataSetChanged()
    }

    fun getFilterListCurrentSelectedSorted(): List<FilterViewModel> {
        val sortedList = ArrayList<FilterViewModel>()
        sortedList.addAll(addSelectedItems())
        sortedList.addAll(addUnselectedItems())
        return sortedList
    }

    fun getOnlySelectedFilter(): List<FilterViewModel> {
        val items = ArrayList<FilterViewModel>()
        for (filter in allFilterList) {
            if (filter.isSelected) {
                items.add(filter)
            }
        }
        return items
    }

    fun getNotSelectedItems(): List<FilterViewModel> {
        val items = ArrayList<FilterViewModel>()
        for (item in allFilterList) {
            if (!item.isSelected) {
                items.add(item)
            }
        }
        return items
    }

    private fun addSelectedItems(): List<FilterViewModel> {
        val sortedList = ArrayList<FilterViewModel>()
        for (item in allFilterList) {
            if (item.isSelected) {
                sortedList.add(item)
            }
        }
        return sortedList
    }

    private fun addUnselectedItems(): List<FilterViewModel> {
        val sortedList = ArrayList<FilterViewModel>()
        for (item in allFilterList) {
            if (!item.isSelected) {
                sortedList.add(item)
            }
        }
        return sortedList
    }

    private fun isFilterDetail(): Boolean {
        return layout == R.layout.item_explore_filter_child
    }

    interface OnFilterClickedListener {
        fun onItemClicked(filters: List<FilterViewModel>, filter: FilterViewModel)
    }

    class Holder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val title: TextView = itemView.findViewById(R.id.title)
    }
}
