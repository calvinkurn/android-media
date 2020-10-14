package com.tokopedia.seller.action.balance.presentation.slice

import android.app.PendingIntent
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.builders.*
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.action.R
import com.tokopedia.seller.action.balance.presentation.model.SellerActionBalance
import com.tokopedia.seller.action.common.presentation.slices.SellerSuccessSlice

class SellerBalanceSlice(context: Context,
                         sliceUri: Uri,
                         private val balanceList: List<SellerActionBalance>,
                         private val remoteConfig: FirebaseRemoteConfigImpl): SellerSuccessSlice<SellerActionBalance>(balanceList, context, sliceUri) {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun getSuccessSlice(): Slice =
            list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title = context.getString(R.string.seller_action_balance_title)
                    primaryAction = createHeaderPrimaryAction()
                }
                gridRow {
                    val defaultErrorString = context.getString(R.string.seller_action_balance_error_message)
                    var accountBalance: String? = null
                    var topAdsBalance: String? = null
                    balanceList.firstOrNull()?.let {
                        accountBalance = it.accountBalance
                        topAdsBalance = it.topAdsBalance
                    }
                    // Saldo
                    cell {
                        addImage(
                                IconCompat.createWithResource(context, R.drawable.ic_sellerapp_slice_balance),
                                ListBuilder.SMALL_IMAGE
                        )
                        addTitleText(context.getString(R.string.seller_action_balance_general))
                        addText(accountBalance ?: defaultErrorString)
                        contentIntent = createAccountBalancePendingIntent()
                    }
                    // Kredit TopAds
                    cell {
                        addImage(
                                IconCompat.createWithResource(context, R.drawable.ic_sellerapp_slice_topads),
                                ListBuilder.SMALL_IMAGE
                        )
                        addTitleText(context.getString(R.string.seller_action_balance_topads))
                        addText(topAdsBalance ?: defaultErrorString)
                        contentIntent = createTopAdsBalancePendingIntent()
                    }
                }
            }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createHeaderPrimaryAction(): SliceAction {
        return SliceAction.create(
                createHeaderPendingIntent(),
                IconCompat.createWithResource(context, R.drawable.ic_sellerapp_slice),
                ListBuilder.ICON_IMAGE,
                context.getString(R.string.seller_action_balance_title)
        )
    }

    private fun createHeaderPendingIntent(): PendingIntent {
        // TODO: Change to other setting applink
        RouteManager.getIntent(context, ApplinkConstInternalSellerapp.SELLER_HOME).let { intent ->
            return PendingIntent.getActivity(context, 0, intent, 0)
        }
    }

    private fun createAccountBalancePendingIntent(): PendingIntent {
        val intent =
                if (remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_SALDO_SPLIT_FOR_SELLER_APP, false))
                    RouteManager.getIntent(context, ApplinkConstInternalGlobal.SALDO_DEPOSIT)
                else {
                    RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, ApplinkConst.WebViewUrl.SALDO_DETAIL)
                }
        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    private fun createTopAdsBalancePendingIntent(): PendingIntent {
        RouteManager.getIntent(context, ApplinkConst.CustomerApp.TOPADS_DASHBOARD).let { intent ->
            return PendingIntent.getActivity(context, 0, intent, 0)
        }
    }
}