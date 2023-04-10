package com.tokopedia.hotel.hoteldetail.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.hoteldetail.di.DaggerHotelDetailComponent
import com.tokopedia.hotel.hoteldetail.di.HotelDetailComponent
import com.tokopedia.hotel.hoteldetail.presentation.fragment.HotelReviewFragment

/**
 * @author by jessica on 29/04/19
 */

class HotelReviewActivity : HotelBaseActivity(), HasComponent<HotelDetailComponent> {

    override fun shouldShowOptionMenu(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 0.0f

        setTitle(R.string.hotel_review_title)
    }

    override fun getNewFragment(): Fragment =
        HotelReviewFragment.createInstance(intent.getLongExtra(HotelReviewFragment.ARG_PROPERTY_ID, 0L))

    override fun getComponent(): HotelDetailComponent =
        DaggerHotelDetailComponent.builder()
            .hotelComponent(HotelComponentInstance.getHotelComponent(application))
            .build()

    override fun getScreenName(): String = ""

    companion object {
        fun getCallingIntent(context: Context, propertyId: Long): Intent = Intent(context, HotelReviewActivity::class.java)
            .putExtra(HotelReviewFragment.ARG_PROPERTY_ID, propertyId)
    }
}
