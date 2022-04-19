package com.tokopedia.seller_migration_common.presentation.activity

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
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
import com.tokopedia.seller_migration_common.presentation.util.getRegisteredMigrationApplinks
import com.tokopedia.user.session.UserSession


class SellerMigrationActivity : BaseSimpleActivity() {

    companion object {
        fun createIntent(context: Context, @SellerMigrationFeatureName featureName: String, screenName: String, appLinks: ArrayList<String>): Intent {
            return RouteManager.getIntent(context, String.format("%s?${SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME}=%s", ApplinkConst.SELLER_MIGRATION, featureName)).apply {
                putExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, appLinks)
                putExtra(SellerMigrationApplinkConst.EXTRA_SCREEN_NAME, screenName)
                putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, featureName)
            }
        }
    }

    private var featureName: String = ""

    override fun getScreenName(): String = "/migration-page"

    override fun onCreate(savedInstanceState: Bundle?) {
        featureName = intent.extras?.getString(SellerMigrationFragment.KEY_PARAM_FEATURE_NAME)
                ?: intent.data?.getQueryParameter(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME).orEmpty()
        super.onCreate(savedInstanceState)
        processAppLink()
    }

    private fun processAppLink() {
        val openedPage = if (isSellerAppInstalled()) {
            val uri = intent.data
            if (uri != null) {
                val appLinks = ArrayList<String>(intent.extras?.getStringArrayList(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA)
                        ?: getRegisteredMigrationApplinks(featureName))

                if (appLinks.isNotEmpty()) {
                    val parameterizedAppLinks = appLinks.map {
                        Uri.parse(it).buildUpon()
                                .appendQueryParameter(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, featureName)
                                .toString()
                    }
                    val sellerHomeAppLink = Uri.parse(ApplinkConstInternalSellerapp.SELLER_HOME).buildUpon()
                            .appendQueryParameter(RouteManager.KEY_REDIRECT_TO_SELLER_APP, "true")
                            .appendQueryParameter(SellerMigrationApplinkConst.QUERY_PARAM_IS_AUTO_LOGIN, "true")
                            .toString()
                    val sellerHomeIntent = RouteManager.getIntent(this, sellerHomeAppLink).apply {
                        putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, ArrayList(parameterizedAppLinks))
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(sellerHomeIntent)
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