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

    static
    {
        sURIMatcher.addURI(ApplinkConstInternalMarketplace.HOST_MARKETPLACE, "product/*/", 1);
        sURIMatcher.addURI(ApplinkConstInternalMarketplace.HOST_MARKETPLACE, "product/*/*/", 2);
    }

    private static RemoteConfig getRemoteConfig(Context context){
        if (remoteConfig == null) {
            remoteConfig = new FirebaseRemoteConfigImpl(context.getApplicationContext());
        }
        return remoteConfig;
    }

    public static boolean isGoToOldProductDetail(Context context){
        return getRemoteConfig(context).getBoolean(RemoteConfigKey.MAIN_APP_DISABLE_NEW_PRODUCT_DETAIL);
    }

    public static Intent getProductIntent(Context context, String productApplink) {
        if (isGoToOldProductDetail(context)) {
            Intent intent = new Intent();
            intent.setClassName(context.getPackageName(), "com.tokopedia.tkpdpdp.ProductInfoActivity");
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                return intent;
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
