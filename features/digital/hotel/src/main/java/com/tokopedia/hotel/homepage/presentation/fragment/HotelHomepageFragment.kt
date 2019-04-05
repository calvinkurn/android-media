package com.tokopedia.hotel.homepage.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.homepage.di.HotelHomepageComponent
import com.tokopedia.hotel.homepage.presentation.model.HotelHomepageModel
import java.util.*

/**
 * @author by furqan on 28/03/19
 */
class HotelHomepageFragment: BaseDaggerFragment() {

    private val hotelHomepageModel: HotelHomepageModel = HotelHomepageModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_homepage, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hotelHomepageModel.checkInDateString = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT,
                TravelDateUtil.addTimeToSpesificDate(TravelDateUtil.getCurrentCalendar().time, Calendar.DATE, 1))
        hotelHomepageModel.checkOutDateString = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT,
                TravelDateUtil.addTimeToSpesificDate(TravelDateUtil.getCurrentCalendar().time, Calendar.DATE, 2))
    }

    override fun initInjector() {
        getComponent(HotelHomepageComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    companion object {
        fun getInstance(): HotelHomepageFragment = HotelHomepageFragment()
    }
}