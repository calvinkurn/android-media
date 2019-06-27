package com.tokopedia.applink.constant

object DeeplinkConstant {
    const val SCHEME_HTTP = "http"
    const val SCHEME_TOKOPEDIA = "tokopedia"
    const val SCHEME_SELLERAPP = "sellerapp"

    const val SCHEME_TOKOPEDIA_SLASH = "$SCHEME_TOKOPEDIA://"

    /**
     * This constant is used to store deeplink started with scheme "tokopedia-android-internal".
     * Since it is for android internal only, if the applink is shared between iOS or site,
     * please create with "tokopedia" scheme and put into different file.
     *
     *
     * To make this deeplnks work, These deeplinks must be registered in the manifest using intent filter
     * (see ProductDetailActivity manifest for example)
     */
    const val SCHEME_INTERNAL = "tokopedia-android-internal"

}