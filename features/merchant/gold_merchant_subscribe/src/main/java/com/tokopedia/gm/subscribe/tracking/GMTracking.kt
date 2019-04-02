package com.tokopedia.gm.subscribe.tracking

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

class GMTracking(){

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
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(CLICK_HAMBURGER_MENU_EVENT,
                CLICK_HAMBURGER_MENU_CATEGORY, "$CLICK_HAMBURGER_MENU_ACTION - $menuName",
                null))
    }

    fun sendClickManageProductDialogEvent(isSubscribing: Boolean){
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(CLICK_GM_EVENT, CLICK_GM_CATEGORY,
                if (isSubscribing) CLICK_GM_SUBSCRIBE_ACTION else CLICK_GM_CANCEL_ACTION,null))
    }

    fun sendClickGMSubscribingEvent(isFromFeatured: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(CLICK_GM_EVENT, CLICK_GM_CATEGORY,
                CLICK_GM_PACKAGE_SUBSCRIBE_ACTION, if (isFromFeatured) CLICK_GM_PACKAGE_SUBSCRIBE_FEATURED_LABEL
        else CLICK_GM_PACKAGE_SUBSCRIBE_HOME_LABEL))
    }
}