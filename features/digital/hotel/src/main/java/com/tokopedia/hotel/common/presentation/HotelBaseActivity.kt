package com.tokopedia.hotel.common.presentation

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.design.component.Menus
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.di.component.HotelComponent

/**
 * @author by furqan on 25/03/19
 */
abstract class HotelBaseActivity: BaseSimpleActivity() {

    private var hotelComponent: HotelComponent? = null
    private lateinit var menus: Menus

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

    override fun onMenuOpened(featureId: Int, menu: Menu?): Boolean {
        showBottomMenus()
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        if (shouldShowOptionMenu()) {
            menuInflater.inflate(R.menu.hotel_base_menu, menu)
            updateOptionMenuColorWhite(menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (shouldShowOptionMenu()) {
            if (item?.itemId ?: "" == R.id.action_overflow_menu) {
                showBottomMenus()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showBottomMenus() {
        menus = Menus(this)
        val menuItem = arrayOf(resources.getString(R.string.hotel_homepage_bottom_menu_transaction_list),
                resources.getString(R.string.hotel_homepage_bottom_menu_promo),
                resources.getString(R.string.hotel_homepage_bottom_menu_help))
        menus.setItemMenuList(menuItem)

        menus.setOnActionClickListener { view -> menus.dismiss() }

        menus.setOnItemMenuClickListener { itemMenus, pos ->
/*            when (pos) {
                MENU_ORDER_LIST -> // some action
                MENU_PROMO -> // some action
                MENU_HELP -> // some action
            }*/
            menus.dismiss()
        }

        menus.setActionText(resources.getString(R.string.hotel_homepage_bottom_menu_action_text))

        menus.show()
    }

    protected fun getHotelComponent(): HotelComponent {
        if (hotelComponent == null) {
            hotelComponent = HotelComponentInstance.getDigitalBrowseComponent(application)
        }
        return hotelComponent as HotelComponent
    }

    abstract fun shouldShowOptionMenu(): Boolean

    companion object {
        private val MENU_ORDER_LIST = 0
        private val MENU_PROMO = 1
        private val MENU_HELP = 2
    }
}