package com.tokopedia.hotel.hoteldetail.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.hoteldetail.presentation.fragment.HotelDetailMapFragment

/**
 * @author by furqan on 29/04/19
 */
class HotelDetailMapActivity: HotelBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(intent.getStringExtra(EXTRA_NAME))
    }

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment = HotelDetailMapFragment.getInstance(
            intent.getStringExtra(EXTRA_NAME),
            intent.getDoubleExtra(EXTRA_LATITUDE, 0.0),
            intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0),
            intent.getStringExtra(EXTRA_ADDRESS)
    )

    companion object {

        val EXTRA_NAME = "EXTRA_PROPERTY_NAME"
        val EXTRA_LATITUDE = "EXTRA_LATITUDE"
        val EXTRA_LONGITUDE = "EXTRA_LONGITUDE"
        val EXTRA_ADDRESS = "EXTRA_ADDRESS"

        fun getCallingIntent(context: Context, propertyName: String, latitude: Double, longitude: Double,
                             address: String) = Intent(context, HotelDetailMapActivity::class.java)
                .putExtra(EXTRA_NAME, propertyName)
                .putExtra(EXTRA_LATITUDE, latitude)
                .putExtra(EXTRA_LONGITUDE, longitude)
                .putExtra(EXTRA_ADDRESS, address)

    }
}