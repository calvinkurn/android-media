package com.tokopedia.gm.subscribe.tracking

import com.tokopedia.abstraction.AbstractionRouter

class GMTracking(private val router: AbstractionRouter){

    companion object {
        const val CLICK_HAMBURGER_MENU_EVENT = "clickHamburgerMenu"
        const val CLICK_HAMBURGER_MENU_CATEGORY = "hamburger icon"
        const val CLICK_HAMBURGER_MENU_ACTION = "click gold merchant"

        const val CLICK_GM_EVENT = "clickGoldMerchant"
        const val CLICK_GM_CATEGORY = "gold merchant"
        const val CLICK_GM_CANCEL_ACTION = "click produk unggulan - batal"
        const val CLICK_GM_SUBSCRIBE_ACTION = "click produk unggulan - berlangganan"

        const val CLICK_GM_PACKAGE_SUBSCRIBE_ACTION = "click paket berlangganan"
        const val CLICK_GM_PACKAGE_SUBSCRIBE_HOME_LABEL = "upgrade to gold merchant"
        const val CLICK_GM_PACKAGE_SUBSCRIBE_FEATURED_LABEL = "produk unggulan"
    }


    fun sendClickHamburgerMenuEvent(menuName: String){
    }

    fun sendClickManageProductDialogEvent(isSubscribing: Boolean){

    }

    fun sendClickGMSubscribingEvent(isFromFeatured: Boolean) {

    }
}