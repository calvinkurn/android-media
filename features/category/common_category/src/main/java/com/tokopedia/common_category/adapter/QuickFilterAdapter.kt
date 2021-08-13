package com.tokopedia.common_category.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common_category.R
import com.tokopedia.common_category.interfaces.QuickFilterListener
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.unifyprinciples.Typography

class QuickFilterAdapter(private var quickFilterList: ArrayList<Filter>,
                         val quickFilterListener: QuickFilterListener, val productCount: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_QUICK_FILTER = 0
        const val VIEW_PRODUCT_COUNT = 1
        const val VIEW_SHIMMER = 2

        const val SHIMMER_LAYOUT_COUNT = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_QUICK_FILTER -> {
                val v = LayoutInflater.from(parent.context).inflate(QuickFilterViewHolder.LAYOUT, parent, false)
                QuickFilterViewHolder(v)
            }
            VIEW_PRODUCT_COUNT -> {
                val v = LayoutInflater.from(parent.context).inflate(ProductCountViewHolder.LAYOUT, parent, false)
                ProductCountViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(ShimmerViewHolder.Layout, parent, false)
                return ShimmerViewHolder(v)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (quickFilterList.size <= 0) {
            SHIMMER_LAYOUT_COUNT
        } else {
            quickFilterList.size + 1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_QUICK_FILTER -> setQuickFilterData(holder as QuickFilterViewHolder, position - 1)
            VIEW_PRODUCT_COUNT -> setProductCountData(holder as ProductCountViewHolder, position)
            else -> {
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (quickFilterList.size <= 0) {
            VIEW_SHIMMER
        } else {
            if (position == 0) {
                VIEW_PRODUCT_COUNT
            } else
                VIEW_QUICK_FILTER
        }
    }

    private fun setQuickFilterData(holder: QuickFilterViewHolder, position: Int) {
        val option: Option = quickFilterList[position].options[0]
        bindFilterNewIcon(holder, option)

        holder.quickFilterText.text = option.name

        setBackgroundResource(holder, option)

        holder.quickFilterText.setOnClickListener {
            quickFilterListener.onQuickFilterSelected(option)
        }
        holder.filterNewIcon.setOnClickListener {
            quickFilterListener.onQuickFilterSelected(option)
        }
    }

    private fun setProductCountData(holder: ProductCountViewHolder, position: Int) {
        if(productCount.isNotEmpty()){
            holder.productCountText.text = productCount
        }else {
            holder.productCountText.visibility = View.GONE
        }
    }

    private fun bindFilterNewIcon(holderQuickFilter: QuickFilterViewHolder, option: Option) {
        if (option.isNew) {
            holderQuickFilter.filterNewIcon.visibility = View.VISIBLE
        } else {
            holderQuickFilter.filterNewIcon.visibility = View.GONE
        }
    }


    private fun setBackgroundResource(holderQuickFilter: QuickFilterViewHolder, option: Option) {
        if (quickFilterListener != null && quickFilterListener.isQuickFilterSelected(option)) {
            holderQuickFilter.itemContainer.setBackgroundResource(com.tokopedia.filter.R.drawable.quick_filter_item_background_selected)
        } else {
            holderQuickFilter.itemContainer.setBackgroundResource(com.tokopedia.filter.R.drawable.quick_filter_item_background_neutral)
        }
    }


    class QuickFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val LAYOUT = R.layout.item_nav_quick_filter
        }

        val quickFilterText = itemView.findViewById<Typography>(R.id.quick_filter_text)
        val itemContainer = itemView.findViewById<View>(R.id.filter_item_container)
        val filterNewIcon = itemView.findViewById<View>(R.id.filter_new_icon)
    }

    class ProductCountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val LAYOUT = R.layout.item_nav_product_count
        }

        val productCountText = itemView.findViewById<Typography>(R.id.product_count_text)
    }


    class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val Layout = R.layout.item_nav_hotlist_quick_filter_shimmer
        }
    }

    fun clearData() {
        val itemSizeBeforeCleared = itemCount
        quickFilterList.clear()
        notifyItemRangeRemoved(0, itemSizeBeforeCleared)
    }
}





