package com.tokopedia.inbox.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.inbox.R

class InboxActivity : BaseActivity() {

    private val bottomNav: BottomNavigationView? by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<BottomNavigationView?>(R.id.inbox_bottom_nav)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)
        setupBackground()
        setupNotificationBar()
        setupBottomNav()
    }

    private fun setupBackground() {
        window.decorView.setBackgroundColor(Color.WHITE)
    }

    private fun setupNotificationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }

    private fun setupBottomNav() {
        bottomNav?.apply {
            setBackgroundColor(Color.TRANSPARENT)
            itemIconTintList = null
            labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
            setOnNavigationItemSelectedListener { menu ->
                when (menu.itemId) {
                    R.id.menu_inbox_notification -> {
                        Toast.makeText(context, "notification", Toast.LENGTH_SHORT).show()
                    }
                    R.id.menu_inbox_chat -> {
                        Toast.makeText(context, "chat", Toast.LENGTH_SHORT).show()
                    }
                    R.id.menu_inbox_discussion -> {
                        Toast.makeText(context, "discussion", Toast.LENGTH_SHORT).show()
                    }
                }
                return@setOnNavigationItemSelectedListener true
            }
        }
    }
}