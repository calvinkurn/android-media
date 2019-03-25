package com.tokopedia.hotel.common.presentation

import android.os.Bundle
import android.os.PersistableBundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.common.di.component.HotelComponent

/**
 * @author by furqan on 25/03/19
 */
abstract class HotelBaseActivity: BaseSimpleActivity() {

    private var hotelComponent: HotelComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        initInjector()
    }

    private fun initInjector() {
        getHotelComponent().inject(this)
    }

    protected fun getHotelComponent(): HotelComponent {
        if (hotelComponent == null) {
            hotelComponent = HotelComponentInstance.getDigitalBrowseComponent(application)
        }
        return hotelComponent as HotelComponent
    }
}