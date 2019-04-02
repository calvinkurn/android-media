package com.tokopedia.core.share;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;
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

                    if (type.equals(ShareData.GROUPCHAT_TYPE)) {
                        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                                "clickShare",
                                "groupchat room",
                                "click on choose media to share",
                                appName
                        ));
                    }
                }

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
}
