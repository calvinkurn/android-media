package com.tokopedia.hotel.hoteldetail.presentation.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityData
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailFacilityAdapterTypeFactory

/**
 * @author by furqan on 06/05/19
 */
class HotelDetailFacilityFragment : BaseListFragment<FacilityData, HotelDetailFacilityAdapterTypeFactory>() {

    lateinit var facilityListData: List<FacilityData>

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
        // do nothing
    }

    fun initData(facilityListData: List<FacilityData>) {
        this.facilityListData = facilityListData
        renderList(facilityListData)
    }

}