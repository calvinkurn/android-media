package com.tokopedia.core.gcm.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.gcm.Constants;

/**
 * Created by alvarisi on 9/7/17.
 */

public class ApplinkUtils {
    @NonNull
    public static Intent getSellerAppApplinkIntent(Context context, Bundle extras) {
        Intent launchIntent = context.getPackageManager()
                .getLaunchIntentForPackage(GlobalConfig.PACKAGE_SELLER_APP);
        if (launchIntent == null) {
            launchIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Constants.URL_MARKET + GlobalConfig.PACKAGE_SELLER_APP)
            );
        } else {
            launchIntent.setData(Uri.parse(extras.getString(DeepLink.URI)));
            launchIntent.putExtras(extras);
            launchIntent.putExtra(Constants.EXTRA_APPLINK, extras.getString(DeepLink.URI));
        }
        return launchIntent;
    }
}
