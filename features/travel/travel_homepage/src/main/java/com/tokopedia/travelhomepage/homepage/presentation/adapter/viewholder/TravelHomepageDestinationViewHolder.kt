package com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageDestinationModel
import com.tokopedia.travelhomepage.homepage.presentation.adapter.TravelHomepageDestinationAdapter
import com.tokopedia.travelhomepage.homepage.presentation.adapter.itemdecoration.TravelHomepageDestinationViewDecorator
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.TravelHomepageActionListener
import kotlinx.android.synthetic.main.travel_homepage_travel_destination_list.view.*

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageDestinationViewHolder(itemView: View, private val onItemBindListener: OnItemBindListener,
                                          private val travelHomepageActionListener: TravelHomepageActionListener)
    : AbstractViewHolder<TravelHomepageDestinationModel>(itemView) {

    lateinit var adapter: TravelHomepageDestinationAdapter
    private var currentPosition = -1

    override fun bind(element: TravelHomepageDestinationModel) {
        if (element.isLoaded) {
            if (element.isSuccess && element.destination.isNotEmpty()) {
                with(itemView) {
                    itemView.section_layout.show()
                    itemView.shimmering.hide()

                    if (element.meta.title.isNotEmpty()) {
                        section_title.show()
                        section_title.text = element.meta.title
                    } else section_title.hide()

                    if (element.layoutData.subtitle.isNotEmpty()) {
                        travel_homepage_destination_subtitle.show()
                        travel_homepage_destination_subtitle.text = element.layoutData.subtitle
                    } else travel_homepage_destination_subtitle.hide()

                    if (!::adapter.isInitialized || currentPosition != element.layoutData.position) {
                        currentPosition = element.layoutData.position
                        adapter = TravelHomepageDestinationAdapter(element.destination, travelHomepageActionListener,
                                element.layoutData.position, element.layoutData.title)
                        travelHomepageActionListener.onViewDynamicBanners(element.destination, element.layoutData.position, element.layoutData.title)
                        val layoutManager = GridLayoutManager(this.context, 2, GridLayoutManager.VERTICAL, false)
                        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                return when (element.spanSize) {
                                    0 -> if (position == 0) 2 else 1
                                    1 -> 2
                                    else -> 1
                                }
                            }
                        }
                        list_recycler_view.layoutManager = layoutManager
                        list_recycler_view.adapter = adapter

                        while (list_recycler_view.itemDecorationCount > 0) {
                            list_recycler_view.removeItemDecorationAt(0)
                        }
                        list_recycler_view.addItemDecoration(TravelHomepageDestinationViewDecorator(element.spanSize))
                    }
                }
            } else {
                itemView.section_layout.hide()
                itemView.shimmering.hide()
            }
        } else {
            currentPosition = -1
            itemView.section_layout.hide()
            itemView.shimmering.show()
            onItemBindListener.onDestinationItemBind(element.layoutData, element.isLoadFromCloud)
        }

    }

    companion object {
        val LAYOUT = R.layout.travel_homepage_travel_destination_list
    }
}
