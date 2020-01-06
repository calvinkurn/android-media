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
            if (element.list.isNotEmpty()) {
                itemView.section_layout.visibility = View.VISIBLE
                itemView.shimmering.visibility = View.GONE

                with(itemView) {
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
            itemView.shimmering.visibility = View.VISIBLE
            itemView.section_layout.visibility = View.GONE
            onViewHolderBindListener.onCityDealsVHBind()
        }
    }

    companion object {
        val LAYOUT = R.layout.travel_homepage_travel_section_list
    }
}