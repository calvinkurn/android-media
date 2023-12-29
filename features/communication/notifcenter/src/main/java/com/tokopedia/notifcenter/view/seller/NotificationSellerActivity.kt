package com.tokopedia.notifcenter.view.seller

import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.config.GlobalConfig
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.notifcenter.di.NotificationActivityComponentFactory
import com.tokopedia.notifcenter.di.NotificationComponent
import com.tokopedia.notifcenter.view.NotificationFragment
import com.tokopedia.notifcenter.view.listener.NotificationFragmentContainer
import javax.inject.Inject

/**
 * Created by faisalramd on 05/02/20.
 */

class NotificationSellerActivity :
    BaseSimpleActivity(),
    HasComponent<NotificationComponent>,
    NotificationFragmentContainer {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private var notificationComponent: NotificationComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setupInjector()
        setupFragmentFactory()
        setThemeWhiteIfSellerApp()
        super.onCreate(savedInstanceState)
        setWhiteStatusBarIfSellerApp()
        setupBackground()
    }

    private fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun initializeNotificationComponent(): NotificationComponent {
        return NotificationActivityComponentFactory
            .instance
            .createNotificationComponent(application).also {
                notificationComponent = it
            }
    }

    override fun getComponent(): NotificationComponent {
        return notificationComponent ?: initializeNotificationComponent()
    }

    private fun setupInjector() {
        component.inject(this)
    }

    override fun getNewFragment(): Fragment {
        return NotificationFragment.getFragment(
            supportFragmentManager,
            classLoader
        )
    }

    private fun setThemeWhiteIfSellerApp() {
        if (GlobalConfig.isSellerApp()) {
            setTheme(com.tokopedia.abstraction.R.style.Theme_WhiteUnify)
        }
    }

    private fun setupBackground() {
        val whiteColor = ContextCompat.getColor(
            this,
            com.tokopedia.unifyprinciples.R.color.Unify_Background
        )
        window.decorView.setBackgroundColor(whiteColor)
    }

    private fun setWhiteStatusBarIfSellerApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && GlobalConfig.isSellerApp()) {
            setStatusBarColor(androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background))
        }
        supportActionBar?.elevation = 0F
    }

    override val role: Int
        get() = RoleType.SELLER

    override fun clearNotificationCounter() {
    }

    override fun refreshNotificationCounter() {
    }

    override fun getPageSource(): String {
        return "notification"
    }
}
