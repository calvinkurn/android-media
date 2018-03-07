package com.tokopedia.sellerapp.fcm.appupdate;

import android.app.Activity;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.appupdate.ApplicationUpdate;
import com.tokopedia.core.appupdate.model.DetailUpdate;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.sellerapp.BuildConfig;

/**
 * Created by nakama on 07/02/18.
 */

public class FirebaseRemoteAppUpdate implements ApplicationUpdate {
    private static final String SELLERAPP_IS_NEED_UPDATE = "sellerapp_is_need_update";
    private static final String SELLERAPP_LATEST_VERSION_CODE = "sellerapp_latest_version_code";
    private static final String SELLERAPP_IS_FORCE_UPDATE = "sellerapp_is_force_update";
    private static final String SELLERAPP_UPDATE_TITLE = "sellerapp_update_title";
    private static final String SELLERAPP_UPDATE_MESSAGE = "sellerapp_update_message";
    private static final String SELLERAPP_UPDATE_LINK = "sellerapp_update_link";

    private RemoteConfig remoteConfig;

    public FirebaseRemoteAppUpdate(Activity activity) {
        remoteConfig = new FirebaseRemoteConfigImpl(activity);
    }

    @Override
    public void checkApplicationUpdate(final OnUpdateListener listener) {
        if(remoteConfig != null) {
            boolean isNeedUpdate = remoteConfig.getBoolean(SELLERAPP_IS_NEED_UPDATE);
            if (!isNeedUpdate) {
                listener.onNotNeedUpdate();
                return;
            }
            long latestVersionCode = remoteConfig.getLong(SELLERAPP_LATEST_VERSION_CODE);
            if (GlobalConfig.VERSION_CODE < latestVersionCode) {
                DetailUpdate detailUpdate = new DetailUpdate();
                detailUpdate.setNeedUpdate(true);
                detailUpdate.setLatestVersionCode(latestVersionCode);
                detailUpdate.setForceUpdate(remoteConfig.getBoolean(SELLERAPP_IS_FORCE_UPDATE));
                detailUpdate.setUpdateTitle(remoteConfig.getString(SELLERAPP_UPDATE_TITLE));
                detailUpdate.setUpdateMessage(remoteConfig.getString(SELLERAPP_UPDATE_MESSAGE));
                detailUpdate.setUpdateLink(remoteConfig.getString(SELLERAPP_UPDATE_LINK));
                listener.onNeedUpdate(detailUpdate);
            } else {
                listener.onNotNeedUpdate();
            }
        }
    }

}