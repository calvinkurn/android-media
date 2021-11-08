package com.tokopedia.affiliate.ui.custom

import android.content.Context
import com.tokopedia.affiliate_toko.R
import com.tokopedia.affiliate.ui.activity.AffiliateActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class AffiliateBottomNavbar(private val bottomNavigation: LottieBottomNavbar?,
                            private val menuListener : IBottomClickListener,
                            private val context : Context) {

    var menu: ArrayList<BottomMenu> = ArrayList()
    private val isNewNavigation = false

    fun populateBottomNavigationView(){
        menu.add(BottomMenu(R.id.menu_home_affiliate, context.resources.getString(R.string.affiliate_home), null, null, R.drawable.ic_bottom_nav_home_active_affiliate, R.drawable.ic_bottom_nav_home_inactive_affiliate,
                com.tokopedia.unifyprinciples.R.color.Unify_G500,com.tokopedia.unifyprinciples.R.color.Unify_NN600, true, 1f, 3f))
        menu.add(BottomMenu(R.id.menu_promo_affiliate, context.resources.getString(R.string.affiliate_promo), null, null, R.drawable.ic_bottom_nav_promo_active_affiliate, R.drawable.ic_bottom_nav_promo_inactive_affiliate,
                com.tokopedia.unifyprinciples.R.color.Unify_G500,com.tokopedia.unifyprinciples.R.color.Unify_NN600, true, 1f, 3f))
        menu.add(BottomMenu(R.id.menu_help_affiliate, context.resources.getString(R.string.affiliate_bantuan), null , null , R.drawable.ic_bottom_nav_help_active_affiliate, R.drawable.ic_bottom_nav_help_inactive_affiliate,
                com.tokopedia.unifyprinciples.R.color.Unify_G500,com.tokopedia.unifyprinciples.R.color.Unify_NN600, true, 1f, 3f))
        bottomNavigation?.setMenu(menu, isNewNavigation)
        bottomNavigation?.setMenuClickListener(menuListener)
        setSelected(AffiliateActivity.HOME_MENU)
    }

    fun setSelected(position : Int){
        bottomNavigation?.setSelected(position)
    }

    fun showBottomNav() {
        bottomNavigation?.show()
    }

    fun hideBottomNav(){
        bottomNavigation?.hide()
    }

    fun selectBottomTab(position : Int){
        bottomNavigation?.selectBottomTab(position)
    }
}

interface AffiliateBottomNavBarInterface{
    fun selectItem(position : Int, id  : Int)
}