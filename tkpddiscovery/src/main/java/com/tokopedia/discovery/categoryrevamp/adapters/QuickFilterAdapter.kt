package com.tokopedia.discovery.categoryrevamp.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.view.interfaces.QuickFilterListener
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.unifyprinciples.Typography

class QuickFilterAdapter(private var quickFilterList: ArrayList<Filter>,
                         val quickFilterListener: QuickFilterListener, val productCount: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        const val VIEW_PRODUCT_COUNT = 1
        const val VIEW_QUICK_FILTER = 0
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_QUICK_FILTER -> {
                val v = LayoutInflater.from(parent.context).inflate(QuickFilterViewHolder.LAYOUT, parent, false)
                QuickFilterViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(ProductCountViewHolder.LAYOUT, parent, false)
                ProductCountViewHolder(v)
            }
        }
    }

    override fun getItemCount(): Int {
        return quickFilterList.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            VIEW_QUICK_FILTER -> setQuickFilterData(holder as QuickFilterViewHolder, position - 1)
            else -> setProductCountData(holder as ProductCountViewHolder, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            VIEW_PRODUCT_COUNT
        } else
            VIEW_QUICK_FILTER
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
        holder.productCountText.text = productCount
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
            holderQuickFilter.itemContainer.setBackgroundResource(R.drawable.quick_filter_item_background_selected)
        } else {
            holderQuickFilter.itemContainer.setBackgroundResource(R.drawable.quick_filter_item_background_neutral)
        }
    }


    class QuickFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object{
            val LAYOUT = R.layout.item_nav_quick_filter
        }
        val quickFilterText = itemView.findViewById<Typography>(R.id.quick_filter_text)
        val itemContainer = itemView.findViewById<View>(R.id.filter_item_container)
        val filterNewIcon = itemView.findViewById<View>(R.id.filter_new_icon)
    }

    class ProductCountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object{
            val LAYOUT = R.layout.item_nav_product_count
        }
        val productCountText = itemView.findViewById<Typography>(R.id.product_count_text)
    }

    fun clearData() {
        val itemSizeBeforeCleared = itemCount
        quickFilterList.clear()
        notifyItemRangeRemoved(0, itemSizeBeforeCleared)
    }
}





