package com.tokopedia.home.account.data.util

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.home.account.AccountConstants
import com.tokopedia.home.account.AccountHomeUrl
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.viewmodel.*
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel
import com.tokopedia.home.account.revamp.domain.data.model.AccountDataModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import java.lang.Boolean

const val RESCENTER_BUYER = "https://m.tokopedia.com/resolution-center/inbox/buyer"
class StaticBuyerModelGenerator private constructor() {

    companion object {
        fun getModel(context: Context, accountDataModel: AccountDataModel?, remoteConfig: RemoteConfig): List<ParcelableViewModel<*>> {
            val viewItems = arrayListOf<ParcelableViewModel<*>>()

            viewItems.add(MenuTitleViewModel().apply {
                title = context.getString(R.string.title_menu_transaction)
            })

            viewItems.add(MenuListViewModel().apply {
                menu = context.getString(R.string.title_menu_waiting_for_payment)
                menuDescription = context.getString(R.string.label_menu_waiting_for_payment)
                val paymentStatus = accountDataModel?.notifications?.buyerOrder?.paymentStatus?: "0"
                count = if(paymentStatus.isNotEmpty()) { paymentStatus.toInt(10) } else { 0 }
                applink = ApplinkConst.PMS
                titleTrack = AccountConstants.Analytics.PEMBELI
                sectionTrack = context.getString(R.string.title_menu_transaction)
            })

            viewItems.add(MenuGridViewModel().apply {
                title = context.getString(R.string.title_menu_other_transaction)
                items = getDigitalOrderMenu(context, remoteConfig)
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
                count = accountDataModel?.notifications?.resolution?.buyer ?: 0
                applink = RESCENTER_BUYER
                titleTrack = AccountConstants.Analytics.PEMBELI
                sectionTrack = context.getString(R.string.title_menu_transaction)
            })

            viewItems.add(MenuTitleViewModel().apply {
                title = context.getString(R.string.title_menu_favorites)
            })

            if (remoteConfig.getBoolean("mainapp_enable_interest_pick", Boolean.TRUE)) {
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

            if (remoteConfig.getBoolean(RemoteConfigKey.APP_SHOW_REFERRAL_BUTTON, false)) {
                viewItems.add(InfoCardViewModel().apply {
                    iconRes = R.drawable.ic_tokocash_big
                    mainText = remoteConfig.getString(
                            RemoteConfigKey.APP_REFERRAL_TITLE,
                            context.getString(R.string.title_menu_wallet_referral)
                    )
                    secondaryText = remoteConfig.getString(
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

        private fun getDigitalOrderMenu(
                context: Context,
                remoteConfig: RemoteConfig
        ): List<MenuGridItemViewModel> {
            val gridItems = arrayListOf<MenuGridItemViewModel>()

            gridItems.add(MenuGridItemViewModel(
                    R.drawable.ic_belanja,
                    context.getString(R.string.title_menu_market_place),
                    when (remoteConfig.getBoolean(RemoteConfigKey.APP_GLOBAL_NAV_NEW_DESIGN, true)) {
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
