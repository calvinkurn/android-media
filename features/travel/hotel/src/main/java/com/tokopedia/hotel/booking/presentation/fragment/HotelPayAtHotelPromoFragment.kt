package com.tokopedia.hotel.booking.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.di.HotelBookingComponent
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import kotlinx.android.synthetic.main.fragment_hotel_pay_at_hotel_promo.*

class HotelPayAtHotelPromoFragment : HotelBaseFragment() {
    override fun onErrorRetryClicked() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_hotel_pay_at_hotel_promo, container, false)
        rootView.setBackgroundResource(com.tokopedia.unifyprinciples.R.color.Unify_N0)

        return rootView
    }

    override fun getScreenName(): String = getString(R.string.hotel_pay_at_hotel_promo_header_title)

    override fun initInjector() = getComponent(HotelBookingComponent::class.java).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnHotelPayAtHotelPromo.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    companion object {
        fun newInstance() = HotelPayAtHotelPromoFragment()
    }
}