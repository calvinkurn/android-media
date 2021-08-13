package com.tokopedia.hotel.booking.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.di.HotelBookingComponent
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.databinding.FragmentHotelPayAtHotelPromoBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

class HotelPayAtHotelPromoFragment : HotelBaseFragment() {

    private var binding by autoClearedNullable<FragmentHotelPayAtHotelPromoBinding>()

    override fun onErrorRetryClicked() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHotelPayAtHotelPromoBinding.inflate(inflater, container, false)
        binding?.root?.setBackgroundResource(com.tokopedia.unifyprinciples.R.color.Unify_N0)

        return binding?.root
    }

    override fun getScreenName(): String = getString(R.string.hotel_pay_at_hotel_promo_header_title)

    override fun initInjector() = getComponent(HotelBookingComponent::class.java).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnHotelPayAtHotelPromo?.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    companion object {
        fun newInstance() = HotelPayAtHotelPromoFragment()
    }
}