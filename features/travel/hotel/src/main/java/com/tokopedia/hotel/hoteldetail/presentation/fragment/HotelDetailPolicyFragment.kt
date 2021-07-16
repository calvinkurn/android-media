package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyPolicyData
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailFacilityAdapterTypeFactory
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelDetailPolicyModel
import kotlinx.android.synthetic.main.fragment_hotel_detail_policy.*

/**
 * @author by furqan on 07/05/19
 */
class HotelDetailPolicyFragment : BaseListFragment<PropertyPolicyData, HotelDetailFacilityAdapterTypeFactory>() {

    lateinit var connector: Connector

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_detail_policy, container, false)

    override fun getSwipeRefreshLayoutResourceId() = 0

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (::connector.isInitialized) {
            setupPolicySwitcher()
            recycler_view.isFocusable = false
        }
    }

    private fun setupPolicySwitcher() {
        if (connector.getPolicyData().checkInTo.isNotEmpty()) {
            scv_hotel_date.setLeftTitleText(getString(R.string.hotel_detail_check_from_to, connector.getPolicyData().checkInFrom, connector.getPolicyData().checkInTo))
        } else {
            scv_hotel_date.setLeftTitleText(getString(R.string.hotel_detail_check_start_from, connector.getPolicyData().checkInFrom))
        }

        if (connector.getPolicyData().checkOutFrom.isNotEmpty()) {
            scv_hotel_date.setRightTitleText(getString(R.string.hotel_detail_check_from_to, connector.getPolicyData().checkOutFrom, connector.getPolicyData().checkOutTo))
        } else {
            scv_hotel_date.setRightTitleText(getString(R.string.hotel_detail_check_to, connector.getPolicyData().checkOutTo))
        }
    }

    override fun getAdapterTypeFactory(): HotelDetailFacilityAdapterTypeFactory = HotelDetailFacilityAdapterTypeFactory()

    override fun onItemClicked(t: PropertyPolicyData?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {}

    override fun loadData(page: Int) {
        if (::connector.isInitialized) {
            renderList(connector.getPolicyData().propertyPolicy)
        }
    }

    override fun showEmpty() {
        // show nothing
    }

    interface Connector {
        fun getPolicyData(): HotelDetailPolicyModel
    }
}