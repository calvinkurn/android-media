package com.tokopedia.digital.home.presentation.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.gms.actions.SearchIntents
import com.google.firebase.appindexing.Action
import com.google.firebase.appindexing.FirebaseUserActions
import com.google.firebase.appindexing.builders.AssistActionBuilder
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
    private val actionTokenExtra = "actions.fulfillment.extra.ACTION_TOKEN"

    private lateinit var travelHomepageComponent: DigitalHomePageComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        intent?.handleIntent()
        GraphqlClient.init(this)
    }

    override fun getNewFragment(): Fragment = DigitalHomePageFragment.getInstance()

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
        const val DIGITAL_HOMEPAGE_SCREEN_NAME = "/digital/subhomepage/topup"
        const val RECHARGE_PRODUCT_EXTRA = "RECHARGE_PRODUCT_EXTRA"
        const val RECHARGE_USER_EXTRA = "RECHARGE_USER_EXTRA"
        const val RECHARGE_PATH_SLICE = "/recharge/home"

        fun getCallingIntent(context: Context): Intent = Intent(context, DigitalHomePageActivity::class.java)
    }

    /* This Method is use to handle intent from Action
   */
    private fun Intent.handleIntent() {
        when (action) {
            Intent.ACTION_VIEW -> handleDeepLink(data)
            SearchIntents.ACTION_SEARCH -> {
            }
            else -> {
            }
        }
    }

    /* This Method is use to tracking action click when user click open app in action
    */
    private fun handleTracking(){
            Timber.d("P2#ACTION_SLICE_CLICK_RECHARGE#DigitalHomepage")
    }


    /* This Method is use to checking checking the right path of deeplink
   */
    private fun handleDeepLink(data: Uri?) {
        var actionHandled = true
        when (data?.path) {
            RECHARGE_PATH_SLICE -> actionHandled = true
            else -> actionHandled = false

        }
        notifyActionSuccess(actionHandled)
    }

    /* This Method is use to send Action logging to FirebaseAppIndexing
   */
    private fun notifyActionSuccess(succeed: Boolean) {
        intent.getStringExtra(actionTokenExtra)?.let { actionToken ->
            handleTracking()
            val actionStatus = if (succeed) {
                Action.Builder.STATUS_TYPE_COMPLETED
            } else {
                Action.Builder.STATUS_TYPE_FAILED
            }
            val action = AssistActionBuilder()
                    .setActionToken(actionToken)
                    .setActionStatus(actionStatus)
                    .build()

            FirebaseUserActions.getInstance().end(action)
        }
    }
}