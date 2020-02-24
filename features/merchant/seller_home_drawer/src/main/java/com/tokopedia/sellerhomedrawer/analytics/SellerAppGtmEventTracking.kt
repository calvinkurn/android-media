package com.tokopedia.sellerhomedrawer.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object Event {
    const val USER_INTERACTION_HOMEPAGE = "userInteractionHomePage"
    const val SHOP_MANAGE = "clickManageShop"
    const val NAVIGATION_DRAWER = "clickNavigationDrawer"
    const val CLICK_GM = "clickGoldMerchant"
    const val CLICK_GM_STAT = "clickGMStat"
    const val CLICK_NAVIGATION_DRAWER = "clickNavigationDrawer"
    const val CLICK_HAMBURGER_MENU = "clickHamburgerMenu"
    const val SELLER_INFO = "clickSellerInfo"
}

object Category {
    const val CLICK_GM = "gold merchant"
    const val CLICK_HAMBURGER_MENU = "hamburger icon"
    const val FEATURED_PRODUCT = "Featured Product"
    const val HAMBURGER = "Hamburger Icon"
    const val SELLER_INFO_HOMEPAGE = "seller info-homepage"
}

object Action {
    const val CLICK = "Click"
    const val CLICK_BELI = "Click Beli"
    const val CLICK_GM_CANCEL = "click produk unggulan - batal"
    const val CLICK_GM_PACKAGE_SUBSCRIBE = "click paket berlangganan"
    const val CLICK_GM_SUBSCRIBE = "click produk unggulan - berlangganan"
    const val CLICK_HAMBURGER_MENU = "click gold merchant"
    const val CLICK_HAMBURGER_ICON = "click hamburger icon"
}

object EventLabel {
    const val SETTING = "Setting"
    const val BUY_GM = "Buy GM"
    const val CLICK_GM_PACKAGE_SUBSCRIBE_FEATURED = "produk unggulan"
    const val CLICK_GM_PACKAGE_SUBSCRIBE_HOME = "upgrade to gold merchant"
    const val DIGITAL_TRANSACTION_LIST = "Daftar Transaksi Digital"
    const val PAYMENT_AND_TOPUP = "Pembayaran dan Top Up"
    const val SELLER_HOME = "Home - SellerApp"
    const val NEW_ORDER = "New Order"
    const val DELIVERY_STATUS = "Delivery Status"
    const val DELIVERY_CONFIRMATION = "Delivery Confirmatio"
    const val SALES_LIST = "Sales List"
    const val PRODUCT_DISPLAY = "Product Display"
    const val STATISTIC = "Statistic"
    const val RESOLUTION_CENTER = "Resolution Center"
    const val DRAFT_PRODUCT = "Draft Product"
    const val TOPADS = "TopAds"
    const val FEATURED_PRODUCT = "Featured Product"
    const val SELLER_INFO = "seller info"
    const val SHOP_EN = "Shop"
    const val HELP = "Help"
    const val REVIEW = "Review"
    const val PRODUCT_DISCUSSION = "Product Discussion"
    const val MESSAGE = "Message"
    const val INBOX = "Inbox"
    const val SIGN_OUT = "Sign Out"

}

fun eventSellerInfo(eventAction: String, eventLabel: String) {
    sendGeneralEventGTM(
            Event.SELLER_INFO,
            Category.SELLER_INFO_HOMEPAGE,
            eventAction,
            eventLabel)
}

fun eventClickGoldMerchantViaDrawer() {
    sendGeneralEventGTM(
            Event.CLICK_NAVIGATION_DRAWER,
            Category.HAMBURGER,
            Action.CLICK,
            EventLabel.BUY_GM
    )
}

fun eventFeaturedProduct(eventLabel: String) {
    sendGeneralEventGTM(
            Event.SHOP_MANAGE,
            Category.FEATURED_PRODUCT,
            Action.CLICK,
            eventLabel
    )
}

fun eventDrawerClick(eventLabel: String) {
    sendGeneralEventGTM(
            Event.NAVIGATION_DRAWER,
            Category.HAMBURGER,
            Action.CLICK,
            EventLabel.PAYMENT_AND_TOPUP)
}

fun eventClickDigitalTransactionListOnDrawer() {
    sendGeneralEventGTM(
            Event.USER_INTERACTION_HOMEPAGE,
            Category.HAMBURGER,
            Action.CLICK,
            EventLabel.DIGITAL_TRANSACTION_LIST
    )
}

fun eventClickGMStat(eventCategory: String, eventLabel: String) {
    sendGeneralEventGTM(
            Event.CLICK_GM_STAT,
            eventCategory,
            Action.CLICK,
            eventLabel
    )
}

fun eventClickPaymentAndTopupOnDrawer() {
    sendGeneralEventGTM(
            Event.USER_INTERACTION_HOMEPAGE,
            Category.HAMBURGER,
            Action.CLICK,
            EventLabel.PAYMENT_AND_TOPUP
    )
}

fun sendClickHamburgerMenuEvent(menuName: String) {
    sendGeneralEventGTM(
            TrackAppUtils.gtmData(
                    Event.CLICK_HAMBURGER_MENU,
                    Category.CLICK_HAMBURGER_MENU,
                    "${Action.CLICK_HAMBURGER_MENU} - $menuName",
                    null))
}

fun sendClickManageProductDialogEvent(isSubscribing: Boolean) {
    sendGeneralEventGTM(TrackAppUtils.gtmData(
            Event.CLICK_GM,
            Category.CLICK_GM,
            if (isSubscribing) Action.CLICK_GM_SUBSCRIBE
            else Action.CLICK_GM_CANCEL,
            null))
}

private fun sendGeneralEventGTM(event: String,
                                category: String,
                                action: String,
                                label: String) {
    TrackApp.getInstance().gtm.sendGeneralEvent(event, category, action, label)
}

private fun sendGeneralEventGTM(value: Map<String, Any>) {
    TrackApp.getInstance().gtm.sendGeneralEvent(value)
}