package com.tokopedia.topads.dashboard.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.config.GlobalConfig;

/**
 * Created by alvarisi on 9/7/17.
 */

public class ApplinkUtils {
    private static String URL_MARKET = "market://details?id=";
    private static String EXTRA_APPLINK = "applink_url";

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
