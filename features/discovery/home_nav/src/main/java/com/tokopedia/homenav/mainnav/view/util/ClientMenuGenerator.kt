package com.tokopedia.homenav.mainnav.view.util

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.base.viewmodel.HomeNavTickerViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifycomponents.ticker.Ticker

class ClientMenuGenerator(val context: Context) {
    companion object {
        val ID_WISHLIST_MENU = 901
        val ID_FAVORITE_SHOP = 902
        val ID_RECENT_VIEW = 903
        val ID_SUBSCRIPTION = 904
        val ID_COMPLAIN = 905
        val ID_TOKOPEDIA_CARE = 906
        val ID_QR_CODE = 907

        val ID_OPEN_SHOP_TICKER = 801
    }

    val APPLINK_MY_BILLS = "tokopedia://mybills"
    val APPLINK_COMPLAIN = "https://m.tokopedia.com/resolution-center/inbox/buyer/mobile"

    fun getMenu(menuId: Int): HomeNavVisitable {
        when(menuId) {
            ID_WISHLIST_MENU -> return getWishlistUserMenu()
            ID_FAVORITE_SHOP -> return getFavoriteShopMenu()
            ID_RECENT_VIEW -> return getRecentViewMenu()
            ID_SUBSCRIPTION -> return getSubscriptionMenu()
            ID_COMPLAIN -> return getComplainMenu()
            ID_TOKOPEDIA_CARE -> return getTokopediaCareMenu()
            ID_QR_CODE -> return getQRCodeMenu()

            ID_OPEN_SHOP_TICKER -> return getOpenShopTicker()
        }
        return HomeNavMenuViewModel()
    }

    private fun getWishlistUserMenu(): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                id = ID_WISHLIST_MENU,
                srcIconId = IconUnify.HEART,
                itemTitle = context.getString(R.string.menu_user_menu_wishlist),
                applink = ApplinkConst.NEW_WISHLIST
        )
    }

    private fun getFavoriteShopMenu(): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                id = ID_FAVORITE_SHOP,
                srcIconId = IconUnify.SHOP_FAVORITE,
                itemTitle = context.getString(R.string.menu_user_menu_favorite_shop),
                applink = ApplinkConst.FAVORITE
        )
    }

    private fun getRecentViewMenu(): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                id = ID_RECENT_VIEW,
                srcIconId = IconUnify.CLOCK,
                itemTitle = context.getString(R.string.menu_user_menu_recent_view),
                applink = ApplinkConst.RECENT_VIEW
        )
    }

    private fun getSubscriptionMenu(): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                id = ID_SUBSCRIPTION,
                srcIconId = IconUnify.BILL,
                itemTitle = context.getString(R.string.menu_user_menu_subscription),
                applink = APPLINK_MY_BILLS
        )
    }

    private fun getComplainMenu(): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                id = ID_COMPLAIN,
                srcIconId = IconUnify.COMPLAINT,
                itemTitle = context.getString(R.string.menu_user_menu_complain),
                applink = APPLINK_COMPLAIN
        )
    }

    private fun getTokopediaCareMenu(): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                id = ID_TOKOPEDIA_CARE,
                srcIconId = IconUnify.CALL_CENTER,
                itemTitle = context.getString(R.string.menu_user_menu_tokopedia_care),
                applink = ApplinkConst.CONTACT_US_NATIVE
        )
    }

    private fun getQRCodeMenu(): HomeNavMenuViewModel {
        return HomeNavMenuViewModel(
                id = ID_QR_CODE,
                srcIconId = IconUnify.QR_CODE,
                itemTitle = context.getString(R.string.menu_user_menu_qr_code),
                applink = ApplinkConst.QRSCAN
        )
    }

    private fun getOpenShopTicker(): HomeNavTickerViewModel {
        return HomeNavTickerViewModel(
                title = context.getString(R.string.menu_user_menu_shop_ticker_title),
                description = context.getString(R.string.menu_user_menu_shop_ticker_description),
                tickerType = Ticker.TYPE_ANNOUNCEMENT,
                applink = ApplinkConst.CREATE_SHOP
        )
    }
}