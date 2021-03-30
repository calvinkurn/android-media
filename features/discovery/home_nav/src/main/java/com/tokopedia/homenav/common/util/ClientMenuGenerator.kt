package com.tokopedia.homenav.common.util

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTickerDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.user.session.UserSessionInterface

class ClientMenuGenerator(val context: Context, val userSession: UserSessionInterface) {
    companion object {
        const val ID_WISHLIST_MENU = 901
        const val ID_FAVORITE_SHOP = 902
        const val ID_RECENT_VIEW = 903
        const val ID_SUBSCRIPTION = 904
        const val ID_COMPLAIN = 905
        const val ID_TOKOPEDIA_CARE = 906
        const val ID_QR_CODE = 907
        const val ID_ALL_TRANSACTION = 908
        const val ID_TICKET = 909
        const val ID_REVIEW = 910
        const val ID_HOME = 911

        const val ID_OPEN_SHOP_TICKER = 801
        const val PAGE_SOURCE_KEY = "pageSource"
        const val PAGE_SOURCE = "home side nav"

        const val APPLINK_MY_BILLS = "tokopedia://webview?url=https://www.tokopedia.com/mybills/"
        const val APPLINK_COMPLAIN = "https://m.tokopedia.com/resolution-center/inbox/buyer/mobile"
        const val APPLINK_TICKET = "tokopedia-android-internal://order/unified?filter=etiket"

        const val IDENTIFIER_TITLE_MY_ACTIVITY = 100
        const val IDENTIFIER_TITLE_ALL_CATEGORIES = 101
        const val IDENTIFIER_TITLE_HELP_CENTER = 102
    }


    fun getMenu(menuId: Int, notifCount: String = "", sectionId: Int = 0): HomeNavMenuDataModel {
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
        return HomeNavMenuDataModel()
    }

    fun getTicker(menuId: Int): HomeNavTickerDataModel {
        when(menuId) {
            ID_OPEN_SHOP_TICKER -> return getOpenShopTicker()
        }
        return HomeNavTickerDataModel()
    }

    fun getSectionTitle(identifier: Int): HomeNavTitleDataModel {
        return HomeNavTitleDataModel(
                identifier = identifier,
                title = when (identifier) {
                    IDENTIFIER_TITLE_MY_ACTIVITY -> context.getString(R.string.title_transaction_section)
                    IDENTIFIER_TITLE_ALL_CATEGORIES -> context.getString(R.string.title_category_section)
                    IDENTIFIER_TITLE_HELP_CENTER -> context.getString(R.string.title_helpcenter_section)
                    else -> ""
                }
        )
    }

    private fun getWishlistUserMenu(notifCount: String, sectionId: Int): HomeNavMenuDataModel {
        return HomeNavMenuDataModel(
                trackerName = context.getString(R.string.menu_user_menu_wishlist_tracker_name),
                id = ID_WISHLIST_MENU,
                srcIconId = IconUnify.HEART,
                itemTitle = context.getString(R.string.menu_user_menu_wishlist),
                applink = ApplinkConst.NEW_WISHLIST.needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getFavoriteShopMenu(notifCount: String, sectionId: Int): HomeNavMenuDataModel {
        return HomeNavMenuDataModel(
                trackerName = context.getString(R.string.menu_user_menu_favorite_shop_tracker_name),
                id = ID_FAVORITE_SHOP,
                srcIconId = IconUnify.SHOP_FAVORITE,
                itemTitle = context.getString(R.string.menu_user_menu_favorite_shop),
                applink = ApplinkConst.FAVORITE.needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getRecentViewMenu(notifCount: String, sectionId: Int): HomeNavMenuDataModel {
        return HomeNavMenuDataModel(
                trackerName = context.getString(R.string.menu_user_menu_recent_view_tracker_name),
                id = ID_RECENT_VIEW,
                srcIconId = IconUnify.CLOCK,
                itemTitle = context.getString(R.string.menu_user_menu_recent_view),
                applink = ApplinkConst.RECENT_VIEW.needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getSubscriptionMenu(notifCount: String, sectionId: Int): HomeNavMenuDataModel {
        return HomeNavMenuDataModel(
                trackerName = context.getString(R.string.menu_user_menu_subscription_tracker_name),
                id = ID_SUBSCRIPTION,
                srcIconId = IconUnify.BILL,
                itemTitle = context.getString(R.string.menu_user_menu_subscription),
                applink = APPLINK_MY_BILLS.needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getComplainMenu(notifCount: String, sectionId: Int): HomeNavMenuDataModel {
        return HomeNavMenuDataModel(
                trackerName = context.getString(R.string.menu_user_menu_complain_tracker_name),
                id = ID_COMPLAIN,
                srcIconId = IconUnify.COMPLAINT,
                itemTitle = context.getString(R.string.menu_user_menu_complain),
                applink = APPLINK_COMPLAIN.needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getTokopediaCareMenu(notifCount: String, sectionId: Int): HomeNavMenuDataModel {
        return HomeNavMenuDataModel(
                trackerName = context.getString(R.string.menu_user_menu_tokopedia_care_tracker_name),
                id = ID_TOKOPEDIA_CARE,
                srcIconId = IconUnify.CALL_CENTER,
                itemTitle = context.getString(R.string.menu_user_menu_tokopedia_care),
                applink = ApplinkConst.CONTACT_US_NATIVE.needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getQRCodeMenu(notifCount: String, sectionId: Int): HomeNavMenuDataModel {
        return HomeNavMenuDataModel(
                trackerName = context.getString(R.string.menu_user_menu_qr_code_tracker_name),
                id = ID_QR_CODE,
                srcIconId = IconUnify.QR_CODE,
                itemTitle = context.getString(R.string.menu_user_menu_qr_code),
                applink = ApplinkConst.QRSCAN,
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getAllTransactionMenu(notifCount: String, sectionId: Int): HomeNavMenuDataModel {
        return HomeNavMenuDataModel(
                id = ID_ALL_TRANSACTION,
                srcIconId = IconUnify.LIST_TRANSACTION,
                itemTitle = context.getString(R.string.menu_transaction_menu_all_transaction),
                applink = ApplinkConst.PURCHASE_ORDER.needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getTicketMenu(notifCount: String, sectionId: Int): HomeNavMenuDataModel {
        return HomeNavMenuDataModel(
                id = ID_TICKET,
                srcIconId = IconUnify.TICKET_ACTIVE,
                itemTitle = context.getString(R.string.menu_transaction_menu_ticket),
                applink = APPLINK_TICKET.needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getReviewMenu(notifCount: String, sectionId: Int): HomeNavMenuDataModel {
        return HomeNavMenuDataModel(
                id = ID_REVIEW,
                srcIconId = IconUnify.STAR,
                itemTitle = context.getString(R.string.menu_transaction_menu_review),
                applink = getReputationApplink().needLoginValidation(),
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getHomeMenu(notifCount: String, sectionId: Int): HomeNavMenuDataModel {
        return HomeNavMenuDataModel(
                id = ID_HOME,
                srcIconId = IconUnify.HOME,
                itemTitle = context.getString(R.string.menu_home_back_to_home),
                applink = ApplinkConst.HOME,
                notifCount = notifCount,
                sectionId = sectionId
        )
    }

    private fun getOpenShopTicker(): HomeNavTickerDataModel {
        return HomeNavTickerDataModel(
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

    private fun getReputationApplink(): String {
        return Uri.parse(ApplinkConst.REPUTATION).buildUpon().appendQueryParameter(PAGE_SOURCE_KEY, PAGE_SOURCE).build().toString()
    }
}