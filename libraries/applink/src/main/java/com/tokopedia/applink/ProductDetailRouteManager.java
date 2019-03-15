package com.tokopedia.applink;

import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;

import com.tokopedia.applink.internal.ApplinkConstInternal;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;

/**
 * This class is temporary solution to route Product Detail Page to the old PDP by remote config
 */
public class ProductDetailRouteManager {
    private static RemoteConfig remoteConfig;
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int TYPE_PRODUCT_DETAIL = 1;
    private static final int TYPE_PRODUCT_DETAIL_DOMAIN = 2;

    static {
        sURIMatcher.addURI(ApplinkConstInternalMarketplace.HOST_MARKETPLACE, "product/*/", TYPE_PRODUCT_DETAIL);
        sURIMatcher.addURI(ApplinkConstInternalMarketplace.HOST_MARKETPLACE, "product/*/*/", TYPE_PRODUCT_DETAIL_DOMAIN);
    }

    private static RemoteConfig getRemoteConfig(Context context) {
        if (remoteConfig == null) {
            remoteConfig = new FirebaseRemoteConfigImpl(context.getApplicationContext());
        }
        return remoteConfig;
    }

    public static boolean isGoToOldProductDetail(Context context) {
        return getRemoteConfig(context).getBoolean(RemoteConfigKey.MAIN_APP_DISABLE_NEW_PRODUCT_DETAIL);
    }

    public static Intent getProductIntent(Context context, String productApplink) {
        if (isGoToOldProductDetail(context)) {
            Intent intent = new Intent();
            intent.setClassName(context.getPackageName(), "com.tokopedia.tkpdpdp.ProductInfoActivity");

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                Uri uri = Uri.parse(productApplink);
                int uriMatcherType = sURIMatcher.match(uri);
                if (uriMatcherType > 0) {
                    if (uriMatcherType == TYPE_PRODUCT_DETAIL) {
                        intent.putExtra("product_id", uri.getLastPathSegment());
                    } else if (uriMatcherType == TYPE_PRODUCT_DETAIL_DOMAIN) {
                        int size = uri.getPathSegments().size();
                        intent.putExtra("shop_domain", uri.getPathSegments().get(size - 2));
                        intent.putExtra("product_key", uri.getPathSegments().get(size - 1));
                    }
                    intent.putExtra("tracker_attribution", uri.getQueryParameter("tracker_attribution"));
                    intent.putExtra("tracker_list_name", uri.getQueryParameter("tracker_list_name"));
                    return intent;
                } else {
                    return RouteManager.buildInternalUri(context, productApplink);
                }

            } else {
                return RouteManager.buildInternalUri(context, productApplink);
            }
        } else {
            return RouteManager.buildInternalUri(context, productApplink);
        }
    }

    static boolean isProductApplink(String applink) {
        Uri uri = Uri.parse(applink);
        return (ApplinkConstInternal.INTERNAL_SCHEME.equals(uri.getScheme()) &&
                sURIMatcher.match(uri) > 0);
    }
}
