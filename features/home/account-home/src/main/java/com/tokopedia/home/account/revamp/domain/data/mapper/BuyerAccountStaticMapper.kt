package com.tokopedia.home.account.revamp.domain.data.mapper

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.home.account.AccountConstants
import com.tokopedia.home.account.AccountHomeUrl
import com.tokopedia.home.account.R
import com.tokopedia.home.account.data.util.RESCENTER_BUYER
import com.tokopedia.home.account.presentation.viewmodel.*
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel
import com.tokopedia.home.account.revamp.domain.data.model.AccountDataModel
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class BuyerAccountStaticMapper @Inject constructor(
        private val context: Context,
        private val remoteConfig: RemoteConfig,
        private val userSession: UserSession
) {

    fun getStaticBuyerModel(useUoh: Boolean): BuyerViewModel {
        val items: ArrayList<ParcelableViewModel<*>> = ArrayList()

        val model = BuyerViewModel()
        items.add(getBuyerProfile())
        items.addAll(getStaticModel(useUoh))
        model.items = items

        return model
    }

    private fun getBuyerProfile(): BuyerCardViewModel {
        val buyerCardViewModel = BuyerCardViewModel()

        buyerCardViewModel.apply {
            userId = userSession.userId
            name = userSession.name

            shopName = userSession.shopName.toEmptyStringIfNull()
            isHasShop = userSession.hasShop()
            imageUrl = userSession.profilePicture.toEmptyStringIfNull()
        }

        return buyerCardViewModel
    }

    private fun getStaticModel(isUseUoh: Boolean): List<ParcelableViewModel<*>> {
        val accountDataModel = AccountDataModel()

        val viewItems = arrayListOf<ParcelableViewModel<*>>()

        viewItems.add(MenuTitleViewModel().apply {
            title = context.getString(R.string.title_menu_transaction)
        })

        if (isUseUoh) {
            viewItems.add(MenuGridIconNotificationViewModel().apply {
                items = getUohMenu(context, accountDataModel)
            })

            viewItems.add(MenuListViewModel().apply {
                val menuEtiketUohTitle = accountDataModel.uohOrderCount.activeTicketsText
                menu = if(menuEtiketUohTitle.isNullOrEmpty()) {
                    AccountConstants.TITLE_UOH_ETICKET
                } else menuEtiketUohTitle
                menuDescription = context.getString(R.string.e_ticket_desc)
                count = accountDataModel.uohOrderCount.activeTickets.toIntOrZero()
                applink = ApplinkConstInternalOrder.UNIFY_ORDER_STATUS.replace(ApplinkConstInternalOrder.PARAM_CUSTOM_FILTER, ApplinkConstInternalOrder.PARAM_E_TIKET)
                titleTrack = AccountConstants.Analytics.PEMBELI
                sectionTrack = context.getString(R.string.title_menu_transaction)
            })
        } else {
            viewItems.add(MenuListViewModel().apply {
                menu = context.getString(R.string.title_menu_waiting_for_payment)
                menuDescription = context.getString(R.string.label_menu_waiting_for_payment)
                val paymentStatus = accountDataModel.notifications.buyerOrder?.paymentStatus?: "0"
                count = if(paymentStatus.isNotEmpty()) { paymentStatus.toInt(10) } else { 0 }
                applink = ApplinkConst.PMS
                titleTrack = AccountConstants.Analytics.PEMBELI
                sectionTrack = context.getString(R.string.title_menu_transaction)
            })

            viewItems.add(MenuGridViewModel().apply {
                title = context.getString(R.string.title_menu_other_transaction)
                items = getDigitalOrderMenu(context, remoteConfig)
            })
        }

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
            count = accountDataModel.notifications.resolution?.buyer ?: 0
            applink = RESCENTER_BUYER
            titleTrack = AccountConstants.Analytics.PEMBELI
            sectionTrack = context.getString(R.string.title_menu_transaction)
        })

        viewItems.add(MenuTitleViewModel().apply {
            title = context.getString(R.string.title_menu_favorites)
        })

        if (remoteConfig.getBoolean("mainapp_enable_interest_pick", true)) {
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
            applink = if(!remoteConfig.getBoolean(RemoteConfigKey.ENABLE_NEW_WISHLIST_PAGE, true)) ApplinkConst.WISHLIST else ApplinkConst.NEW_WISHLIST
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
                    true -> ApplinkConst.BELANJA_ORDER
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

    private fun getUohMenu(context: Context, accountDataModel: AccountDataModel?): ArrayList<MenuGridIconNotificationItemViewModel> {
        val gridItems = arrayListOf<MenuGridIconNotificationItemViewModel>()

        accountDataModel?.notifications?.buyerOrder?.paymentStatus?.toIntOrZero()?.let {
            MenuGridIconNotificationItemViewModel(
                    R.drawable.ic_uoh_menunggu_pembayaran,
                    AccountConstants.TITLE_UOH_MENUNGGU_PEMBAYARAN,
                    ApplinkConst.PMS,
                    it,
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            )
        }?.let { gridItems.add(it) }

        gridItems.add(MenuGridIconNotificationItemViewModel(
                R.drawable.ic_uoh_belanja,
                AccountConstants.TITLE_UOH_TRANSAKSI_BERLANGSUNG,
                ApplinkConstInternalOrder.UNIFY_ORDER_STATUS.replace(ApplinkConstInternalOrder.PARAM_CUSTOM_FILTER, ApplinkConstInternalOrder.PARAM_DALAM_PROSES),
                accountDataModel?.uohOrderCount?.sedangBerlangsung.toIntOrZero(),
                AccountConstants.Analytics.PEMBELI,
                context.getString(R.string.title_menu_transaction)
        ))

        gridItems.add(MenuGridIconNotificationItemViewModel(
                R.drawable.ic_uoh_all_transactions,
                AccountConstants.TITLE_UOH_SEMUA_TRANSAKSI,
                ApplinkConstInternalOrder.UNIFY_ORDER_STATUS.replace(ApplinkConstInternalOrder.PARAM_CUSTOM_FILTER, ApplinkConstInternalOrder.PARAM_SEMUA_TRANSAKSI),
                0,
                AccountConstants.Analytics.PEMBELI,
                context.getString(R.string.title_menu_transaction)
        ))

        return gridItems
    }
}