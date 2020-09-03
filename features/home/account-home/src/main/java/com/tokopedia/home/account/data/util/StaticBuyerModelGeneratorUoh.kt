package com.tokopedia.home.account.data.util

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.UnifyOrder.*
import com.tokopedia.home.account.AccountConstants
import com.tokopedia.home.account.AccountHomeUrl
import com.tokopedia.home.account.R
import com.tokopedia.home.account.data.model.AccountModel
import com.tokopedia.home.account.presentation.viewmodel.*
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel
import com.tokopedia.home.account.revamp.domain.data.model.AccountDataModel
import com.tokopedia.navigation_common.model.UohCounterModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
const val RESCENTER_BUYER_UOH = "https://m.tokopedia.com/resolution-center/inbox/buyer"
class StaticBuyerModelGeneratorUoh private constructor() {

    /*companion object {
        fun getModel(context: Context, accountModel: AccountDataModel?, remoteConfig: RemoteConfig, uohOrderCount: Uoh): List<ParcelableViewModel<*>> {
            val viewItems = arrayListOf<ParcelableViewModel<*>>()

            viewItems.add(MenuTitleViewModel().apply {
                title = context.getString(R.string.title_menu_transaction)
            })

            viewItems.add(MenuGridIconNotificationViewModel().apply {
                setItems(uohOrderCount?.let { getDigitalOrderMenu(context, accountModel, it) })
            })

            viewItems.add(MenuListViewModel().apply {
                menu = uohOrderCount?.activeTicketsText
                menuDescription = context.getString(R.string.e_ticket_desc)
                uohOrderCount?.activeTickets?.let {
                    count = it.toInt()
                }
                applink = UNIFY_ORDER_STATUS.replace(PARAM_CUSTOM_FILTER, PARAM_E_TIKET)
                titleTrack = AccountConstants.Analytics.PEMBELI
                sectionTrack = context.getString(R.string.title_menu_transaction)
            })

            viewItems.add(MenuListViewModel().apply {
                menu = context.getString(R.string.ulasan)
                menuDescription = context.getString(R.string.ulasan_desc)
                applink = ApplinkConst.REPUTATION
                titleTrack = AccountConstants.Analytics.PEMBELI
                sectionTrack = context.getString(R.string.title_menu_transaction)
            })

            viewItems.add(MenuListViewModel().apply {
                menu = context.getString(R.string.title_menu_buyer_complain)
                menuDescription = context.getString(R.string.label_menu_buyer_complain)
                count = accountModel?.notifications?.resolution?.buyer ?: 0
                applink = RESCENTER_BUYER_UOH
                titleTrack = AccountConstants.Analytics.PEMBELI
                sectionTrack = context.getString(R.string.title_menu_transaction)
            })

            viewItems.add(MenuTitleViewModel().apply {
                title = context.getString(R.string.title_menu_favorites)
            })

            viewItems.add(MenuListViewModel().apply {
                menu = context.getString(R.string.title_menu_favorite_topic)
                menuDescription = context.getString(R.string.label_menu_favorite_topic)
                applink = ApplinkConst.INTEREST_PICK
                titleTrack = AccountConstants.Analytics.PEMBELI
                sectionTrack = context.getString(R.string.title_menu_favorites)
            })

            viewItems.add(MenuListViewModel().apply {
                menu = context.getString(R.string.title_menu_last_seen)
                menuDescription = context.getString(R.string.label_menu_last_seen)
                applink = ApplinkConst.RECENT_VIEW
                titleTrack = AccountConstants.Analytics.PEMBELI
                sectionTrack = context.getString(R.string.title_menu_favorites)
            })

            viewItems.add(MenuListViewModel().apply {
                menu = context.getString(R.string.title_menu_wishlist)
                menuDescription = context.getString(R.string.label_menu_wishlist)
                applink = if(!remoteConfig.getBoolean(RemoteConfigKey.ENABLE_NEW_WISHLIST_PAGE, true))ApplinkConst.WISHLIST else ApplinkConst.NEW_WISHLIST
                titleTrack = AccountConstants.Analytics.PEMBELI
                sectionTrack = context.getString(R.string.title_menu_favorites)
            })

            viewItems.add(MenuListViewModel().apply {
                menu = context.getString(R.string.title_menu_favorite_shops)
                menuDescription = context.getString(R.string.label_menu_favorite_shops)
                applink = ApplinkConst.FAVORITE
                titleTrack = AccountConstants.Analytics.PEMBELI
                sectionTrack = context.getString(R.string.title_menu_favorites)
            })

            viewItems.add(MenuListViewModel().apply {
                menu = context.getString(R.string.title_menu_mybills)
                menuDescription = context.getString(R.string.label_menu_mybills)
                applink = "${ApplinkConst.WEBVIEW}?url=${AccountHomeUrl.Pulsa.MYBILLS}"
                titleTrack = AccountConstants.Analytics.PEMBELI
                sectionTrack = context.getString(R.string.title_menu_mybills)
            })

            viewItems.add(MenuTitleViewModel().apply {
                title = context.getString(R.string.tokopedia_care)
            })

            viewItems.add(MenuListViewModel().apply {
                menu = context.getString(R.string.title_menu_resolution_center)
                menuDescription = context.getString(R.string.label_menu_resolution_center)
                applink = ApplinkConst.CONTACT_US_NATIVE
                titleTrack = AccountConstants.Analytics.PEMBELI
                sectionTrack = context.getString(R.string.title_menu_help)
                isUseSeparator = false
            })

            return viewItems
        }

        private fun getPurchaseOrderMenu(
                context: Context,
                accountModel: AccountModel?
        ): List<MenuGridItemViewModel> {
            val gridItems = arrayListOf<MenuGridItemViewModel>()

            gridItems.add(MenuGridItemViewModel(
                    R.drawable.ic_waiting_for_confirmation,
                    context.getString(R.string.label_menu_waiting_confirmation),
                    ApplinkConst.PURCHASE_CONFIRMED,
                    accountModel?.notifications?.buyerOrder?.confirmed ?: 0,
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            ))

            gridItems.add(MenuGridItemViewModel(
                    R.drawable.ic_order_processed,
                    context.getString(R.string.label_menu_order_processed),
                    ApplinkConst.PURCHASE_PROCESSED,
                    accountModel?.notifications?.buyerOrder?.processed ?: 0,
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            ))

            gridItems.add(MenuGridItemViewModel(
                    R.drawable.ic_shipped,
                    context.getString(R.string.label_menu_shipping),
                    ApplinkConst.PURCHASE_SHIPPED,
                    accountModel?.notifications?.buyerOrder?.shipped ?: 0,
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            ))

            gridItems.add(MenuGridItemViewModel(
                    R.drawable.ic_delivered,
                    context.getString(R.string.label_menu_delivered),
                    ApplinkConst.PURCHASE_DELIVERED,
                    accountModel?.notifications?.buyerOrder?.arriveAtDestination ?: 0,
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            ))

            return gridItems
        }

        private fun getMarketPlaceOrderMenu(
                context: Context,
                accountModel: AccountModel?
        ): List<MenuGridItemViewModel> {
            val gridItems = arrayListOf<MenuGridItemViewModel>()

            gridItems.add(MenuGridItemViewModel(
                    R.drawable.ic_waiting_for_confirmation,
                    context.getString(R.string.label_menu_waiting_confirmation),
                    ApplinkConst.MARKETPLACE_WAITING_CONFIRMATION,
                    accountModel?.notifications?.buyerOrder?.confirmed ?: 0,
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            ))

            gridItems.add(MenuGridItemViewModel(
                    R.drawable.ic_order_processed,
                    context.getString(R.string.label_menu_order_processed),
                    ApplinkConst.MARKETPLACE_ORDER_PROCESSED,
                    accountModel?.notifications?.buyerOrder?.processed ?: 0,
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            ))

            gridItems.add(MenuGridItemViewModel(
                    R.drawable.ic_shipped,
                    context.getString(R.string.label_menu_shipping),
                    ApplinkConst.MARKETPLACE_SENT,
                    accountModel?.notifications?.buyerOrder?.shipped ?: 0,
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            ))

            gridItems.add(MenuGridItemViewModel(
                    R.drawable.ic_delivered,
                    context.getString(R.string.label_menu_delivered),
                    ApplinkConst.MARKETPLACE_DELIVERED,
                    accountModel?.notifications?.buyerOrder?.arriveAtDestination ?: 0,
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            ))

            return gridItems
        }

        private fun getDigitalOrderMenu(
                context: Context, accountModel: AccountDataModel?, uohOrderCount: UohCounterModel.UohOrderCount
        ): List<MenuGridIconNotificationItemViewModel> {
            val gridItems = arrayListOf<MenuGridIconNotificationItemViewModel>()

            gridItems.add(MenuGridIconNotificationItemViewModel(
                    R.drawable.ic_uoh_menunggu_pembayaran,
                    context.getString(R.string.title_uoh_1),
                    ApplinkConst.PMS,
                    accountModel?.notifications?.buyerOrder?.paymentStatus?.toInt(10) ?: 0,
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            ))

            gridItems.add(MenuGridIconNotificationItemViewModel(
                    R.drawable.ic_uoh_belanja,
                    uohOrderCount.onProcessText,
                    UNIFY_ORDER_STATUS.replace(PARAM_CUSTOM_FILTER, PARAM_DALAM_PROSES),
                    uohOrderCount.onProcess.toInt(),
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            ))

            gridItems.add(MenuGridIconNotificationItemViewModel(
                    R.drawable.ic_uoh_all_transactions,
                    context.getString(R.string.title_uoh_3),
                    UNIFY_ORDER_STATUS.replace(PARAM_CUSTOM_FILTER, PARAM_SEMUA_TRANSAKSI),
                    0,
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            ))

            return gridItems
        }
    }*/
}
