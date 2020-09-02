package com.tokopedia.seller_migration_common.presentation.activity

import android.app.TaskStackBuilder
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
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
import com.tokopedia.user.session.UserSession


class SellerMigrationActivity : BaseSimpleActivity() {

    companion object {
        fun createIntent(context: Context, @SellerMigrationFeatureName featureName: String, screenName: String, appLinks: ArrayList<String>,
                         isStackBuilder: Boolean = false): Intent {
            return RouteManager.getIntent(context, String.format("%s?${SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME}=%s", ApplinkConst.SELLER_MIGRATION, featureName)).apply {
                putExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, appLinks)
                putExtra(SellerMigrationApplinkConst.EXTRA_SCREEN_NAME, screenName)
                putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, featureName)
                putExtra(SellerMigrationApplinkConst.EXTRA_IS_STACK_BUILDER, isStackBuilder)
            }
        }
    }

    private var featureName: String = ""
    private var isStackBuilder: Boolean = false

    override fun getScreenName(): String = "/migration-page"

    override fun onCreate(savedInstanceState: Bundle?) {
        featureName = intent.extras?.getString(SellerMigrationFragment.KEY_PARAM_FEATURE_NAME).orEmpty()
        isStackBuilder = intent.extras?.getBoolean(SellerMigrationApplinkConst.EXTRA_IS_STACK_BUILDER, false) ?: false
        super.onCreate(savedInstanceState)
        processAppLink()
    }

    private fun processAppLink() {
        val openedPage = if (isSellerAppInstalled()) {
            val uri = intent.data
            if (uri != null) {
                val appLinks = ArrayList<String>(intent.extras?.getStringArrayList(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA).orEmpty())
                if (appLinks.isNotEmpty()) {
                    val firstAppLink = appLinks.firstOrNull().orEmpty()

                    val parameterizedFirstAppLink = Uri.parse(firstAppLink)
                            .buildUpon()
                            .appendQueryParameter(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, featureName)
                            .build()
                            .toString()
                    val sellerAppSeamlessLoginAppLink = Uri.parse(ApplinkConstInternalGlobal.SEAMLESS_LOGIN).buildUpon()
                            .appendQueryParameter(ApplinkConstInternalGlobal.KEY_REDIRECT_SEAMLESS_APPLINK, parameterizedFirstAppLink)
                            .appendQueryParameter(RouteManager.KEY_REDIRECT_TO_SELLER_APP, "true")
                            .appendQueryParameter(SellerMigrationApplinkConst.QUERY_PARAM_IS_AUTO_LOGIN, "true")
                            .build()
                            .toString()
                    appLinks.removeAt(0)

                    val seamLessLoginIntent = RouteManager.getIntent(this, sellerAppSeamlessLoginAppLink)?.apply {
                        putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, appLinks)
                        putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, featureName)
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }

                    if(isStackBuilder) {
                        if(appLinks.size == 1) {
                            val taskStackBuilder = TaskStackBuilder.create(this)
                            val secondAppLink = appLinks.firstOrNull().orEmpty()
                            val secondIntent = RouteManager.getIntent(this, secondAppLink)
                            taskStackBuilder.addNextIntent(seamLessLoginIntent)
                            taskStackBuilder.addNextIntent(secondIntent)
                            taskStackBuilder.startActivities()
                        }
                    } else {
                        startActivity(seamLessLoginIntent)
                    }
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