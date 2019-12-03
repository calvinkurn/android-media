package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://s".
 * This Class Store Salam Deeplink, btw 's' is stand for salam, we got that from
 * Only create "tokopedia-android-internal://" if this deeplink is used only for android app, and not shared to iOs and web.
 * If the deeplink is shared between iOS and web, it should use "tokopedia://" scheme.
 */

object ApplinkConstInternalSalam{
    @JvmField
    val PARAM_UMROH = "umroh"
    /**
     * Host Salam
     */
    @JvmField
    val HOST_SALAM = "s"

    @JvmField
    val HOST_SALAM_ORDER_DETAIL = "order-details"

    /**
     * INTERNAL PATH Salam
     */
    @JvmField
    val INTERNAL_SALAM_ORDER_DETAIL = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_SALAM_ORDER_DETAIL}"

    @JvmField
    val INTERNAL_SALAM = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_SALAM}"

    /**
     * PATH Salam
     */
    // UmrahOrderDetailActivity
    // tokopedia-android-internal://order-details/umroh/.*\\
    @JvmField
    val SALAM_ORDER_DETAIL = "$INTERNAL_SALAM_ORDER_DETAIL/$PARAM_UMROH/.*\\"

    // UmrahHomepageActivity
    // tokopedia-android-internal://s/umroh
    @JvmField
    val SALAM_UMRAH_HOME_PAGE = "$INTERNAL_SALAM/$PARAM_UMROH"

    // UmrahSearchActivity
    // tokopedia-android-internal://s/umroh/search
    @JvmField
    val SALAM_UMRAH_SEARCH = "$INTERNAL_SALAM/$PARAM_UMROH/search"

    // UmrahSearchActivity
    // tokopedia-android-internal://s/umroh/paket/.*\
    @JvmField
    val SALAM_UMRAH_PACKET = "$INTERNAL_SALAM/$PARAM_UMROH/paket/.*\\"

    // UmrahPDPActivity
    // tokopedia-android-internal://s/umroh/produk/.*\
    @JvmField
    val SALAM_UMRAH_PRODUCT = "$INTERNAL_SALAM/$PARAM_UMROH/produk/.*\\"

    // UmrahCheckoutActivity
    // tokopedia-android-internal://s/umroh/checkout
    @JvmField
    val SALAM_UMRAH_CHECKOUT = "$INTERNAL_SALAM/$PARAM_UMROH/checkout"


}