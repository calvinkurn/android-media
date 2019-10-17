package com.tokopedia.home.account.data.util

import android.content.Context
import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.home.account.AccountConstants
import com.tokopedia.home.account.AccountHomeRouter
import com.tokopedia.home.account.AccountHomeUrl
import com.tokopedia.home.account.R
import com.tokopedia.home.account.data.model.AccountModel
import com.tokopedia.home.account.presentation.viewmodel.InfoCardViewModel
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel
import com.tokopedia.home.account.presentation.viewmodel.MenuTitleViewModel
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel
import com.tokopedia.remoteconfig.RemoteConfigKey

class StaticBuyerModelGenerator private constructor() {

    companion object {
        fun getModel(context: Context, accountModel: AccountModel?): List<ParcelableViewModel<*>> {
            val homeRouter: AccountHomeRouter = context.applicationContext as AccountHomeRouter
            val viewItems = arrayListOf<ParcelableViewModel<*>>()

            viewItems.add(MenuTitleViewModel().apply {
                title = context.getString(R.string.title_menu_transaction)
            })

            viewItems.add(MenuListViewModel().apply {
                menu = context.getString(R.string.title_menu_waiting_for_payment)
                menuDescription = context.getString(R.string.label_menu_waiting_for_payment)
                count = accountModel?.notifications?.buyerOrder?.paymentStatus?.toInt(10) ?: 0
                applink = ApplinkConst.PMS
                titleTrack = AccountConstants.Analytics.PEMBELI
                sectionTrack = context.getString(R.string.title_menu_transaction)
            })

            MenuGridViewModel().apply {
                title = context.getString(R.string.title_menu_shopping_transaction)
                linkText = context.getString(R.string.label_menu_show_history)
                titleTrack = AccountConstants.Analytics.PEMBELI
                sectionTrack = context.getString(R.string.title_menu_transaction)
                applinkUrl = ApplinkConst.MARKETPLACE_ORDER
                items = when (homeRouter.getBooleanRemoteConfig(RemoteConfigKey.APP_GLOBAL_NAV_NEW_DESIGN, true)) {
                    true -> getMarketPlaceOrderMenu(context, accountModel)
                    else -> getPurchaseOrderMenu(context, accountModel)
                }
            }

            viewItems.add(MenuGridViewModel().apply {
                title = context.getString(R.string.title_menu_other_transaction)
                items = getDigitalOrderMenu(context, homeRouter)
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
                applink = ApplinkConst.RESCENTER_BUYER
                titleTrack = AccountConstants.Analytics.PEMBELI
                sectionTrack = context.getString(R.string.title_menu_transaction)
            })

            viewItems.add(MenuTitleViewModel().apply {
                title = context.getString(R.string.title_menu_favorites)
            })

            if (homeRouter.isEnableInterestPick) {
                viewItems.add(MenuListViewModel().apply {
                    menu = context.getString(R.string.title_menu_favorite_topic)
                    menuDescription = context.getString(R.string.label_menu_favorite_topic)
                    applink = ApplinkConst.INTEREST_PICK
                    titleTrack = AccountConstants.Analytics.PEMBELI
                    sectionTrack = context.getString(R.string.title_menu_favorites)
                })
            }

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
                applink = ApplinkConst.WISHLIST
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

            if (homeRouter.getBooleanRemoteConfig(RemoteConfigKey.APP_SHOW_REFERRAL_BUTTON, false)) {
                viewItems.add(InfoCardViewModel().apply {
                    iconRes = R.drawable.ic_tokocash_big
                    mainText = homeRouter.getStringRemoteConfig(
                            RemoteConfigKey.APP_REFERRAL_TITLE,
                            context.getString(R.string.title_menu_wallet_referral)
                    )
                    secondaryText = homeRouter.getStringRemoteConfig(
                            RemoteConfigKey.APP_REFERRAL_SUBTITLE,
                            context.getString(R.string.label_menu_wallet_referral)
                    )
                    applink = ApplinkConst.REFERRAL
                    titleTrack = AccountConstants.Analytics.PEMBELI
                    sectionTrack = context.getString(R.string.title_menu_wallet_referral)
                })
            }

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
                context: Context,
                homeRouter: AccountHomeRouter
        ): List<MenuGridItemViewModel> {
            val gridItems = arrayListOf<MenuGridItemViewModel>()

            gridItems.add(MenuGridItemViewModel(
                    R.drawable.ic_belanja,
                    context.getString(R.string.title_menu_market_place),
                    when (homeRouter.getBooleanRemoteConfig(RemoteConfigKey.APP_GLOBAL_NAV_NEW_DESIGN, true)) {
                        true -> ApplinkConst.MARKETPLACE_ORDER
                        else -> ApplinkConst.PURCHASE_HISTORY
                    },
                    0,
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            ))

            gridItems.add(MenuGridItemViewModel(
                    R.drawable.ic_top_up_bill,
                    context.getString(R.string.title_menu_top_up_bill),
                    ApplinkConst.DIGITAL_ORDER,
                    0,
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            ))

            gridItems.add(MenuGridItemViewModel(
                    R.drawable.ic_flight,
                    context.getString(R.string.title_menu_flight),
                    ApplinkConst.FLIGHT_ORDER,
                    0,
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            ))

            gridItems.add(MenuGridItemViewModel(
                    R.drawable.ic_see_all,
                    context.getString(R.string.title_menu_show_all),
                    AccountConstants.Navigation.SEE_ALL,
                    0,
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            ))

            return gridItems
        }
    }
}
