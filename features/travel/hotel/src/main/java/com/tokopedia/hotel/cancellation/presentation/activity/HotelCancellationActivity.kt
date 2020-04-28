package com.tokopedia.hotel.cancellation.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.hotel.common.presentation.HotelBaseActivity

/**
 * @author by jessica on 27/04/20
 */

class HotelCancellationActivity: HotelBaseActivity() {
    override fun shouldShowOptionMenu(): Boolean = false
    override fun getNewFragment(): Fragment = Fragment()
}