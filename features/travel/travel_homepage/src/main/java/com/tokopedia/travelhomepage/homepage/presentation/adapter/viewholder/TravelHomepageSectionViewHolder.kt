package com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageSectionModel
import com.tokopedia.travelhomepage.homepage.presentation.adapter.TravelHomepageSectionAdapter
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.TravelHomepageActionListener
import kotlinx.android.synthetic.main.travel_homepage_travel_section_list.view.*

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageSectionViewHolder(itemView: View,
                                      private val onItemBindListener: OnItemBindListener,
                                      private val travelHomepageActionListener: TravelHomepageActionListener)
    : AbstractViewHolder<TravelHomepageSectionModel>(itemView) {

    lateinit var adapter: TravelHomepageSectionAdapter
    private var currentPosition = -1

    override fun bind(element: TravelHomepageSectionModel) {
        if (element.isLoaded) {
            if (element.isSuccess && element.list.isNotEmpty()) {
                itemView.section_layout.visibility = View.VISIBLE
                itemView.shimmering.visibility = View.GONE

                with(itemView) {
                    if (element.title.isNotEmpty()) {
                        section_title.show()
                        section_title.text = element.title
                    } else section_title.hide()

                    if (element.subtitle.isNotEmpty()) {
                        travel_homepage_section_subtitle.show()
                        travel_homepage_section_subtitle.text = element.subtitle
                    } else travel_homepage_section_subtitle.hide()

                    if (element.seeAllUrl.isNotEmpty()) {
                        section_see_all.show()
                        section_see_all.text = element.layoutData.metaText
                        section_see_all.setOnClickListener {
                            travelHomepageActionListener.onClickSeeAllProductSlider(element.layoutData.position, element.title)
                            travelHomepageActionListener.onItemClick(element.seeAllUrl)
                        }
                    } else section_see_all.hide()

                    if (!::adapter.isInitialized || currentPosition != element.layoutData.position) {
                        currentPosition = element.layoutData.position

                        adapter = TravelHomepageSectionAdapter(element.list, element.layoutData, travelHomepageActionListener)

                        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                        list_recycler_view.layoutManager = layoutManager
                        list_recycler_view.adapter = adapter
                        travelHomepageActionListener.onViewProductSlider(element.list, element.layoutData.position, element.title)
                    }
                }
            } else {
                itemView.section_layout.hide()
                itemView.shimmering.hide()
            }
        } else {
            itemView.shimmering.visibility = View.VISIBLE
            itemView.section_layout.visibility = View.GONE
            currentPosition = -1
            onItemBindListener.onHomepageSectionItemBind(element.layoutData, element.isLoadFromCloud)
        }
    }

    companion object {
        val LAYOUT = R.layout.travel_homepage_travel_section_list
    }
}