package com.tokopedia.travel.homepage.presentation.adapter.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageSectionViewModel
import com.tokopedia.travel.homepage.presentation.adapter.TravelHomepageSectionAdapter
import com.tokopedia.travel.homepage.presentation.listener.OnItemBindListener
import kotlinx.android.synthetic.main.travel_homepage_travel_section_list.view.*

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageSectionViewHolder(itemView: View, val onItemBindListener: OnItemBindListener)
    : AbstractViewHolder<TravelHomepageSectionViewModel>(itemView) {

    lateinit var orderAdapter: TravelHomepageSectionAdapter
    var listener: TravelHomepageSectionAdapter.ViewHolder.OnItemClickListener? = null

    override fun bind(element: TravelHomepageSectionViewModel) {
        if (element.isLoaded) {
            if (element.list.isNotEmpty()) {
                itemView.section_layout.visibility = View.VISIBLE
                with(itemView) {
                    section_title.text = element.title

                    if (!::orderAdapter.isInitialized) {
                        orderAdapter = TravelHomepageSectionAdapter(element.list, listener)

                        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                        list_recycler_view.layoutManager = layoutManager
                        list_recycler_view.adapter = orderAdapter
                    }
                }
            } else itemView.section_layout.visibility = View.GONE
        } else {
            //show shimmering
            when(element.type) {
                TravelHomepageSectionViewModel.TYPE_ORDER_LIST -> onItemBindListener.onOrderListVHBind()
                TravelHomepageSectionViewModel.TYPE_RECENT_SEARCH -> onItemBindListener.onRecentSearchVHBind()
                TravelHomepageSectionViewModel.TYPE_RECOMMENDATION -> onItemBindListener.onRecommendationVHBind()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.travel_homepage_travel_section_list
    }
}