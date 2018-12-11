package com.tokopedia.core.share;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.core.model.share.ShareData;

import java.util.Objects;

/**
 * @author by nisie on 8/23/18.
 */
public class ShareBroadcastReceiver extends BroadcastReceiver {

    public static final String KEY_TYPE = "shareType";

    @Override
    public void onReceive(Context context, Intent intent) {
        for (String key : Objects.requireNonNull(intent.getExtras()).keySet()) {
            try {
                ComponentName componentInfo = (ComponentName) intent.getExtras().get(key);
                PackageManager packageManager = context.getPackageManager();
                assert componentInfo != null;
                String appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(componentInfo.getPackageName(), PackageManager.GET_META_DATA));

                if (intent.getExtras() != null) {
                    String type = intent.getExtras().getString(KEY_TYPE, "");
                    AnalyticTracker analyticTracker = ((AbstractionRouter) context
                            .getApplicationContext()).getAnalyticTracker();

                    if (analyticTracker != null && type.equals(ShareData.GROUPCHAT_TYPE)) {
                        analyticTracker.sendEventTracking(
                                "clickShare",
                                "groupchat room",
                                "click on choose media to share",
                                appName
                        );
                    }
                }

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
}
