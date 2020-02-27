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
        intent?.handleExtra()
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

        fun getCallingIntent(context: Context): Intent = Intent(context, DigitalHomePageActivity::class.java)
    }

    private fun Intent.handleIntent() {
        when (action) {
            Intent.ACTION_VIEW -> handleDeepLink(data)
            SearchIntents.ACTION_SEARCH -> {
            }
            else -> {
            }
        }
    }

    private fun Intent.handleExtra(){
        if(intent.data != null) {
            val trackingClick = intent.getStringExtra(RECHARGE_PRODUCT_EXTRA)
            Timber.d("P2#ACTION_SLICE_CLICK_RECHARGE#$trackingClick")
        }
    }

    private fun handleDeepLink(data: Uri?) {
        var actionHandled = true
        when (data?.path) {
            "/recharge/home" -> actionHandled = true
            else -> actionHandled = false

        }


        notifyActionSuccess(actionHandled)
    }

    private fun notifyActionSuccess(succeed: Boolean) {
        intent.getStringExtra(actionTokenExtra)?.let { actionToken ->
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