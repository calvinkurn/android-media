package com.tokopedia.createpost.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.bottomSheet.*
import com.tokopedia.createpost.view.listener.SearchCategoryBottomSheetListener
import com.tokopedia.createpost.view.searchtypeviewholder.SearchTypeContentViewHolder
import com.tokopedia.createpost.view.searchtypeviewholder.SearchTypeHeaderViewHolder

class SearchTypeListAdapter (
    private val itemList: List<SearchTypeData>,
    private val listener: SearchCategoryBottomSheetListener
)
    : RecyclerView.Adapter<SearchTypeListAdapter.ContentSearchTYpeItemVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentSearchTYpeItemVH {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = inflater.inflate(R.layout.search_category_header_item, parent, false)
                return SearchTypeHeaderViewHolder(view, listener)
            }
            VIEW_TYPE_CONTENT -> {
                val view = inflater.inflate(R.layout.search_category_item, parent, false)
                return SearchTypeContentViewHolder(view, listener)
            }

        }
        val view = inflater.inflate(R.layout.search_category_item, parent, false)
        return SearchTypeContentViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ContentSearchTYpeItemVH, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position < itemList.size)
            when (itemList[position].type) {
                CreatorListItemType.TYPE_HEADER ->  return VIEW_TYPE_HEADER
                CreatorListItemType.TYPE_CONTENT -> return VIEW_TYPE_CONTENT

            }
        return super.getItemViewType(position)
    }
    companion object {
        const val VIEW_TYPE_HEADER = 1
        const val VIEW_TYPE_CONTENT = 2
    }

    abstract class ContentSearchTYpeItemVH(view: View) : RecyclerView.ViewHolder(view) {

        abstract fun bind(searchTypeData: SearchTypeData)
    }


}