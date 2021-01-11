package com.tokopedia.seller.menu.common.view.mapper

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.common.constant.PermissionId
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import javax.inject.Inject

class AdminPermissionMapper @Inject constructor(
        private val context: Context,
        private val remoteConfig: RemoteConfig) {

    companion object {
        private const val GO_TO_BUYER_REVIEW = "GO_TO_BUYER_REVIEW"
        private const val GO_TO_MY_PRODUCT = "GO_TO_MY_PRODUCT"
        private const val APPLINK_FORMAT = "%s?url=%s%s"
    }

    fun mapFeatureToPermissionList(@AdminFeature adminFeature: String): List<String> {
        return when(adminFeature) {
            AdminFeature.SALDO -> listOf(PermissionId.MANAGE_FINANCE)
            AdminFeature.NEW_ORDER -> listOf(PermissionId.MANAGE_ORDER)
            AdminFeature.READY_TO_SHIP_ORDER -> listOf(PermissionId.MANAGE_ORDER)
            AdminFeature.MANAGE_PRODUCT -> listOf(PermissionId.MANAGE_PRODUCT)
            AdminFeature.ADD_PRODUCT -> listOf(PermissionId.MANAGE_PRODUCT)
            AdminFeature.REVIEW -> listOf(PermissionId.REPLY_REVIEW)
            AdminFeature.DISCUSSION -> listOf(PermissionId.REPLY_DISCUSSION)
            AdminFeature.COMPLAINT -> listOf(PermissionId.RESPOND_COMPLAINTS)
            AdminFeature.MANAGE_SHOP -> listOf(PermissionId.MANAGE_SHOP)
            else -> listOf()
        }
    }

    fun mapFeatureToDestination(@AdminFeature adminFeature: String): Intent? {
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
            else -> null
        }
    }

}