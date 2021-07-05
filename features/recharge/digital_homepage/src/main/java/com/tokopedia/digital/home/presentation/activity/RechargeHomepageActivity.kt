package com.tokopedia.digital.home.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.digital.home.di.RechargeHomepageComponent
import com.tokopedia.digital.home.di.RechargeHomepageComponentInstance
import com.tokopedia.digital.home.presentation.fragment.RechargeHomepageFragment
import com.tokopedia.graphql.data.GraphqlClient
import timber.log.Timber

/**
 * applink
 * tokopedia://recharge/home
 * or
 * RouteManager.route(this, ApplinkConst.DIGITAL_SUBHOMEPAGE_HOME)
 */

class RechargeHomepageActivity : BaseSimpleActivity(), HasComponent<RechargeHomepageComponent> {

    private lateinit var travelHomepageComponent: RechargeHomepageComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        GraphqlClient.init(this)
    }

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        val platformId = bundle?.getString(PARAM_PLATFORM_ID)?.toIntOrNull() ?: 0
        val enablePersonalize = (bundle?.getString(PARAM_ENABLE_PERSONALIZE) ?: "true").toBoolean()
        val sliceOpenApp = bundle?.getBoolean(RECHARGE_HOME_PAGE_EXTRA) ?: false
        return RechargeHomepageFragment.newInstance(platformId, enablePersonalize, sliceOpenApp)
    }

    override fun getScreenName(): String {
        return DIGITAL_HOMEPAGE_SCREEN_NAME
    }

    override fun getComponent(): RechargeHomepageComponent {
        if (!::travelHomepageComponent.isInitialized) {
            travelHomepageComponent = RechargeHomepageComponentInstance.getRechargeHomepageComponent(application)
        }
        return travelHomepageComponent
    }

    override fun onBackPressed() {
        if(fragment != null){
            val rechargeFragment = (fragment as com.tokopedia.digital.home.presentation.fragment.RechargeHomepageFragment)
            rechargeFragment.onBackPressed()
        }
        super.onBackPressed()
    }

    companion object {
        const val PARAM_PLATFORM_ID = "platform_id"
        const val PARAM_ENABLE_PERSONALIZE = "personalize"

        const val DIGITAL_HOMEPAGE_SCREEN_NAME = "/digital/subhomepage/topup"
        const val RECHARGE_HOME_PAGE_EXTRA = "RECHARGE_HOME_PAGE_EXTRA"

        fun getCallingIntent(context: Context, platformID: String,
                             enablePersonalize: Boolean = true): Intent {
            val intent = Intent(context, RechargeHomepageActivity::class.java)
            intent.putExtra(PARAM_PLATFORM_ID, platformID)
            intent.putExtra(PARAM_ENABLE_PERSONALIZE, enablePersonalize)
            return intent
        }
    }
}