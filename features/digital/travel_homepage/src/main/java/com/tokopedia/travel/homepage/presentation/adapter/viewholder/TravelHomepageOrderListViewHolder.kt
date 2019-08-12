package com.tokopedia.travel.homepage.presentation.adapter.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageOrderListModel
import com.tokopedia.travel.homepage.presentation.adapter.TravelHomepageOrderListAdapter
import kotlinx.android.synthetic.main.travel_homepage_travel_section_list.view.*

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageOrderListViewHolder(itemView: View)
    : AbstractViewHolder<TravelHomepageOrderListModel>(itemView) {

    lateinit var orderAdapter: TravelHomepageOrderListAdapter
    var listener: TravelHomepageOrderListAdapter.OrderViewHolder.OnItemClickListener? = null

    override fun bind(element: TravelHomepageOrderListModel) {
        if (element.isLoaded) {
            with(itemView) {
                section_title.text = element.meta.title

                if (!::orderAdapter.isInitialized) {
                    orderAdapter = TravelHomepageOrderListAdapter(element.orders, listener)

                    val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                    list_recycler_view.layoutManager = layoutManager
                    list_recycler_view.adapter = orderAdapter
                }
            }
        } else {
            //show Shimmering
            //call API
        }

    }

    companion object {
        val LAYOUT = R.layout.travel_homepage_travel_section_list
    }
}