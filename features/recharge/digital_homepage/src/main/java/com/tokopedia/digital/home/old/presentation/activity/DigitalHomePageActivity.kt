package com.tokopedia.digital.home.old.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.digital.home.old.di.DigitalHomePageComponent
import com.tokopedia.digital.home.old.di.DigitalHomePageComponentInstance
import com.tokopedia.digital.home.old.presentation.fragment.DigitalHomePageFragment
import com.tokopedia.digital.home.presentation.activity.RechargeHomepageActivity
import com.tokopedia.graphql.data.GraphqlClient
import timber.log.Timber

/**
 * applink
 * tokopedia://recharge/home
 * or
 * RouteManager.route(this, ApplinkConst.DIGITAL_SUBHOMEPAGE_HOME)
 */

class DigitalHomePageActivity : BaseSimpleActivity(), HasComponent<DigitalHomePageComponent> {

    private lateinit var digitalHomePageComponent: DigitalHomePageComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        GraphqlClient.init(this)
    }

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        val sliceOpenApp = bundle?.getBoolean(RechargeHomepageActivity.RECHARGE_HOME_PAGE_EXTRA) ?: false
        return DigitalHomePageFragment.getInstance(sliceOpenApp)
    }

    override fun getScreenName(): String {
        return DIGITAL_HOMEPAGE_SCREEN_NAME
    }

    override fun getComponent(): DigitalHomePageComponent {
        if (!::digitalHomePageComponent.isInitialized) {
            digitalHomePageComponent = DigitalHomePageComponentInstance.getDigitalHomepageComponent(application)
        }
        return digitalHomePageComponent
    }

    override fun onBackPressed() {
        (fragment as DigitalHomePageFragment).onBackPressed()
        super.onBackPressed()
    }

    companion object {
        const val DIGITAL_HOMEPAGE_SCREEN_NAME = "/digital/subhomepage/topup"
        const val RECHARGE_HOME_PAGE_EXTRA = "RECHARGE_HOME_PAGE_EXTRA"

        fun getCallingIntent(context: Context): Intent = Intent(context, DigitalHomePageActivity::class.java)
    }
}