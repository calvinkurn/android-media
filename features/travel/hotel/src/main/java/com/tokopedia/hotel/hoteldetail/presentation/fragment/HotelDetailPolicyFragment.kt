package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.FragmentHotelDetailPolicyBinding
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyPolicyData
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailFacilityAdapterTypeFactory
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelDetailPolicyModel
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * @author by furqan on 07/05/19
 */
class HotelDetailPolicyFragment : BaseListFragment<PropertyPolicyData, HotelDetailFacilityAdapterTypeFactory>() {

    lateinit var connector: Connector
    private var binding by autoClearedNullable<FragmentHotelDetailPolicyBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHotelDetailPolicyBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun getSwipeRefreshLayoutResourceId() = 0

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (::connector.isInitialized) {
            setupPolicySwitcher()
            binding?.recyclerView?.isFocusable = false
        }
    }

    private fun setupPolicySwitcher() {
        if (connector.getPolicyData().checkInTo.isNotEmpty()) {
            binding?.scvHotelDate?.setLeftTitleText(getString(R.string.hotel_detail_check_from_to, connector.getPolicyData().checkInFrom, connector.getPolicyData().checkInTo))
        } else {
            binding?.scvHotelDate?.setLeftTitleText(getString(R.string.hotel_detail_check_start_from, connector.getPolicyData().checkInFrom))
        }

        if (connector.getPolicyData().checkOutFrom.isNotEmpty()) {
            binding?.scvHotelDate?.setRightTitleText(getString(R.string.hotel_detail_check_from_to, connector.getPolicyData().checkOutFrom, connector.getPolicyData().checkOutTo))
        } else {
            binding?.scvHotelDate?.setRightTitleText(getString(R.string.hotel_detail_check_to, connector.getPolicyData().checkOutTo))
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