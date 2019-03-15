package com.tokopedia.applink.internal

/**
 * This class is used to store deeplink started with scheme "tokopedia-android-internal".
 * Since it is for android internal only, if the applink is shared between iOS or site,
 * please create with "tokopedia" scheme and put into different file.
 *
 *
 * To make this deeplnks work, These deeplinks must be registered in the manifest using intent filter
 * (see ProductDetailActivity manifest for example)
 */
object ApplinkConstInternal {

    const val INTERNAL_SCHEME = "tokopedia-android-internal"

    const val HOST_MARKETPLACE = "marketplace"

}
