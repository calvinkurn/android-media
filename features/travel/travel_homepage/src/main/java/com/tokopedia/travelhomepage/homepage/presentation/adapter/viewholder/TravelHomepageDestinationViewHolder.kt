package com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder

import androidx.recyclerview.widget.GridLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageDestinationModel
import com.tokopedia.travelhomepage.homepage.presentation.adapter.TravelHomepageDestinationAdapter
import com.tokopedia.travelhomepage.homepage.presentation.adapter.itemdecoration.TravelHomepageDestinationViewDecorator
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemClickListener
import kotlinx.android.synthetic.main.travel_homepage_travel_destination_list.view.*

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageDestinationViewHolder(itemView: View, private val onItemBindListener: OnItemBindListener,
                                          private val onItemClickListener: OnItemClickListener)
    : AbstractViewHolder<TravelHomepageDestinationModel>(itemView) {

    lateinit var recentSearchAdapter: TravelHomepageDestinationAdapter

    override fun bind(element: TravelHomepageDestinationModel) {
        if (element.isLoaded) {
            with(itemView) {
                itemView.section_layout.show()
                itemView.shimmering.hide()
                section_title.text = element.meta.title

                if (!::recentSearchAdapter.isInitialized) {
                    recentSearchAdapter = TravelHomepageDestinationAdapter(element.destination, onItemClickListener)

                    val layoutManager = GridLayoutManager(this.context, 2, GridLayoutManager.VERTICAL, false)
                    layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return if (position == 0) 2 else 1
                        }
                    }
                    list_recycler_view.layoutManager = layoutManager
                    list_recycler_view.addItemDecoration(TravelHomepageDestinationViewDecorator())
                    list_recycler_view.adapter = recentSearchAdapter
                } else {
                    recentSearchAdapter.updateList(element.destination)
                }
            }
        } else {
            itemView.section_layout.hide()
            itemView.shimmering.show()
            onItemBindListener.onDestinationVHBind(element.isLoadFromCloud)
        }

    }

    companion object {
        val LAYOUT = R.layout.travel_homepage_travel_destination_list
    }
}
