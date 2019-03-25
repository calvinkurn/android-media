package com.tokopedia.hotel.common.presentation

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        // waiting for menu list from tribe
        //        menuInflater.inflate(R.menu.some_menu, menu)
        updateOptionMenuColorWhite(menu)
        return shouldShowOptionMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // waiting for menu list from tribe
        return super.onOptionsItemSelected(item)
    }

    protected fun getHotelComponent(): HotelComponent {
        if (hotelComponent == null) {
            hotelComponent = HotelComponentInstance.getDigitalBrowseComponent(application)
        }
        return hotelComponent as HotelComponent
    }

    abstract fun shouldShowOptionMenu(): Boolean
}