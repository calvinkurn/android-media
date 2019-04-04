package com.tokopedia.hotel.homepage.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.homepage.presentation.activity.HotelHomepageActivity

/**
 * @author by furqan on 28/03/19
 */
class HotelHomepageFragment: BaseDaggerFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_homepage, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startActivity(HotelDestinationActivity.createInstance(activity as HotelHomepageActivity, "Tujuan"))
    }

    override fun initInjector() {
        getComponent(HotelComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    companion object {
        fun getInstance(): HotelHomepageFragment = HotelHomepageFragment()
    }
}