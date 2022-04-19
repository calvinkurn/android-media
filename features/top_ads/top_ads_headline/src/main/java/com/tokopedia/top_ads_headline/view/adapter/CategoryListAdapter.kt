package com.tokopedia.top_ads_headline.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.TopAdsHeadlineTabModel
import com.tokopedia.unifycomponents.ChipsUnify

private const val DEFAULT_SHIMMER_COUNT = 5
private const val VIEW_SHIMMER = 0
private const val VIEW_CATEGORY = 1

class CategoryListAdapter(private var list: ArrayList<TopAdsHeadlineTabModel>,
                          private val chipFilterClick: ((TopAdsHeadlineTabModel) -> Unit)?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var lastSelectedPosition = 0

    fun setItem(count: Int, position: Int) {
        list.getOrNull(position)?.let {
            it.count = count
            notifyItemChanged(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_CATEGORY -> {
                val v = LayoutInflater.from(parent.context).inflate(CategoryListViewHolder.Layout, parent, false)
                CategoryListViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(ShimmerViewHolder.Layout, parent, false)
                return ShimmerViewHolder(v)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (list.isEmpty()) {
            DEFAULT_SHIMMER_COUNT
        } else {
            list.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.size <= 0) {
            VIEW_SHIMMER
        } else {
            VIEW_CATEGORY
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? CategoryListViewHolder)?.let {
            val headlineTabModel = list[position]
            val context = holder.itemView.context
            val tabName = context.getString(headlineTabModel.name)
            if(headlineTabModel.count>0){
                it.chipFilter.chipText = "$tabName (${headlineTabModel.count})"
            } else {
                it.chipFilter.chipText = tabName
            }
            if (headlineTabModel.isSelected) {
                it.chipFilter.chipType = ChipsUnify.TYPE_SELECTED
            } else {
                it.chipFilter.chipType = ChipsUnify.TYPE_NORMAL
            }
            it.chipFilter.setOnClickListener {
                list[lastSelectedPosition].isSelected = false
                list[position].isSelected = true
                notifyItemChanged(lastSelectedPosition)
                notifyItemChanged(position)
                lastSelectedPosition = position
                chipFilterClick?.invoke(list[position])
            }
        }
    }

    class CategoryListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val Layout = R.layout.item_layout_chip_filter
        }

        val chipFilter: ChipsUnify = itemView.findViewById(R.id.chipFilter)
    }

    class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val Layout = R.layout.item_layout_topads_category_shimmer
        }
    }
}