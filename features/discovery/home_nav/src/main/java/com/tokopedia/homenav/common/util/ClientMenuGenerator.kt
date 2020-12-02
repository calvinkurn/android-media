package com.tokopedia.homenav.common.util

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.base.viewmodel.HomeNavTickerViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.user.session.UserSessionInterface

class ClientMenuGenerator(val context: Context, val userSession: UserSessionInterface) {
    companion object {
        val ID_WISHLIST_MENU = 901
        val ID_FAVORITE_SHOP = 902
        val ID_RECENT_VIEW = 903
        val ID_SUBSCRIPTION = 904
        val ID_COMPLAIN = 905
        val ID_TOKOPEDIA_CARE = 906
        val ID_QR_CODE = 907
        val ID_ALL_TRANSACTION = 908
        val ID_TICKET = 909
        val ID_REVIEW = 910
        val ID_HOME = 911

        val ID_OPEN_SHOP_TICKER = 801
    }

    val APPLINK_MY_BILLS = "tokopedia://webview?url=https://www.tokopedia.com/mybills/"
    val APPLINK_COMPLAIN = "https://m.tokopedia.com/resolution-center/inbox/buyer/mobile"
    val APPLINK_TICKET = "tokopedia-android-internal://order/unified?filter=etiket"

    fun getMenu(menuId: Int, notifCount: String = "", sectionId: Int = 0): HomeNavMenuViewModel {
        when(menuId) {
            ID_WISHLIST_MENU -> return getWishlistUserMenu(notifCount, sectionId)
            ID_FAVORITE_SHOP -> return getFavoriteShopMenu(notifCount, sectionId)
            ID_RECENT_VIEW -> return getRecentViewMenu(notifCount, sectionId)
            ID_SUBSCRIPTION -> return getSubscriptionMenu(notifCount, sectionId)
            ID_COMPLAIN -> return getComplainMenu(notifCount, sectionId)
            ID_TOKOPEDIA_CARE -> return getTokopediaCareMenu(notifCount, sectionId)
            ID_QR_CODE -> return getQRCodeMenu(notifCount, sectionId)
            ID_ALL_TRANSACTION -> return getAllTransactionMenu(notifCount, sectionId)
            ID_TICKET -> return getTicketMenu(notifCount, sectionId)
            ID_REVIEW -> return getReviewMenu(notifCount, sectionId)
            ID_HOME -> return getHomeMenu(notifCount, sectionId)
        }
        return HomeNavMenuViewModel()
    }

    fun getTicker(menuId: Int): HomeNavTickerViewModel {
        when(menuId) {
            ID_OPEN_SHOP_TICKER -> return getOpenShopTicker()
        }
        return HomeNavTickerViewModel()
    }

    private fun getWishlistUserMenu(notifCount: String, sectionId: Int): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                trackerName = context.getString(R.string.menu_user_menu_wishlist_tracker_name),
                id = ID_WISHLIST_MENU,
                srcIconId = IconUnify.HEART,
                itemTitle = context.getString(R.string.menu_user_menu_wishlist),
                applink = ApplinkConst.NEW_WISHLIST.needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getFavoriteShopMenu(notifCount: String, sectionId: Int): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                trackerName = context.getString(R.string.menu_user_menu_favorite_shop_tracker_name),
                id = ID_FAVORITE_SHOP,
                srcIconId = IconUnify.SHOP_FAVORITE,
                itemTitle = context.getString(R.string.menu_user_menu_favorite_shop),
                applink = ApplinkConst.FAVORITE.needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getRecentViewMenu(notifCount: String, sectionId: Int): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                trackerName = context.getString(R.string.menu_user_menu_recent_view_tracker_name),
                id = ID_RECENT_VIEW,
                srcIconId = IconUnify.CLOCK,
                itemTitle = context.getString(R.string.menu_user_menu_recent_view),
                applink = ApplinkConst.RECENT_VIEW.needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getSubscriptionMenu(notifCount: String, sectionId: Int): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                trackerName = context.getString(R.string.menu_user_menu_subscription_tracker_name),
                id = ID_SUBSCRIPTION,
                srcIconId = IconUnify.BILL,
                itemTitle = context.getString(R.string.menu_user_menu_subscription),
                applink = APPLINK_MY_BILLS.needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getComplainMenu(notifCount: String, sectionId: Int): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                trackerName = context.getString(R.string.menu_user_menu_complain_tracker_name),
                id = ID_COMPLAIN,
                srcIconId = IconUnify.COMPLAINT,
                itemTitle = context.getString(R.string.menu_user_menu_complain),
                applink = APPLINK_COMPLAIN.needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getTokopediaCareMenu(notifCount: String, sectionId: Int): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                trackerName = context.getString(R.string.menu_user_menu_tokopedia_care_tracker_name),
                id = ID_TOKOPEDIA_CARE,
                srcIconId = IconUnify.CALL_CENTER,
                itemTitle = context.getString(R.string.menu_user_menu_tokopedia_care),
                applink = ApplinkConst.CONTACT_US_NATIVE.needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getQRCodeMenu(notifCount: String, sectionId: Int): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                trackerName = context.getString(R.string.menu_user_menu_qr_code_tracker_name),
                id = ID_QR_CODE,
                srcIconId = IconUnify.QR_CODE,
                itemTitle = context.getString(R.string.menu_user_menu_qr_code),
                applink = ApplinkConst.QRSCAN,
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getAllTransactionMenu(notifCount: String, sectionId: Int): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                id = ID_ALL_TRANSACTION,
                srcIconId = IconUnify.LIST_TRANSACTION,
                itemTitle = context.getString(R.string.menu_transaction_menu_all_transaction),
                applink = ApplinkConst.PURCHASE_ORDER.needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getTicketMenu(notifCount: String, sectionId: Int): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                id = ID_TICKET,
                srcIconId = IconUnify.TICKET_ACTIVE,
                itemTitle = context.getString(R.string.menu_transaction_menu_ticket),
                applink = APPLINK_TICKET.needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getReviewMenu(notifCount: String, sectionId: Int): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                id = ID_REVIEW,
                srcIconId = IconUnify.STAR,
                itemTitle = context.getString(R.string.menu_transaction_menu_review),
                applink = ApplinkConst.REPUTATION.needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getHomeMenu(notifCount: String, sectionId: Int): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                id = ID_HOME,
                srcIconId = IconUnify.HOME,
                itemTitle = context.getString(R.string.menu_home_back_to_home),
                applink = ApplinkConst.HOME,
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getOpenShopTicker(): HomeNavTickerViewModel {
        return HomeNavTickerViewModel(
                title = context.getString(R.string.menu_user_menu_shop_ticker_title),
                description = context.getString(R.string.menu_user_menu_shop_ticker_description),
                tickerType = Ticker.TYPE_ANNOUNCEMENT,
                applink = ApplinkConst.CREATE_SHOP.needLoginValidation()
        )
    }

    private fun String.needLoginValidation(): String {
        return if (userSession.isLoggedIn) {
            this
        } else {
            ApplinkConst.LOGIN
        }
    }
}