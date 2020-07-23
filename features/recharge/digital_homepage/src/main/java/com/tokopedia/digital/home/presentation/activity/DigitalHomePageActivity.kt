package com.tokopedia.digital.home.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.digital.home.di.DigitalHomePageComponent
import com.tokopedia.digital.home.di.DigitalHomePageComponentInstance
import com.tokopedia.digital.home.presentation.fragment.DigitalHomePageFragment
import com.tokopedia.graphql.data.GraphqlClient
import timber.log.Timber

/**
 * applink
 * tokopedia://recharge/home
 * or
 * RouteManager.route(this, ApplinkConst.DIGITAL_SUBHOMEPAGE_HOME)
 */

class DigitalHomePageActivity : BaseSimpleActivity(), HasComponent<DigitalHomePageComponent> {

    private lateinit var travelHomepageComponent: DigitalHomePageComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        handleTracking()
        GraphqlClient.init(this)
    }

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        val platformId = bundle?.getString(PARAM_PLATFORM_ID)?.toIntOrNull() ?: 0
        val enablePersonalize = bundle?.getBoolean(PARAM_ENABLE_PERSONALIZE, false) ?: false
        return DigitalHomePageFragment.newInstance(platformId, enablePersonalize)
    }

    override fun getScreenName(): String {
        return DIGITAL_HOMEPAGE_SCREEN_NAME
    }

    override fun getComponent(): DigitalHomePageComponent {
        if (!::travelHomepageComponent.isInitialized) {
            travelHomepageComponent = DigitalHomePageComponentInstance.getDigitalHomepageComponent(application)
        }
        return travelHomepageComponent
    }

    override fun onBackPressed() {
        (fragment as DigitalHomePageFragment).onBackPressed()
        super.onBackPressed()
    }

    companion object {
        const val PARAM_PLATFORM_ID = "platform_id"
        const val PARAM_ENABLE_PERSONALIZE = "personalize"

        const val DIGITAL_HOMEPAGE_SCREEN_NAME = "/digital/subhomepage/topup"
        const val RECHARGE_HOME_PAGE_EXTRA = "RECHARGE_HOME_PAGE_EXTRA"

        fun getCallingIntent(context: Context, platformID: String,
                             enablePersonalize: Boolean = false): Intent {
            val intent = Intent(context, DigitalHomePageActivity::class.java)
            intent.putExtra(PARAM_PLATFORM_ID, platformID)
            intent.putExtra(PARAM_ENABLE_PERSONALIZE, enablePersonalize)
            return intent
        }
    }


    /* This Method is use to tracking action click when user click open app in action
    */
    private fun handleTracking(){
        val trackingClick = intent.getBooleanExtra(RECHARGE_HOME_PAGE_EXTRA, false)
        if (trackingClick) {
            Timber.w("P2#ACTION_SLICE_CLICK_RECHARGE#DigitalHomepage")
        }
    }
}