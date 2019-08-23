package com.tokopedia.home.account.data.util

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.home.account.AccountConstants
import com.tokopedia.home.account.AccountHomeRouter
import com.tokopedia.home.account.AccountHomeUrl
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.viewmodel.*
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel
import com.tokopedia.remoteconfig.RemoteConfigKey
import rx.functions.Func1
import javax.inject.Inject

class BuyerEmptyMapper @Inject constructor(@ApplicationContext val context: Context) :
        Func1<Throwable, BuyerViewModel> {

    private val homeRouter: AccountHomeRouter = context.applicationContext as AccountHomeRouter

    override fun call(throwable: Throwable): BuyerViewModel {
        return getEmptyBuyerModel()
    }

    private fun getEmptyBuyerModel() : BuyerViewModel {
        val viewModel = BuyerViewModel()
        val viewItems = arrayListOf<ParcelableViewModel<*>>()

        viewItems.add(MenuTitleViewModel().apply {
            title = context.getString(R.string.title_menu_transaction)
        })

        viewItems.add(MenuListViewModel().apply {
            menu = context.getString(R.string.title_menu_waiting_for_payment)
            menuDescription = context.getString(R.string.label_menu_waiting_for_payment)
            count = 0
            applink = ApplinkConst.PMS
            titleTrack = AccountConstants.Analytics.PEMBELI
            sectionTrack = context.getString(R.string.title_menu_transaction)
        })

        viewItems.add(MenuGridViewModel().apply {
            title = context.getString(R.string.title_menu_other_transaction)
            items = getDigitalOrderMenu()
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

        if (homeRouter.getBooleanRemoteConfig(RemoteConfigKey.APP_ENABLE_INDI_CHALLENGES, true)) {
            viewItems.add(InfoCardViewModel().apply {
                iconRes = R.drawable.ic_challenge_trophy
                mainText = context.getString(R.string.title_menu_challenge)
                secondaryText = context.getString(R.string.label_menu_challenge)
                applink = ApplinkConst.CHALLENGE
                titleTrack = AccountConstants.Analytics.PEMBELI
                sectionTrack = AccountConstants.Analytics.CLICK_CHALLENGE
                isNewTxtVisiblle = if (homeRouter.getBooleanRemoteConfig(
                                RemoteConfigKey.APP_ENTRY_CHALLENGE_BARU,
                                true)
                ) View.VISIBLE else View.GONE
            })
        }

        viewItems.add(MenuTitleViewModel().apply {
            title = context.getString(R.string.title_menu_help)
        })

        viewItems.add(MenuListViewModel().apply {
            menu = context.getString(R.string.title_menu_resolution_center)
            menuDescription = context.getString(R.string.label_menu_resolution_center)
            applink = ApplinkConst.CONTACT_US_NATIVE
            titleTrack = AccountConstants.Analytics.PEMBELI
            sectionTrack = context.getString(R.string.title_menu_help)
        })

        viewModel.items = viewItems

        return viewModel
    }

    private fun getDigitalOrderMenu(): List<MenuGridItemViewModel> {
        val gridItems = arrayListOf<MenuGridItemViewModel>()
        val gridItem: MenuGridItemViewModel

        if (homeRouter.getBooleanRemoteConfig(RemoteConfigKey.APP_GLOBAL_NAV_NEW_DESIGN, true)) {
            gridItem = MenuGridItemViewModel(
                    R.drawable.ic_belanja,
                    context.getString(R.string.title_menu_market_place),
                    ApplinkConst.MARKETPLACE_ORDER,
                    0,
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            )
        } else {
            gridItem = MenuGridItemViewModel(
                    R.drawable.ic_belanja,
                    context.getString(R.string.title_menu_market_place),
                    ApplinkConst.PURCHASE_HISTORY,
                    0,
                    AccountConstants.Analytics.PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            )
        }
        gridItems.add(gridItem)

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
