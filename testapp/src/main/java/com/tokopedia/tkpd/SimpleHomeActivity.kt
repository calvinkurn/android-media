package com.tokopedia.tkpd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment
import com.tokopedia.navigation_common.listener.AllNotificationListener
import com.tokopedia.navigation_common.listener.FragmentListener
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener


/**
 * Created by mzennis on 14/10/20.
 */
class SimpleHomeActivity : AppCompatActivity(), AllNotificationListener, FragmentListener, MainParentStatusBarListener {

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.simple_home_activity)

                val ft = supportFragmentManager.beginTransaction()
                ft.add(R.id.container, HomeRevampFragment(), "home")
                ft.commit()
            }

        override fun onNotificationChanged(notificationCount: Int, inboxCount: Int, cartCount: Int) {
        }

        override fun isLightThemeStatusBar(): Boolean = false

        override fun onScrollToTop() {
        }

    override fun requestStatusBarLight() {
    }

    override fun requestStatusBarDark() {
    }
}