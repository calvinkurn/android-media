package com.tokopedia.flight.searchV4.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.flight.common.view.BaseFlightActivity
import com.tokopedia.flight.searchV4.presentation.fragment.FlightSearchFragment

/**
 * @author by furqan on 06/04/2020
 */
open class FlightSearchActivity : BaseFlightActivity() {

    override fun getNewFragment(): Fragment? =
            FlightSearchFragment.newInstance()
}