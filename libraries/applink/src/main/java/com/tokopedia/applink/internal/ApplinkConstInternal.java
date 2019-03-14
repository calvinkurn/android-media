package com.tokopedia.applink.internal;

/**
 * This class is used to store deeplink started with scheme "tokopedia-android-internal".
 * Since it is for android internal only, if the applink is shared between iOS or site,
 * please create with "tokopedia" scheme and put into different file.
 * <p>
 * To make this deeplnks work, These deeplinks must be registered in the manifest using intent filter
 * (see ProductDetailActivity manifest for example)
 */
public class  ApplinkConstInternal {

    public static final String INTERNAL_SCHEME = "tokopedia-android-internal";

    public static final String HOST_MARKETPLACE = "marketplace";

    public static final String INTERNAL_MARKETPLACE = INTERNAL_SCHEME + "://" + HOST_MARKETPLACE;

    public static class Marketplace extends ApplinkConstInternalMarketplace {}

}
