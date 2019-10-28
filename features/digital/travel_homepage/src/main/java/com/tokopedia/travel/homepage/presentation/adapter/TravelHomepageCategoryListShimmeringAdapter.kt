package com.tokopedia.travel.homepage.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.travel.homepage.R

/**
 * @author by jessica on 2019-08-09
 */

class TravelHomepageCategoryListShimmeringAdapter:
        RecyclerView.Adapter<TravelHomepageCategoryListShimmeringAdapter.CategoryShimmeringViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CategoryShimmeringViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(CategoryShimmeringViewHolder.LAYOUT, parent, false)
        return CategoryShimmeringViewHolder(view)
    }

    override fun getItemCount(): Int = CategoryShimmeringViewHolder.ITEM_COUNT

    override fun onBindViewHolder(holder: CategoryShimmeringViewHolder, position: Int) {
        // do nothing
    }

    class CategoryShimmeringViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        companion object {
            val LAYOUT = R.layout.travel_homepage_category_list_item_shimmering
            const val ITEM_COUNT = 10
        }
    }
}