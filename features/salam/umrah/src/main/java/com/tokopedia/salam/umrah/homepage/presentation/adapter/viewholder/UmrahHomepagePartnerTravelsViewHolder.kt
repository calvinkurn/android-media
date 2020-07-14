package com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsEntity
import com.tokopedia.salam.umrah.homepage.presentation.adapter.UmrahHomepagePartnerTravelAdapter
import com.tokopedia.salam.umrah.homepage.presentation.fragment.UmrahHomepageFragment
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import kotlinx.android.synthetic.main.partial_umrah_home_page_partner_travel.view.*

/**
 * @author by firman on 20/01/19
 */

class UmrahHomepagePartnerTravelsViewHolder(view: View, private val onItemBindListener: onItemBindListener,
                                            private val adapterPartnerTravel: UmrahHomepagePartnerTravelAdapter) : AbstractViewHolder<UmrahTravelAgentsEntity>(view) {

    override fun bind(element: UmrahTravelAgentsEntity) {
        with(itemView) {
            if (element.isLoaded && element.umrahTravelAgents.isNotEmpty()) {
                umrah_partner_section_layout.show()
                umrah_partner_section_shimmering.hide()
                onItemBindListener.onImpressionPartnerTravel(resources.getString(R.string.umrah_home_page_partner_label),element)
                adapterPartnerTravel.setList(element.umrahTravelAgents)
                rv_umrah_home_page_partner_travel.apply {
                    adapter = adapterPartnerTravel
                    layoutManager = GridLayoutManager(context, COLUMN_SIZE)
                }
                tg_umrah_list_travel.setOnClickListener {
                    onItemBindListener.onClickAllPartner()
                }
            } else {
                umrah_partner_section_layout.hide()
                umrah_partner_section_shimmering.show()
                if (!UmrahHomepageFragment.isRequestedPartner) {
                    onItemBindListener.onBindPartnerVH(element.isLoadFromCloud)
                    UmrahHomepageFragment.isRequestedPartner = true
                } else {

                }
            }

        }
    }

    companion object {
        val LAYOUT = R.layout.partial_umrah_home_page_partner_travel
        val COLUMN_SIZE = 3
    }
}