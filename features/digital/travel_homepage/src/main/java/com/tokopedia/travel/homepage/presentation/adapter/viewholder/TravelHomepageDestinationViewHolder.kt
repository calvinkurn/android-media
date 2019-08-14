package com.tokopedia.travel.homepage.presentation.adapter.viewholder

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageDestinationModel
import com.tokopedia.travel.homepage.presentation.adapter.TravelHomepageDestinationAdapter
import com.tokopedia.travel.homepage.presentation.adapter.itemdecoration.TravelHomepageDestinationViewDecorator
import com.tokopedia.travel.homepage.presentation.listener.OnItemBindListener
import kotlinx.android.synthetic.main.travel_homepage_travel_destination_list.view.*

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageDestinationViewHolder(itemView: View, val onItemBindListener: OnItemBindListener)
    : AbstractViewHolder<TravelHomepageDestinationModel>(itemView) {

    lateinit var recentSearchAdapter: TravelHomepageDestinationAdapter
    var listener: TravelHomepageDestinationAdapter.DestinationViewHolder.OnItemClickListener? = null

    override fun bind(element: TravelHomepageDestinationModel) {
        if (element.isLoaded) {
            with(itemView) {
                section_title.text = element.meta.title

                if (!::recentSearchAdapter.isInitialized) {
                    recentSearchAdapter = TravelHomepageDestinationAdapter(element.destination, listener)

                    val layoutManager = GridLayoutManager(this.context, 2, GridLayoutManager.VERTICAL, false)
                    layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return if (position == 0) 2 else 1
                        }
                    }
                    list_recycler_view.layoutManager = layoutManager
                    list_recycler_view.addItemDecoration(TravelHomepageDestinationViewDecorator())
                    list_recycler_view.adapter = recentSearchAdapter
                }
            }
        } else onItemBindListener.onDestinationVHBind()

    }

    companion object {
        val LAYOUT = R.layout.travel_homepage_travel_destination_list
    }
}
