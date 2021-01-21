package com.tokopedia.seller.menu.common.view.mapper

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.common.constant.PermissionId
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.common.view.viewholder.SellerFeatureViewHolder
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.shop.common.constant.AccessId
import javax.inject.Inject

class AdminPermissionMapper @Inject constructor(private val remoteConfig: RemoteConfig) {

    companion object {
        private const val GO_TO_BUYER_REVIEW = "GO_TO_BUYER_REVIEW"
        private const val GO_TO_MY_PRODUCT = "GO_TO_MY_PRODUCT"
        private const val APPLINK_FORMAT = "%s?url=%s%s"

        private const val SCREEN_NAME = "MA - Akun Toko"
    }

    // TODO: Delete soon, check first with product to make sure these are unused
    fun mapFeatureToPermissionList(@AdminFeature adminFeature: String): List<String> {
        return when(adminFeature) {
            AdminFeature.SALDO -> listOf(PermissionId.MANAGE_FINANCE)
            AdminFeature.NEW_ORDER -> listOf(PermissionId.MANAGE_ORDER)
            AdminFeature.READY_TO_SHIP_ORDER -> listOf(PermissionId.MANAGE_ORDER)
            AdminFeature.ORDER_HISTORY -> listOf(PermissionId.MANAGE_ORDER)
            AdminFeature.MANAGE_PRODUCT -> listOf(PermissionId.MANAGE_PRODUCT)
            AdminFeature.ADD_PRODUCT -> listOf(PermissionId.MANAGE_PRODUCT)
            AdminFeature.REVIEW -> listOf(PermissionId.REPLY_REVIEW)
            AdminFeature.DISCUSSION -> listOf(PermissionId.REPLY_DISCUSSION)
            AdminFeature.COMPLAINT -> listOf(PermissionId.RESPOND_COMPLAINTS)
            AdminFeature.MANAGE_SHOP -> listOf(PermissionId.MANAGE_SHOP)
            else -> listOf()
        }
    }

    fun mapFeatureToAccessId(@AdminFeature adminFeature: String): Int {
        return when(adminFeature) {
            AdminFeature.SALDO -> AccessId.EDIT_STOCK
            AdminFeature.NEW_ORDER -> AccessId.SOM
            AdminFeature.READY_TO_SHIP_ORDER -> AccessId.SOM
            AdminFeature.ORDER_HISTORY -> AccessId.SOM
            AdminFeature.MANAGE_PRODUCT -> AccessId.PRODUCT_LIST
            AdminFeature.ADD_PRODUCT -> AccessId.PRODUCT_ADD
            AdminFeature.REVIEW -> AccessId.REVIEW
            AdminFeature.DISCUSSION -> AccessId.DISCUSSION
            AdminFeature.COMPLAINT -> AccessId.COMPLAINT
            AdminFeature.MANAGE_SHOP -> AccessId.SHOP_SETTING
            AdminFeature.STATISTIC -> AccessId.STATISTIC
            AdminFeature.ADS_AND_PROMOTION -> AccessId.ADS_AND_PROMO
            else -> 0
        }
    }

    fun mapFeatureToDestination(context: Context, @AdminFeature adminFeature: String): Intent? {
        return when (adminFeature) {
            AdminFeature.SALDO -> {
                if (remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_SALDO_SPLIT_FOR_SELLER_APP, false))
                    RouteManager.getIntent(context, ApplinkConstInternalGlobal.SALDO_DEPOSIT)
                else {
                    RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, ApplinkConst.WebViewUrl.SALDO_DETAIL)
                }
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

    private fun getSellerMigrationIntent(context: Context,
                                         @SellerMigrationFeatureName featureName: String,
                                         appLinks: ArrayList<String>): Intent =
            SellerMigrationActivity.createIntent(context, featureName, SCREEN_NAME, appLinks)

}