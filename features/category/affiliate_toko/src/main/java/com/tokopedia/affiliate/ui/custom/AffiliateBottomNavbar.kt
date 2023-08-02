package com.tokopedia.affiliate.ui.custom

import android.content.Context
import com.tokopedia.affiliate.AFFILIATE_PROMOTE_HOME
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.RemoteConfigInstance

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

    private val isAffiliatePromoteHomeEnabled =
        RemoteConfigInstance.getInstance()?.abTestPlatform?.getString(
            AFFILIATE_PROMOTE_HOME,
            ""
        ) == AFFILIATE_PROMOTE_HOME

    fun populateBottomNavigationView() {
        val secondTabText = context.resources.getString(
            if (isAffiliatePromoteHomeEnabled) {
                R.string.affiliate_performa
            } else {
                R.string.affiliate_promo
            }
        )
        menu.add(
            BottomMenu(
                R.id.menu_promo_affiliate,
                context.resources.getString(R.string.affiliate_home),
                null,
                null,
                R.drawable.ic_bottom_nav_home_active_home,
                R.drawable.ic_bottom_nav_home_inactive_home,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                com.tokopedia.unifyprinciples.R.color.Unify_NN900,
                true,
                ANIM_SPEED,
                ANIM_TO_ENABLED_SPEED
            )
        )
        menu.add(
            BottomMenu(
                R.id.menu_performa_affiliate,
                secondTabText,
                null,
                null,
                R.drawable.ic_bottom_nav_promo_active_performa,
                R.drawable.ic_bottom_nav_promo_inactive_performa,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                com.tokopedia.unifyprinciples.R.color.Unify_NN900,
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
                R.drawable.ic_bottom_nav_promo_active_transaction,
                R.drawable.ic_bottom_nav_promo_inactive_transaction,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                com.tokopedia.unifyprinciples.R.color.Unify_NN900,
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
                com.tokopedia.unifyprinciples.R.color.Unify_NN900,
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

    fun hideBottomNav() {
        bottomNavigation?.hide()
    }

    fun selectBottomTab(position: Int) {
        bottomNavigation?.selectBottomTab(position)
    }
}

interface AffiliateBottomNavBarInterface {
    fun selectItem(position: Int, id: Int, isNotFromBottom: Boolean = false)
}
