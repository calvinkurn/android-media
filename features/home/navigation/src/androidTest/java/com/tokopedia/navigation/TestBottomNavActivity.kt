package com.tokopedia.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.navigation.presentation.customview.BottomMenu
import com.tokopedia.navigation.presentation.customview.LottieBottomNavbar
import com.tokopedia.navigation.test.R;

class TestBottomNavActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav_test)

        val bottomNav: LottieBottomNavbar = findViewById(R.id.bottom_navbar)

        val menu: ArrayList<BottomMenu> = ArrayList()
        menu.add(BottomMenu(0L, resources.getString(R.string.home), R.raw.bottom_nav_home, null, com.tokopedia.navigation.R.color.color_active_bottom_nav, true))
        menu.add(BottomMenu(1L, resources.getString(R.string.feed), R.raw.bottom_nav_feed, null, com.tokopedia.navigation.R.color.color_active_bottom_nav, true))
        menu.add(BottomMenu(2L, resources.getString(R.string.official), R.raw.bottom_nav_official, null, com.tokopedia.navigation.R.color.color_active_bottom_nav_os, true))
        menu.add(BottomMenu(3L, resources.getString(R.string.keranjang), R.raw.bottom_nav_cart, null, com.tokopedia.navigation.R.color.color_active_bottom_nav, true))
        menu.add(BottomMenu(4L, resources.getString(R.string.akun), null, R.drawable.ic_mainparent_login_enable, com.tokopedia.navigation.R.color.color_active_bottom_nav, true))

        bottomNav.setMenu(menu)
        bottomNav.setSelected(0)
    }
}