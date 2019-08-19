package com.tokopedia.travel.homepage.presentation.adapter.viewholder

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageCategoryListModel
import com.tokopedia.travel.homepage.presentation.adapter.TravelHomepageCategoryListAdapter
import com.tokopedia.travel.homepage.presentation.adapter.TravelHomepageCategoryListShimmeringAdapter
import com.tokopedia.travel.homepage.presentation.listener.OnItemBindListener

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageCategoryViewHolder(itemView: View, private val onItemBindListener: OnItemBindListener)
    : AbstractViewHolder<TravelHomepageCategoryListModel>(itemView) {

    var listener: TravelHomepageCategoryListAdapter.CategoryViewHolder.OnCategoryClickListener? = null
    private val categoriesRecyclerView: RecyclerView = itemView.findViewById(R.id.category_recycler_view)

    override fun bind(element: TravelHomepageCategoryListModel) {
        val layoutManager = GridLayoutManager(itemView.context, CATEGORY_SPAN_COUNT)
        categoriesRecyclerView.layoutManager = layoutManager

        if (element.isLoaded) {
            categoriesRecyclerView.adapter = TravelHomepageCategoryListAdapter(element.categories, listener)
        } else {
            onItemBindListener.onCategoryVHBind()

            categoriesRecyclerView.adapter = TravelHomepageCategoryListShimmeringAdapter()
        }
    }

    companion object {
        val LAYOUT = R.layout.travel_homepage_category_list
        const val CATEGORY_SPAN_COUNT = 5
    }
}