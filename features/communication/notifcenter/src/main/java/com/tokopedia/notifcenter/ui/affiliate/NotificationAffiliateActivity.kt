package com.tokopedia.notifcenter.ui.affiliate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.di.DaggerNotificationComponent
import com.tokopedia.notifcenter.di.NotificationComponent
import com.tokopedia.notifcenter.di.module.CommonModule
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class NotificationAffiliateActivity : BaseSimpleActivity() {

    @Inject
    @JvmField
    var userSessionInterface: UserSessionInterface? = null

    override fun getLayoutRes(): Int = R.layout.activity_notification_affiliate
    override fun getNewFragment(): Fragment = NotificationAffiliateFragment()
    override fun getParentViewResourceID(): Int = R.id.fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        setupToolbar()
    }

    private fun setupToolbar() {
        val navToolbar = findViewById<NavToolbar>(R.id.affiliate_nc_nav_toolbar)
        navToolbar?.switchToLightToolbar()
        val view = View.inflate(
            this,
            R.layout.partial_affiliate_nc_nav_content_view,
            null
        ).also {
            it.findViewById<ImageUnify>(R.id.iv_user_thumbnail)
                ?.setImageUrl(userSessionInterface?.profilePicture.toString())
            it.findViewById<Typography>(R.id.txt_user_name)?.text = userSessionInterface?.name
        }
        navToolbar?.setCustomViewContentView(view)
        navToolbar?.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_CUSTOM)
    }

    private fun initInjector() {
        generateDaggerComponent().inject(this)
    }

    private fun generateDaggerComponent(): NotificationComponent =
        DaggerNotificationComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .commonModule(CommonModule(this))
            .build()
}
