package com.tokopedia.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.navigation.presentation.customview.BottomMenu
import com.tokopedia.navigation.presentation.customview.IBottomClickListener
import com.tokopedia.navigation.presentation.customview.LottieBottomNavbar
import com.tokopedia.navigation.test.R;

class TestBottomNavActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav_test)

        val bottomNav: LottieBottomNavbar = findViewById(R.id.bottom_navbar)

        val menu: ArrayList<BottomMenu> = ArrayList()
        menu.add(BottomMenu(
                id = 0,
                title = resources.getString(com.tokopedia.navigation.R.string.home),
                animActive = com.tokopedia.navigation.R.raw.bottom_nav_home,
                animInactive = com.tokopedia.navigation.R.raw.bottom_nav_home_to_enabled,
                animActiveDark = com.tokopedia.navigation.R.raw.bottom_nav_home_dark,
                animInactiveDark = com.tokopedia.navigation.R.raw.bottom_nav_home_to_enabled_dark,
                imageActive = com.tokopedia.navigation.R.drawable.ic_bottom_nav_home_active,
                imageInactive = com.tokopedia.navigation.R.drawable.ic_bottom_nav_home_enabled,
                activeButtonColor =com.tokopedia.navigation.R.color.navigation_dms_color_active_bottom_nav,
                useBadge = true,
                animActiveSpeed = 1f,
                animInactiveSpeed = 3f))
        menu.add(BottomMenu(
                id = 1,
                title = resources.getString(com.tokopedia.navigation.R.string.feed),
                animActive = com.tokopedia.navigation.R.raw.bottom_nav_feed,
                animInactive = com.tokopedia.navigation.R.raw.bottom_nav_feed_to_enabled,
                animActiveDark = com.tokopedia.navigation.R.raw.bottom_nav_feed_dark,
                animInactiveDark = com.tokopedia.navigation.R.raw.bottom_nav_feed_to_enabled_dark,
                imageActive = com.tokopedia.navigation.R.drawable.ic_bottom_nav_feed_active,
                imageInactive = com.tokopedia.navigation.R.drawable.ic_bottom_nav_feed_enabled,
                activeButtonColor = com.tokopedia.navigation.R.color.navigation_dms_color_active_bottom_nav,
                useBadge = true,
                animActiveSpeed = 1f,
                animInactiveSpeed = 3f))
        menu.add(BottomMenu(
                id = 2,
                title = resources.getString(com.tokopedia.navigation.R.string.official),
                animActive = com.tokopedia.navigation.R.raw.bottom_nav_official,
                animInactive = com.tokopedia.navigation.R.raw.bottom_nav_os_to_enabled,
                animActiveDark = com.tokopedia.navigation.R.raw.bottom_nav_official_dark,
                animInactiveDark = com.tokopedia.navigation.R.raw.bottom_nav_os_to_enabled_dark,
                imageActive = com.tokopedia.navigation.R.drawable.ic_bottom_nav_os_active,
                imageInactive = com.tokopedia.navigation.R.drawable.ic_bottom_nav_os_enabled,
                activeButtonColor = com.tokopedia.navigation.R.color.navigation_dms_color_active_bottom_nav_os,
                useBadge = true,
                animActiveSpeed = 1f,
                animInactiveSpeed = 3f))
        menu.add(
            BottomMenu(
                id = 3,
                title = resources.getString(R.string.uoh),
                animActive = R.raw.bottom_nav_transaction,
                animInactive = R.raw.bottom_nav_transaction_to_enabled,
                animActiveDark = R.raw.bottom_nav_transaction_dark,
                animInactiveDark = R.raw.bottom_nav_transaction_to_enabled_dark,
                imageActive = R.drawable.ic_bottom_nav_uoh_active,
                imageInactive = R.drawable.ic_bottom_nav_uoh_enabled,
                activeButtonColor = com.tokopedia.unifyprinciples.R.color.Unify_G500,
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
