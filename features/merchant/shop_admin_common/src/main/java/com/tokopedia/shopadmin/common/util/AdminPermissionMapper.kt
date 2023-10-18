package com.tokopedia.shopadmin.common.util

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.review.ReviewApplinkConst
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.shopadmin.common.R
import com.tokopedia.url.TokopediaUrl
import java.util.*
import javax.inject.Inject

class AdminPermissionMapper @Inject constructor() {

    companion object {
        private const val GO_TO_MY_PRODUCT = "GO_TO_MY_PRODUCT"
        private const val APPLINK_FORMAT = "%s?url=%s%s"

        private const val RESO_INBOX_SELLER = "resolution-center/inbox/seller/mobile"
    }

    fun mapFeatureToAccessId(@AdminFeature adminFeature: String): Int {
        return when(adminFeature) {
            AdminFeature.SHOP_SCORE -> AccessId.SHOP_SCORE
            AdminFeature.NEW_ORDER, AdminFeature.READY_TO_SHIP_ORDER, AdminFeature.ORDER_HISTORY -> AccessId.SOM_LIST
            AdminFeature.MANAGE_PRODUCT -> AccessId.PRODUCT_LIST
            AdminFeature.ADD_PRODUCT -> AccessId.PRODUCT_ADD
            AdminFeature.REVIEW -> AccessId.REVIEW
            AdminFeature.DISCUSSION -> AccessId.DISCUSSION
            AdminFeature.COMPLAINT -> AccessId.COMPLAINT
            AdminFeature.MANAGE_SHOP -> AccessId.SHOP_SETTING_INFO
            AdminFeature.STATISTIC -> AccessId.STATISTIC
            AdminFeature.ADS_AND_PROMOTION -> AccessId.ADS_AND_PROMO
            AdminFeature.SHOP_SETTINGS_INFO -> AccessId.SHOP_SETTING_INFO
            AdminFeature.SHOP_SETTINGS_NOTES -> AccessId.SHOP_SETTING_NOTES
            AdminFeature.SHOP_OPERATIONAL_HOURS -> AccessId.SHOP_SETTING_INFO
            AdminFeature.SHOP_SETTING_ADDR -> AccessId.SHOP_SETTING_ADDRESS
            AdminFeature.SHIPPING_EDITOR -> AccessId.SHOP_SETTING_SHIPMENT
            else -> 0
        }
    }

