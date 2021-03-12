package com.tokopedia.seller.menu.presentation.util

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

class AdminPermissionMapper @Inject constructor(private val userSession: UserSessionInterface) {

    companion object {
        private const val GO_TO_BUYER_REVIEW = "GO_TO_BUYER_REVIEW"
        private const val GO_TO_MY_PRODUCT = "GO_TO_MY_PRODUCT"
        private const val APPLINK_FORMAT = "%s?url=%s%s"

        private const val SCREEN_NAME = "MA - Akun Toko"
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
            else -> 0
        }
    }

    fun mapFeatureToDestination(context: Context, @AdminFeature adminFeature: String): Intent? {
        return when (adminFeature) {
            AdminFeature.SHOP_SCORE -> {
                RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_SCORE_DETAIL, userSession.shopId)
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
                RouteManager.getIntent(context, ApplinkConst.REPUTATION).apply {
                    putExtra(GO_TO_BUYER_REVIEW, true)
                }
            }
            AdminFeature.DISCUSSION -> {
                RouteManager.getIntent(context, ApplinkConst.TALK).apply {
                    putExtra(GO_TO_MY_PRODUCT, true)
                }
            }
            AdminFeature.COMPLAINT -> {
                String.format(APPLINK_FORMAT, ApplinkConst.WEBVIEW,
                        SellerBaseUrl.HOSTNAME, SellerBaseUrl.RESO_INBOX_SELLER).let { resolutionInboxApplink ->
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
            else -> return ""
        }.let { stringResId ->
            return context?.getString(stringResId).orEmpty()
        }
    }

    private fun getSellerMigrationIntent(context: Context,
                                         @SellerMigrationFeatureName featureName: String,
                                         appLinks: ArrayList<String>): Intent =
            SellerMigrationActivity.createIntent(context, featureName, SCREEN_NAME, appLinks)

}