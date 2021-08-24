package com.tokopedia.exploreCategory.ui.bottomnav

import android.content.Context
import com.tokopedia.affiliate_toko.R
import com.tokopedia.exploreCategory.ui.activity.AffiliateActivity

class AffiliateBottomNavbar(private val bottomNavigation: LottieBottomNavbar?,
                            private val menuListener : IBottomClickListener,
                            private val context : Context) {

    var menu: ArrayList<BottomMenu> = ArrayList()
    private val isNewNavigation = false

    fun populateBottomNavigationView(){
        menu.add(BottomMenu(R.id.menu_home, context.resources.getString(R.string.home), R.raw.bottom_nav_home, R.raw.bottom_nav_home_to_enabled, R.drawable.ic_bottom_nav_home_active, R.drawable.ic_bottom_nav_home_enabled, com.tokopedia.unifyprinciples.R.color.Unify_G500, true, 1f, 3f))
        menu.add(BottomMenu(R.id.menu_feed, context.resources.getString(R.string.promo), R.raw.bottom_nav_feed, R.raw.bottom_nav_feed_to_enabled, R.drawable.ic_bottom_nav_feed_active, R.drawable.ic_bottom_nav_feed_enabled, com.tokopedia.unifyprinciples.R.color.Unify_G500, true, 1f, 3f))
        menu.add(BottomMenu(R.id.menu_os, context.resources.getString(R.string.bantuan), R.raw.bottom_nav_official, R.raw.bottom_nav_os_to_enabled, R.drawable.ic_bottom_nav_os_active, R.drawable.ic_bottom_nav_os_enabled, com.tokopedia.unifyprinciples.R.color.Unify_P500, true, 1f, 3f))
        bottomNavigation?.setMenu(menu, isNewNavigation)
        bottomNavigation?.setMenuClickListener(menuListener)
        setSelected(AffiliateActivity.HOME_MENU)
    }

    fun setSelected(position : Int){
        bottomNavigation?.setSelected(position)
    }
}