    fun mapFeatureToDestination(context: Context, @AdminFeature adminFeature: String): Intent? {
        return when (adminFeature) {
            AdminFeature.SHOP_SCORE -> {
                RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_PERFORMANCE)
            }
            AdminFeature.NEW_ORDER -> {
                RouteManager.getIntent(context, ApplinkConst.SELLER_NEW_ORDER)
            }
            AdminFeature.READY_TO_SHIP_ORDER -> {
                RouteManager.getIntent(context, ApplinkConst.SELLER_PURCHASE_READY_TO_SHIP)
            }
            AdminFeature.ORDER_HISTORY -> {
                RouteManager.getIntent(context, ApplinkConst.SELLER_HISTORY)
            }
            AdminFeature.MANAGE_PRODUCT -> {
                RouteManager.getIntent(context, ApplinkConst.PRODUCT_MANAGE)
            }
            AdminFeature.ADD_PRODUCT -> {
                RouteManager.getIntent(context, ApplinkConst.PRODUCT_ADD)
            }
            AdminFeature.REVIEW -> {
                getReputationIntent(context)
            }
            AdminFeature.DISCUSSION -> {
                RouteManager.getIntent(context, ApplinkConst.TALK).apply {
                    putExtra(GO_TO_MY_PRODUCT, true)
                }
            }
            AdminFeature.COMPLAINT -> {
                String.format(
                    APPLINK_FORMAT, ApplinkConst.WEBVIEW, TokopediaUrl.getInstance().SELLER, RESO_INBOX_SELLER).let { resolutionInboxApplink ->
                    RouteManager.getIntent(context, resolutionInboxApplink)
                }
            }
            AdminFeature.MANAGE_SHOP -> {
                RouteManager.getIntent(context, ApplinkConstInternalSellerapp.SELLER_SETTINGS)
            }
            AdminFeature.STATISTIC -> {
                val appLinks = ArrayList<String>().apply {
                    add(ApplinkConstInternalSellerapp.SELLER_HOME)
                    add(ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD)
                }
                getSellerMigrationIntent(context, SellerMigrationFeatureName.FEATURE_SHOP_INSIGHT, appLinks)
            }
            AdminFeature.ADS_AND_PROMOTION -> {
                val appLinks = ArrayList<String>().apply {
                    add(ApplinkConstInternalSellerapp.SELLER_HOME)
                    add(ApplinkConstInternalSellerapp.CENTRALIZED_PROMO)
                }
                getSellerMigrationIntent(context, SellerMigrationFeatureName.FEATURE_CENTRALIZED_PROMO, appLinks)
            }
            AdminFeature.SHOP_SETTINGS_INFO -> {
                RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO)
            }
            AdminFeature.SHOP_SETTINGS_NOTES -> {
                RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES)
            }
            AdminFeature.SHOP_OPERATIONAL_HOURS -> {
                RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_SETTINGS_OPERATIONAL_HOURS)
            }
            AdminFeature.SHOP_SETTING_ADDR -> {
                RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS)
            }
            AdminFeature.SHIPPING_EDITOR -> {
                RouteManager.getIntent(context, ApplinkConst.SELLER_SHIPPING_EDITOR)
            }
            else -> null
        }
    }

    fun mapFeatureToToolbarTitle(context: Context?, @AdminFeature adminFeature: String): String {
        when(adminFeature) {
            AdminFeature.SALDO -> R.string.admin_title_balance
            AdminFeature.SHOP_SCORE -> R.string.admin_title_shop_score
            AdminFeature.NEW_ORDER, AdminFeature.READY_TO_SHIP_ORDER, AdminFeature.ORDER_HISTORY -> R.string.admin_title_order
            AdminFeature.MANAGE_PRODUCT -> R.string.admin_title_product_list
            AdminFeature.ADD_PRODUCT -> R.string.admin_title_product_add
            AdminFeature.REVIEW -> R.string.admin_title_review
            AdminFeature.DISCUSSION -> R.string.admin_title_discussion
            AdminFeature.COMPLAINT -> R.string.admin_title_complaint
            AdminFeature.MANAGE_SHOP -> R.string.admin_title_shop_setting
            AdminFeature.STATISTIC -> R.string.admin_title_statistic
            AdminFeature.ADS_AND_PROMOTION -> R.string.admin_title_ads_and_promotion
            AdminFeature.SHOP_SETTINGS_INFO -> R.string.admin_title_shop_settings_info
            AdminFeature.SHOP_SETTINGS_NOTES -> R.string.admin_title_shop_settings_notes
            AdminFeature.SHOP_OPERATIONAL_HOURS -> R.string.admin_title_shop_operational_hours
            AdminFeature.SHOP_SETTING_ADDR -> R.string.admin_title_shop_settings_address
            AdminFeature.SHIPPING_EDITOR -> R.string.admin_title_shop_shipping_editor
            else -> return String.EMPTY
        }.let { stringResId ->
            return context?.getString(stringResId).orEmpty()
        }
    }

    private fun getSellerMigrationIntent(context: Context,
                                         @SellerMigrationFeatureName featureName: String,
                                         appLinks: ArrayList<String>): Intent =
            RouteManager.getIntent(
                context,
                String.format(
                    Locale.getDefault(),
                    "%s?${SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME}=%s",
                    ApplinkConst.SELLER_MIGRATION,
                    featureName
                )
            ).apply {
                putStringArrayListExtra(
                    SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA,
                    appLinks
                )
            }

    private fun getReputationIntent(context: Context): Intent {
        val appLink = UriUtil.buildUriAppendParam(
            ApplinkConst.REPUTATION,
            mapOf(ReviewApplinkConst.PARAM_TAB to ReviewApplinkConst.SELLER_TAB)
        )
        return RouteManager.getIntent(context, appLink)
    }

}
