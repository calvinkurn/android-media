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
        menu.add(BottomMenu(0L, "Home", R.raw.bottom_nav_home, R.color.color_active_bottom_nav))
        menu.add(BottomMenu(1L, "Feed", R.raw.bottom_nav_feed, R.color.color_active_bottom_nav))
        menu.add(BottomMenu(2L, "Official Store", R.raw.bottom_nav_official, R.color.color_active_bottom_nav_os))
        menu.add(BottomMenu(3L, "Cart", R.raw.bottom_nav_cart, R.color.color_active_bottom_nav))
        menu.add(BottomMenu(4L, "Account", R.raw.bottom_nav_account, R.color.color_active_bottom_nav))

        bottomNav.setMenu(menu)
        bottomNav.setSelected(0)
    }
}