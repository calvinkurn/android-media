package com.tokopedia.talk.feature.sellersettings.common.activity


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.header.HeaderUnify
import com.tokopedia.talk.R
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.sellersettings.common.util.UserSessionListener
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TalkSellerSettingsActivity : BaseSimpleActivity(), HasComponent<TalkComponent>, UserSessionListener {

    companion object {
        const val KEY_NAVIGATION_PARAM = "navigation"
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    private var navigationParam: String = ""

    override fun getParentViewResourceID(): Int {
        return R.id.talk_seller_settings_parent_view
    }

    override fun getLayoutRes() = R.layout.activity_talk_settings

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        getDataFromApplink()
        setupNavController()
    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    override fun onBackPressed() {
        if(isTaskRoot && isTalkSettingsFragment()) {
            goToInbox()
        }
        super.onBackPressed()
    }

    override fun getShopId(): String {
        return userSession.shopId
    }

    override fun getUserId(): String {
        return userSession.userId
    }

    private fun goToInbox() {
        RouteManager.route(this, ApplinkConstInternalGlobal.INBOX_TALK)
    }

    private fun isTalkSettingsFragment(): Boolean {
        return findNavController(R.id.talk_seller_settings_parent_view).currentDestination?.id == R.id.sellerSettingsFragment
    }

    private fun setupNavController() {
        val navController = findNavController(R.id.talk_seller_settings_parent_view)
        val listener = AppBarConfiguration.OnNavigateUpListener {
            navController.navigateUp()
        }

        val bundle = Bundle().apply {
            putString(KEY_NAVIGATION_PARAM, navigationParam)
        }

        findViewById<HeaderUnify>(R.id.talk_seller_settings_toolbar)?.let {
            setSupportActionBar(it)
            it.setNavigationOnClickListener {
                onBackPressed()
            }
        }

        val appBarConfiguration = AppBarConfiguration.Builder().setFallbackOnNavigateUpListener(listener).build()
        navController.setGraph(R.navigation.talk_seller_settings_navigation, bundle)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    private fun getDataFromApplink() {
        val uri = intent.data
        navigationParam = uri?.getQueryParameter(KEY_NAVIGATION_PARAM) ?: ""
    }
}