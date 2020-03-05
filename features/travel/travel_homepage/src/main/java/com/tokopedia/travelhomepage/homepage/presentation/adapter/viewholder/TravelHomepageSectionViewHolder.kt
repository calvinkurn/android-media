package com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageSectionModel
import com.tokopedia.travelhomepage.homepage.presentation.adapter.TravelHomepageSectionAdapter
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_ALL_DEALS
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_ALL_ORDER_LIST
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_ORDER_LIST
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECENT_SEARCH
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECOMMENDATION
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemClickListener
import kotlinx.android.synthetic.main.travel_homepage_travel_destination_list.view.*

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageSectionViewHolder(itemView: View,
                                      private val onItemBindListener: OnItemBindListener,
                                      private val onItemClickListener: OnItemClickListener)
    : AbstractViewHolder<TravelHomepageSectionModel>(itemView) {

    lateinit var orderAdapter: TravelHomepageSectionAdapter

    override fun bind(element: TravelHomepageSectionModel) {
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

                            onItemClickListener.onItemClick(element.seeAllUrl)
                        }
                    } else section_see_all.hide()

                    if (!::orderAdapter.isInitialized) {
                        orderAdapter = TravelHomepageSectionAdapter(element.list, element.type, element.categoryType, onItemClickListener)

                        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                        list_recycler_view.layoutManager = layoutManager
                        list_recycler_view.adapter = orderAdapter
                    } else {
                        orderAdapter.updateList(element.list)
                    }
                }
            } else {
                itemView.section_layout.hide()
                itemView.shimmering.hide()
            }
        } else {
            itemView.shimmering.visibility = View.VISIBLE
            itemView.section_layout.visibility = View.GONE
            onItemBindListener.onItemBindViewHolder(element.layoutData, element.isLoadFromCloud)
        }
    }

    companion object {
        val LAYOUT = R.layout.travel_homepage_travel_section_list
    }
}