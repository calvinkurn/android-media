package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityData
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailFacilityAdapterTypeFactory

/**
 * @author by furqan on 06/05/19
 */
class HotelDetailFacilityFragment : BaseListFragment<FacilityData, HotelDetailFacilityAdapterTypeFactory>() {

    lateinit var connector: Connector

    override fun getRecyclerView(view: View): RecyclerView {
        return view.findViewById<View>(com.tokopedia.abstraction.R.id.recycler_view) as RecyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = getRecyclerView(view) as VerticalRecyclerView
        recyclerView.clearItemDecoration()
        recyclerView.setPadding(0, 0, 0,
                resources.getDimension(com.tokopedia.design.R.dimen.dp_16).toInt())
        recyclerView.clipToPadding = false
        recyclerView.isFocusable = false
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