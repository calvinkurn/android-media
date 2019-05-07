package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyData
import kotlinx.android.synthetic.main.fragment_hotel_detail_policy.*

/**
 * @author by furqan on 07/05/19
 */
class HotelDetailPolicyFragment : Fragment() {

    private lateinit var data: PropertyData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_detail_policy, container, false)

    fun setPropertyData(data: PropertyData) {
        this.data = data
        setupPolicySwitcher()
    }

    private fun setupPolicySwitcher() {
        if (data.checkinTo.isNotEmpty()) {
            scv_hotel_date.setLeftTitleText(getString(R.string.hotel_detail_check_from_to, data.checkInFrom, data.checkinTo))
        } else {
            scv_hotel_date.setLeftTitleText(getString(R.string.hotel_detail_check_start_from, data.checkInFrom))
        }

        if (data.checkoutFrom.isNotEmpty()) {
            scv_hotel_date.setRightTitleText(getString(R.string.hotel_detail_check_from_to, data.checkoutFrom, data.checkoutTo))
        } else {
            scv_hotel_date.setRightTitleText(getString(R.string.hotel_detail_check_to, data.checkoutTo))
        }
    }
}