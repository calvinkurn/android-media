package com.tokopedia.travel.homepage.presentation.adapter.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageRecommendationModel
import com.tokopedia.travel.homepage.presentation.adapter.TravelHomepageRecommendationAdapter
import kotlinx.android.synthetic.main.travel_homepage_travel_section_list.view.*

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageRecommendationViewHolder(itemView: View)
    : AbstractViewHolder<TravelHomepageRecommendationModel>(itemView) {

    lateinit var recentSearchAdapter: TravelHomepageRecommendationAdapter
    var listener: TravelHomepageRecommendationAdapter.RecommendationViewHolder.OnItemClickListener? = null

    override fun bind(element: TravelHomepageRecommendationModel) {
        if (element.isLoaded) {
            with(itemView) {
                section_title.text = element.meta.title

                if (!::recentSearchAdapter.isInitialized) {
                    recentSearchAdapter = TravelHomepageRecommendationAdapter(element.items, listener)

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
        val LAYOUT = R.layout.travel_homepage_travel_section_list_3
    }
}