package com.tokopedia.topads.common.data.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.utils.GlobalConfig;

public class ApplinkUtil {
    private static String EXTRA_APPLINK = "applink_url";
    private static String URL_MARKET = "market://details?id=";
    @NonNull
    public static Intent getSellerAppApplinkIntent(Context context, Bundle extras) {
        Intent launchIntent = context.getPackageManager()
                .getLaunchIntentForPackage(GlobalConfig.PACKAGE_SELLER_APP);
        if (launchIntent == null) {
            launchIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(URL_MARKET + GlobalConfig.PACKAGE_SELLER_APP)
            );
        } else {
            launchIntent.setData(Uri.parse(extras.getString(DeepLink.URI)));
            launchIntent.putExtras(extras);
            launchIntent.putExtra(EXTRA_APPLINK, extras.getString(DeepLink.URI));
        }
        return launchIntent;
    }
}
