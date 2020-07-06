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
        menu.add(BottomMenu(0L, resources.getString(R.string.home), R.raw.bottom_nav_home, R.raw.bottom_nav_home_to_enabled, R.drawable.ic_bottom_nav_home_active, R.drawable.ic_bottom_nav_home_enabled, com.tokopedia.navigation.R.color.color_active_bottom_nav, true))
        menu.add(BottomMenu(1L, resources.getString(R.string.feed), R.raw.bottom_nav_feed, R.raw.bottom_nav_feed_to_enabled,  R.drawable.ic_bottom_nav_feed_active, R.drawable.ic_bottom_nav_feed_enabled,com.tokopedia.navigation.R.color.color_active_bottom_nav, true))
        menu.add(BottomMenu(2L, resources.getString(R.string.official), R.raw.bottom_nav_official, R.raw.bottom_nav_os_to_enabled,  R.drawable.ic_bottom_nav_os_active, R.drawable.ic_bottom_nav_os_enabled,com.tokopedia.navigation.R.color.color_active_bottom_nav_os, true))
        menu.add(BottomMenu(3L, resources.getString(R.string.keranjang), R.raw.bottom_nav_cart, R.raw.bottom_nav_cart_to_enabled,  R.drawable.ic_bottom_nav_cart_active, R.drawable.ic_bottom_nav_cart_enabled, com.tokopedia.navigation.R.color.color_active_bottom_nav, true))
        menu.add(BottomMenu(4L, resources.getString(R.string.akun), R.raw.bottom_nav_account,   R.raw.bottom_nav_account_to_enabled, R.drawable.ic_bottom_nav_account_active, R.drawable.ic_bottom_nav_account_enabled,com.tokopedia.navigation.R.color.color_active_bottom_nav, true))

        bottomNav.setMenu(menu)
        bottomNav.setMenuClickListener(object: IBottomClickListener {
            override fun menuClicked(position: Int, id: Long): Boolean {
                return true
            }

            override fun menuReselected(position: Int, id: Long) {

            }
        })
        bottomNav.setSelected(0)
    }
}