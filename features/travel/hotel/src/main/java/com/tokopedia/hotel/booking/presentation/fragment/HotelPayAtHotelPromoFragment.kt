package com.tokopedia.hotel.booking.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.di.HotelBookingComponent
import com.tokopedia.hotel.common.presentation.HotelBaseFragment

class HotelPayAtHotelPromoFragment : HotelBaseFragment() {
    override fun onErrorRetryClicked() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_pay_at_hotel_promo, container, false)

    override fun getScreenName(): String = getString(R.string.hotel_payathotel_header_title)

    override fun initInjector() = getComponent(HotelBookingComponent::class.java).inject(this)

    companion object {
        fun newInstance() = HotelPayAtHotelPromoFragment()
    }
}