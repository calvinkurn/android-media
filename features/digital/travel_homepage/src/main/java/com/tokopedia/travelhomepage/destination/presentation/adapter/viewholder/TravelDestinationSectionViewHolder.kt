package com.tokopedia.travelhomepage.destination.presentation.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.destination.listener.OnClickListener
import com.tokopedia.travelhomepage.destination.listener.OnViewHolderBindListener
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionViewModel
import com.tokopedia.travelhomepage.destination.presentation.adapter.TravelDestinationSectionAdapter
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel.Companion.CITY_DEALS_ORDER
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel.Companion.CITY_EVENT_ORDER
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel.Companion.ORDER_LIST_ORDER
import kotlinx.android.synthetic.main.travel_homepage_travel_destination_list.view.*

/**
 * @author by furqan on 06/08/2019
 */
class TravelDestinationSectionViewHolder(itemView: View, private val onViewHolderBindListener: OnViewHolderBindListener,
                                         private val onClickListener: OnClickListener)
    : AbstractViewHolder<TravelDestinationSectionViewModel>(itemView) {

    lateinit var sectionAdapter: TravelDestinationSectionAdapter

    override fun bind(element: TravelDestinationSectionViewModel) {
        if (element.isLoaded) {
            if (element.isSuccess && element.list.isNotEmpty()) {

                with(itemView) {
                    section_layout.show()
                    shimmering.hide()

                    section_title.text = element.title
                    if (element.seeAllUrl.isNotBlank()) {
                        section_see_all.show()
                        section_see_all.setOnClickListener {
                            onClickListener.clickAndRedirect(element.seeAllUrl)
                        }
                    } else section_see_all.hide()

                    if (!::sectionAdapter.isInitialized) {
                        sectionAdapter = TravelDestinationSectionAdapter(element.list, element.type, element.categoryType, onClickListener)

                        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                        list_recycler_view.layoutManager = layoutManager
                        list_recycler_view.adapter = sectionAdapter
                    } else {
                        sectionAdapter.updateList(element.list)
                    }
                }
            } else {
                itemView.section_layout.hide()
                itemView.shimmering.hide()
            }
        } else {
            itemView.shimmering.show()
            itemView.section_layout.hide()
            when (element.type) {
                CITY_EVENT_ORDER -> onViewHolderBindListener.onCityEventVHBind()
                CITY_DEALS_ORDER -> onViewHolderBindListener.onCityDealsVHBind()
                ORDER_LIST_ORDER -> onViewHolderBindListener.onOrderListVHBind()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.travel_homepage_travel_section_list
    }
}