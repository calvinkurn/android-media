package com.tokopedia.tkpd.fcm.appupdate;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate;
import com.tokopedia.abstraction.base.view.appupdate.model.DataUpdateApp;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;

/**
 * Created by okasurya on 7/25/17.
 */
public class FirebaseRemoteAppUpdate implements ApplicationUpdate {
    private static final String ANDROID_CUSTOMER_APP_UPDATE = "android_customer_app_update";

    private RemoteConfig remoteConfig;

    public FirebaseRemoteAppUpdate(Context context) {
        remoteConfig = new FirebaseRemoteConfigImpl(context);
    }

    @Override
    public void checkApplicationUpdate(final OnUpdateListener listener) {
        if (remoteConfig != null) {
            String dataAppUpdate = remoteConfig.getString(ANDROID_CUSTOMER_APP_UPDATE);
            if (!TextUtils.isEmpty(dataAppUpdate)) {
                Gson gson = new Gson();
                DataUpdateApp dataUpdateApp = gson.fromJson(dataAppUpdate, DataUpdateApp.class);
                if(dataUpdateApp != null) {
                    DetailUpdate detailUpdate = generateDetailUpdate(dataUpdateApp);
                    if (dataUpdateApp.isIsForceEnabled() && GlobalConfig.VERSION_CODE < dataUpdateApp.getLatestVersionForceUpdate()) {
                        detailUpdate.setForceUpdate(true);
                        listener.onNeedUpdate(detailUpdate);
                    } else if (dataUpdateApp.isIsOptionalEnabled() && GlobalConfig.VERSION_CODE < dataUpdateApp.getLatestVersionOptionalUpdate()) {
                        detailUpdate.setForceUpdate(false);
                        listener.onNeedUpdate(detailUpdate);
                    } else {
                        listener.onNotNeedUpdate();
                    }
                }
            }
        }
    }

    private DetailUpdate generateDetailUpdate(DataUpdateApp dataUpdateApp) {
        DetailUpdate detailUpdate = new DetailUpdate();
        detailUpdate.setNeedUpdate(true);
        detailUpdate.setUpdateTitle(dataUpdateApp.getTitle());
        detailUpdate.setUpdateMessage(dataUpdateApp.getMessage());
        detailUpdate.setUpdateLink(dataUpdateApp.getLink());
        return detailUpdate;
    }
}
