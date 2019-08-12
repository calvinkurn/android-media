package com.tokopedia.travel.homepage.presentation.adapter.viewholder

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageCategoryListModel
import com.tokopedia.travel.homepage.presentation.adapter.TravelHomepageCategoryListAdapter

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageCategoryViewHolder(itemView: View)
    : AbstractViewHolder<TravelHomepageCategoryListModel>(itemView) {

    lateinit var categoriesAdapter: TravelHomepageCategoryListAdapter
    var listener: TravelHomepageCategoryListAdapter.CategoryViewHolder.OnCategoryClickListener? = null

    override fun bind(element: TravelHomepageCategoryListModel) {
        if (element.isLoaded) {
            val categoriesRecyclerView: RecyclerView = itemView.findViewById(R.id.category_recycler_view)

            if (!::categoriesAdapter.isInitialized) {
                categoriesAdapter = TravelHomepageCategoryListAdapter(element.categories, listener)
                //set onclicklistener here

                val layoutManager = GridLayoutManager(itemView.context, CATEGORY_SPAN_COUNT)
                categoriesRecyclerView.layoutManager = layoutManager
                categoriesRecyclerView.adapter = categoriesAdapter
            }
        } else {
            //show shimmering
            //hit API
        }
    }

    companion object {
        val LAYOUT = R.layout.travel_homepage_category_list
        val CATEGORY_SPAN_COUNT = 5
    }
}