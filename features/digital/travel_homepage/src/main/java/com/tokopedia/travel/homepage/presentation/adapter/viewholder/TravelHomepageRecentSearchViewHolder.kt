package com.tokopedia.travel.homepage.presentation.adapter.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageRecentSearchModel
import com.tokopedia.travel.homepage.presentation.adapter.TravelHomepageRecentSearchListAdapter
import kotlinx.android.synthetic.main.travel_homepage_travel_section_list.view.*

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageRecentSearchViewHolder(itemView: View)
    : AbstractViewHolder<TravelHomepageRecentSearchModel>(itemView) {

    lateinit var recentSearchAdapter: TravelHomepageRecentSearchListAdapter
    var listener: TravelHomepageRecentSearchListAdapter.RecentSearchViewHolder.OnItemClickListener? = null

    override fun bind(element: TravelHomepageRecentSearchModel) {
        if (element.isLoaded) {
            with(itemView) {
                section_title.text = element.meta.title

                if (!::recentSearchAdapter.isInitialized) {
                    recentSearchAdapter = TravelHomepageRecentSearchListAdapter(element.items, listener)

                    val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                    list_recycler_view.layoutManager = layoutManager
                    list_recycler_view.adapter = recentSearchAdapter
                }
            }
        } else {
            //show Shimmering
            //call API
        }

    }

    companion object {
        val LAYOUT = R.layout.travel_homepage_travel_section_list_2
    }
}