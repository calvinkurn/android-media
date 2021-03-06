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
                animName = com.tokopedia.navigation.R.raw.bottom_nav_home,
                animToEnabledName = com.tokopedia.navigation.R.raw.bottom_nav_home_to_enabled,
                imageName = com.tokopedia.navigation.R.drawable.ic_bottom_nav_home_active,
                imageEnabledName = com.tokopedia.navigation.R.drawable.ic_bottom_nav_home_enabled,
                activeButtonColor =com.tokopedia.navigation.R.color.navigation_dms_color_active_bottom_nav,
                useBadge = true,
                animSpeed = 1f,
                animToEnabledSpeed = 3f))
        menu.add(BottomMenu(
                id = 1,
                title = resources.getString(com.tokopedia.navigation.R.string.feed),
                animName = com.tokopedia.navigation.R.raw.bottom_nav_feed,
                animToEnabledName = com.tokopedia.navigation.R.raw.bottom_nav_feed_to_enabled,
                imageName = com.tokopedia.navigation.R.drawable.ic_bottom_nav_feed_active,
                imageEnabledName = com.tokopedia.navigation.R.drawable.ic_bottom_nav_feed_enabled,
                activeButtonColor = com.tokopedia.navigation.R.color.navigation_dms_color_active_bottom_nav,
                useBadge = true,
                animSpeed = 1f,
                animToEnabledSpeed = 3f))
        menu.add(BottomMenu(
                id = 2,
                title = resources.getString(com.tokopedia.navigation.R.string.official),
                animName = com.tokopedia.navigation.R.raw.bottom_nav_official,
                animToEnabledName = com.tokopedia.navigation.R.raw.bottom_nav_os_to_enabled,
                imageName = com.tokopedia.navigation.R.drawable.ic_bottom_nav_os_active,
                imageEnabledName = com.tokopedia.navigation.R.drawable.ic_bottom_nav_os_enabled,
                activeButtonColor = com.tokopedia.navigation.R.color.navigation_dms_color_active_bottom_nav_os,
                useBadge = true,
                animSpeed = 1f,
                animToEnabledSpeed = 3f))
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