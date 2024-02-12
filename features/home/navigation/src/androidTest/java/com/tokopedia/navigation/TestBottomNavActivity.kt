package com.tokopedia.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.navigation.presentation.customview.BottomMenu
import com.tokopedia.navigation.presentation.customview.IBottomClickListener
import com.tokopedia.navigation.presentation.customview.LottieBottomNavbar
import com.tokopedia.navigation.test.R as navigationtestR;
import com.tokopedia.navigation.R as navigationR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class TestBottomNavActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(navigationtestR.layout.activity_bottom_nav_test)

        val bottomNav: LottieBottomNavbar = findViewById(navigationtestR.id.bottom_navbar)

        val menu: ArrayList<BottomMenu> = ArrayList()
        menu.add(BottomMenu(
                id = 0,
                title = resources.getString(navigationR.string.home),
                animActive = navigationR.raw.bottom_nav_home,
                animInactive = navigationR.raw.bottom_nav_home_to_enabled,
                animActiveDark = navigationR.raw.bottom_nav_home_dark,
                animInactiveDark = navigationR.raw.bottom_nav_home_to_enabled_dark,
                imageActive = navigationR.drawable.ic_bottom_nav_home_active,
                imageInactive = navigationR.drawable.ic_bottom_nav_home_enabled,
                activeButtonColor =navigationR.color.navigation_dms_color_active_bottom_nav,
                useBadge = true,
                animActiveSpeed = 1f,
                animInactiveSpeed = 3f))
        menu.add(BottomMenu(
                id = 1,
                title = resources.getString(navigationR.string.feed),
                animActive = navigationR.raw.bottom_nav_feed,
                animInactive = navigationR.raw.bottom_nav_feed_to_enabled,
                animActiveDark = navigationR.raw.bottom_nav_feed_dark,
                animInactiveDark = navigationR.raw.bottom_nav_feed_to_enabled_dark,
                imageActive = navigationR.drawable.ic_bottom_nav_feed_active,
                imageInactive = navigationR.drawable.ic_bottom_nav_feed_enabled,
                activeButtonColor = navigationR.color.navigation_dms_color_active_bottom_nav,
                useBadge = true,
                animActiveSpeed = 1f,
                animInactiveSpeed = 3f))
        menu.add(BottomMenu(
                id = 2,
                title = resources.getString(navigationR.string.official),
                animActive = navigationR.raw.bottom_nav_official,
                animInactive = navigationR.raw.bottom_nav_os_to_enabled,
                animActiveDark = navigationR.raw.bottom_nav_official_dark,
                animInactiveDark = navigationR.raw.bottom_nav_os_to_enabled_dark,
                imageActive = navigationR.drawable.ic_bottom_nav_os_active,
                imageInactive = navigationR.drawable.ic_bottom_nav_os_enabled,
                activeButtonColor = navigationR.color.navigation_dms_color_active_bottom_nav_os,
                useBadge = true,
                animActiveSpeed = 1f,
                animInactiveSpeed = 3f))
        menu.add(
            BottomMenu(
                id = 3,
                title = resources.getString(navigationR.string.uoh),
                animActive = navigationR.raw.bottom_nav_transaction,
                animInactive = navigationR.raw.bottom_nav_transaction_to_enabled,
                animActiveDark = navigationR.raw.bottom_nav_transaction_dark,
                animInactiveDark = navigationR.raw.bottom_nav_transaction_to_enabled_dark,
                imageActive = navigationR.drawable.ic_bottom_nav_uoh_active,
                imageInactive = navigationR.drawable.ic_bottom_nav_uoh_enabled,
                activeButtonColor = unifyprinciplesR.color.Unify_G500,
                useBadge = true,
                animActiveSpeed = 1f,
                animInactiveSpeed = 3f
            )
        )
        bottomNav.setMenu(menu)
        bottomNav.setMenuClickListener(object: IBottomClickListener {
            override fun menuClicked(position: Int, id: Int): Boolean {
                return true
            }

            override fun menuReselected(position: Int, id: Int) {

            }
        })
        bottomNav.setSelected(0)
    }
}
