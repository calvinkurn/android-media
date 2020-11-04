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
        menu.add(BottomMenu(0, resources.getString(com.tokopedia.navigation.R.string.home), com.tokopedia.navigation.R.raw.bottom_nav_home, com.tokopedia.navigation.R.raw.bottom_nav_home_to_enabled, com.tokopedia.navigation.R.drawable.ic_bottom_nav_home_active, com.tokopedia.navigation.R.drawable.ic_bottom_nav_home_enabled, com.tokopedia.navigation.R.color.color_active_bottom_nav, true, 1f, 3f))
        menu.add(BottomMenu(1, resources.getString(com.tokopedia.navigation.R.string.feed), com.tokopedia.navigation.R.raw.bottom_nav_feed, com.tokopedia.navigation.R.raw.bottom_nav_feed_to_enabled, com.tokopedia.navigation.R.drawable.ic_bottom_nav_feed_active, com.tokopedia.navigation.R.drawable.ic_bottom_nav_feed_enabled, com.tokopedia.navigation.R.color.color_active_bottom_nav, true, 1f, 3f))
        menu.add(BottomMenu(2, resources.getString(com.tokopedia.navigation.R.string.official), com.tokopedia.navigation.R.raw.bottom_nav_official, com.tokopedia.navigation.R.raw.bottom_nav_os_to_enabled, com.tokopedia.navigation.R.drawable.ic_bottom_nav_os_active, com.tokopedia.navigation.R.drawable.ic_bottom_nav_os_enabled, com.tokopedia.navigation.R.color.color_active_bottom_nav_os, true, 1f, 3f))
        menu.add(BottomMenu(3, resources.getString(com.tokopedia.navigation.R.string.keranjang), com.tokopedia.navigation.R.raw.bottom_nav_cart, com.tokopedia.navigation.R.raw.bottom_nav_cart_to_enabled, com.tokopedia.navigation.R.drawable.ic_bottom_nav_cart_active, com.tokopedia.navigation.R.drawable.ic_bottom_nav_cart_enabled, com.tokopedia.navigation.R.color.color_active_bottom_nav, true, 1f, 3f))
        menu.add(BottomMenu(4, resources.getString(com.tokopedia.navigation.R.string.akun), com.tokopedia.navigation.R.raw.bottom_nav_account, com.tokopedia.navigation.R.raw.bottom_nav_account_to_enabled, com.tokopedia.navigation.R.drawable.ic_bottom_nav_account_active, com.tokopedia.navigation.R.drawable.ic_bottom_nav_account_enabled, com.tokopedia.navigation.R.color.color_active_bottom_nav, true, 1f, 3f))

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