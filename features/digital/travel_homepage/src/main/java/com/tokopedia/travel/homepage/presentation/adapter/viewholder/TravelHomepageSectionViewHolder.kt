package com.tokopedia.travel.homepage.presentation.adapter.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageSectionViewModel
import com.tokopedia.travel.homepage.presentation.adapter.TravelHomepageSectionAdapter
import com.tokopedia.travel.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_ALL_DEALS
import com.tokopedia.travel.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_ALL_ORDER_LIST
import com.tokopedia.travel.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_ORDER_LIST
import com.tokopedia.travel.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_POPULAR_SEARCH
import com.tokopedia.travel.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_POPULAR_SEARCH_CATEGORY
import com.tokopedia.travel.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECENT_SEARCH
import com.tokopedia.travel.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECOMMENDATION
import com.tokopedia.travel.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travel.homepage.presentation.listener.OnItemClickListener
import kotlinx.android.synthetic.main.travel_homepage_travel_destination_list.view.*

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageSectionViewHolder(itemView: View,
                                      private val onItemBindListener: OnItemBindListener,
                                      private val onItemClickListener: OnItemClickListener)
    : AbstractViewHolder<TravelHomepageSectionViewModel>(itemView) {

    lateinit var orderAdapter: TravelHomepageSectionAdapter

    override fun bind(element: TravelHomepageSectionViewModel) {
        if (element.isLoaded) {
            if (element.list.isNotEmpty()) {
                itemView.section_layout.visibility = View.VISIBLE
                itemView.shimmering.visibility = View.GONE

                with(itemView) {
                    section_title.text = element.title
                    if (element.seeAllUrl.isNotBlank()) {
                        section_see_all.show()
                        section_see_all.setOnClickListener {
                            if (element.type == TYPE_ORDER_LIST) onItemClickListener.onTrackEventClick(TYPE_ALL_ORDER_LIST)
                            else if (element.type == TYPE_RECOMMENDATION) onItemClickListener.onTrackEventClick(TYPE_ALL_DEALS)
                            else if (element.type == TYPE_RECENT_SEARCH && element.categoryType == TYPE_POPULAR_SEARCH_CATEGORY) onItemClickListener.onTrackEventClick(TYPE_POPULAR_SEARCH)
                            else if (element.type == TYPE_RECENT_SEARCH && element.categoryType != TYPE_POPULAR_SEARCH_CATEGORY) onItemClickListener.onTrackEventClick(TYPE_RECENT_SEARCH)
                            onItemClickListener.onItemClick(element.seeAllUrl)
                        }
                    } else section_see_all.hide()

                    if (!::orderAdapter.isInitialized) {
                        orderAdapter = TravelHomepageSectionAdapter(element.list, element.type, onItemClickListener)

                        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                        list_recycler_view.layoutManager = layoutManager
                        list_recycler_view.adapter = orderAdapter
                    }
                }
            } else {
                itemView.section_layout.hide()
                itemView.shimmering.hide()
            }
        } else {
            itemView.shimmering.visibility = View.VISIBLE
            itemView.section_layout.visibility = View.GONE
            when (element.type) {
                TYPE_ORDER_LIST -> onItemBindListener.onOrderListVHBind()
                TYPE_RECENT_SEARCH -> onItemBindListener.onRecentSearchVHBind()
                TYPE_RECOMMENDATION -> onItemBindListener.onRecommendationVHBind()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.travel_homepage_travel_section_list
    }
}