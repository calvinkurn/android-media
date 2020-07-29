package com.tokopedia.seller_migration_common.presentation.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTracking
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.EVENT_CATEGORY_MIGRATION_PAGE
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.EVENT_LABEL_TO_SELLER_APP
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.USER_REDIRECTION_BUSINESS_UNIT
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.USER_REDIRECTION_EVENT_ACTION
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.USER_REDIRECTION_EVENT_CATEGORY
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.USER_REDIRECTION_EVENT_NAME
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.seller_migration_common.presentation.fragment.SellerMigrationFragment
import com.tokopedia.user.session.UserSession


class SellerMigrationActivity : BaseSimpleActivity() {

    private var featureName: String = ""

    override fun getScreenName(): String = "/migration-page"

    override fun onCreate(savedInstanceState: Bundle?) {
        featureName = intent.data?.getQueryParameter(SellerMigrationFragment.KEY_PARAM_FEATURE_NAME).orEmpty()
        super.onCreate(savedInstanceState)
        processAppLink()
    }

    private fun processAppLink() {
        val openedPage = if (isSellerAppInstalled()) {
            val uri = intent.data
            if (uri != null) {
                val parameterizedFirstAppLink = Uri.parse(this.intent.getStringExtra(SellerMigrationApplinkConst.QUERY_PARAM_SELLER_MIGRATION_FIRST_APPLINK_EXTRA).orEmpty())
                        .buildUpon()
                        .appendQueryParameter(RouteManager.KEY_REDIRECT_TO_SELLER_APP, "true")
                        .appendQueryParameter(SellerMigrationApplinkConst.QUERY_PARAM_IS_AUTO_LOGIN, "true")
                        .appendQueryParameter(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, featureName)
                        .build()
                        .toString()
                RouteManager.getIntent(this, parameterizedFirstAppLink)?.apply {
                    putExtra(SellerMigrationApplinkConst.QUERY_PARAM_SELLER_MIGRATION_SECOND_APPLINK_EXTRA,
                            this@SellerMigrationActivity.intent.getStringExtra(SellerMigrationApplinkConst.QUERY_PARAM_SELLER_MIGRATION_SECOND_APPLINK_EXTRA).orEmpty())
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(this)
                    finish()
                }
            }
            EVENT_LABEL_TO_SELLER_APP
        } else {
            SellerMigrationTracking.trackOpenScreenSellerMigration(screenName)
            EVENT_CATEGORY_MIGRATION_PAGE
        }

        trackUserRedirection(openedPage)
    }

    private fun trackUserRedirection(openedPage: String) {
        val userSession = UserSession(this)
        val screenName = intent.extras?.getString(SellerMigrationApplinkConst.EXTRA_SCREEN_NAME).orEmpty()
        SellerMigrationTracking.eventUserRedirection(
                eventName = USER_REDIRECTION_EVENT_NAME[featureName].orEmpty(),
                eventCategory = USER_REDIRECTION_EVENT_CATEGORY[featureName].orEmpty(),
                eventAction = USER_REDIRECTION_EVENT_ACTION[featureName].orEmpty(),
                eventLabel = "${SellerMigrationTrackingConstants.USER_REDIRECTION_EVENT_LABEL[featureName].orEmpty()}$openedPage",
                screenName = screenName,
                userId = userSession.userId,
                bu = USER_REDIRECTION_BUSINESS_UNIT[featureName].orEmpty()
        )
    }

    private fun isSellerAppInstalled(): Boolean {
        with(SellerMigrationConstants) {
            return try {
                packageManager?.getLaunchIntentForPackage(PACKAGE_SELLER_APP) != null
            } catch (anfe: ActivityNotFoundException) {
                false
            }
        }
    }

    override fun getNewFragment(): Fragment {
        return SellerMigrationFragment.createInstance(featureName)
    }
}