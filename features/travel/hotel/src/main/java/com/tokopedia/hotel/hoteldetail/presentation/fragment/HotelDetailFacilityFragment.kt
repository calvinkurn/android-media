package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityData
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailFacilityAdapterTypeFactory

/**
 * @author by furqan on 06/05/19
 */
class HotelDetailFacilityFragment : BaseListFragment<FacilityData, HotelDetailFacilityAdapterTypeFactory>() {


    lateinit var connector: Connector

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.tokopedia.baselist.R.layout.fragment_base_list, container, false)
    }

    override fun getSwipeRefreshLayoutResourceId(): Int = com.tokopedia.baselist.R.id.swipe_refresh_layout

    override fun getRecyclerViewResourceId() = com.tokopedia.baselist.R.id.recycler_view


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { context ->
            val recyclerView = getRecyclerView(view) as VerticalRecyclerView
            recyclerView.clearItemDecoration()
            recyclerView.setPadding(0, 0, 0,
                context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2).toInt())
            recyclerView.clipToPadding = false
            recyclerView.isFocusable = false
        }
    }

    override fun getAdapterTypeFactory(): HotelDetailFacilityAdapterTypeFactory = HotelDetailFacilityAdapterTypeFactory()

    override fun onItemClicked(t: FacilityData) {
        // do nothing
    }

    override fun hasInitialSwipeRefresh(): Boolean = false

    override fun getScreenName(): String = ""

    override fun initInjector() {
        // no injection needed
    }

    override fun loadData(page: Int) {
        if (::connector.isInitialized) {
            renderList(connector.getFacilityData())
        }
    }

    interface Connector {
        fun getFacilityData(): List<FacilityData>
    }

}
