package com.tokopedia.hotel.hoteldetail.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.hoteldetail.presentation.fragment.HotelDetailAllFacilityFragment
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelDetailAllFacilityModel

/**
 * @author by furqan on 06/05/19
 */
class HotelDetailAllFacilityActivity : HotelBaseActivity() {

    override fun getParentViewResourceID(): Int = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment =
            HotelDetailAllFacilityFragment.getInstance(
                    intent.getStringExtra(HotelDetailAllFacilityFragment.EXTRA_PROPERTY_NAME),
                    intent.getParcelableExtra(HotelDetailAllFacilityFragment.EXTRA_PROPERTY_DETAIL),
                    intent.getStringExtra(HotelDetailAllFacilityFragment.EXTRA_TAB_TITLE)
            )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.elevation = 0F
        updateTitle(intent.getStringExtra(HotelDetailAllFacilityFragment.EXTRA_PROPERTY_NAME))
    }

    companion object {

        fun getCallingIntent(context: Context, propertyName: String, data: HotelDetailAllFacilityModel, tabTitle: String): Intent =
                Intent(context, HotelDetailAllFacilityActivity::class.java)
                        .putExtra(HotelDetailAllFacilityFragment.EXTRA_PROPERTY_NAME, propertyName)
                        .putExtra(HotelDetailAllFacilityFragment.EXTRA_PROPERTY_DETAIL, data)
                        .putExtra(HotelDetailAllFacilityFragment.EXTRA_TAB_TITLE, tabTitle)

    }
}