package com.tokopedia.affiliate.ui.custom

import android.content.Context
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.show

class AffiliateBottomNavbar(
    private val bottomNavigation: LottieBottomNavbar?,
    private val menuListener: IBottomClickListener,
    private val context: Context,
    private val defaultSelectedTab: Int
) {

    private var menu: ArrayList<BottomMenu> = ArrayList()
    private val isNewNavigation = false

    companion object {
        private const val ANIM_SPEED = 1f
        private const val ANIM_TO_ENABLED_SPEED = 3f
    }

    fun populateBottomNavigationView() {
        menu.add(
            BottomMenu(
                R.id.menu_home_affiliate,
                context.resources.getString(R.string.affiliate_home),
                null,
                null,
                R.drawable.ic_bottom_nav_home_active_affiliate,
                R.drawable.ic_bottom_nav_home_inactive_affiliate,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                com.tokopedia.unifyprinciples.R.color.Unify_NN600,
                true,
                ANIM_SPEED,
                ANIM_TO_ENABLED_SPEED
            )
        )
        menu.add(
            BottomMenu(
                R.id.menu_promo_affiliate,
                context.resources.getString(R.string.affiliate_promo),
                null,
                null,
                R.drawable.ic_bottom_nav_promo_active_affiliate,
                R.drawable.ic_bottom_nav_promo_inactive_affiliate,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                com.tokopedia.unifyprinciples.R.color.Unify_NN600,
                true,
                ANIM_SPEED,
                ANIM_TO_ENABLED_SPEED
            )
        )
        menu.add(
            BottomMenu(
                R.id.menu_withdrawal_affiliate,
                context.resources.getString(R.string.affiliate_withdrawal),
                null,
                null,
                R.drawable.ic_bottom_nav_finance_active_affiliate,
                R.drawable.ic_bottom_nav_finance_inactive_affiliate,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                com.tokopedia.unifyprinciples.R.color.Unify_NN600,
                true,
                ANIM_SPEED,
                ANIM_TO_ENABLED_SPEED
            )
        )
        menu.add(
            BottomMenu(
                R.id.menu_edukasi_affiliate,
                context.resources.getString(R.string.affiliate_edukasi),
                null,
                null,
                R.drawable.ic_bottom_nav_edukasi_active_affiliate,
                R.drawable.ic_bottom_nav_edukasi_inactive_affiliate,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                com.tokopedia.unifyprinciples.R.color.Unify_NN600,
                true,
                ANIM_SPEED,
                ANIM_TO_ENABLED_SPEED
            )
        )
        bottomNavigation?.setMenu(menu, isNewNavigation)
        bottomNavigation?.setMenuClickListener(menuListener)
        setSelected(defaultSelectedTab, true)
    }

    fun setSelected(position: Int, isNotFromBottom: Boolean = false) {
        bottomNavigation?.setSelected(position, isNotFromBottom)
    }

    fun showBottomNav() {
        bottomNavigation?.show()
    }

    fun selectBottomTab(position: Int) {
        bottomNavigation?.selectBottomTab(position)
    }
}

interface AffiliateBottomNavBarInterface {
    fun selectItem(position: Int, id: Int, isNotFromBottom: Boolean = false)
}
