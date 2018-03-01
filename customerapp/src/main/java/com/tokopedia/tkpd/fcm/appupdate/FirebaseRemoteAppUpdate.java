package com.tokopedia.tkpd.fcm.appupdate;

import android.app.Activity;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.appupdate.ApplicationUpdate;
import com.tokopedia.core.appupdate.model.DetailUpdate;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.tkpd.BuildConfig;

/**
 * Created by okasurya on 7/25/17.
 */
public class FirebaseRemoteAppUpdate implements ApplicationUpdate {
    private static final String MAINAPP_IS_NEED_UPDATE = "mainapp_is_need_update";
    private static final String MAINAPP_LATEST_VERSION_CODE = "mainapp_latest_version_code";
    private static final String MAINAPP_IS_FORCE_UPDATE = "mainapp_is_force_update";
    private static final String MAINAPP_UPDATE_TITLE = "mainapp_update_title";
    private static final String MAINAPP_UPDATE_MESSAGE = "mainapp_update_message";
    private static final String MAINAPP_UPDATE_LINK = "mainapp_update_link";

    private RemoteConfig remoteConfig;

    public FirebaseRemoteAppUpdate(Activity activity) {
        remoteConfig = new FirebaseRemoteConfigImpl(activity);
    }

    @Override
    public void checkApplicationUpdate(final OnUpdateListener listener) {
        if(remoteConfig != null) {
            DetailUpdate detailUpdate = getDetailUpdate();
            if (detailUpdate.isNeedUpdate() && BuildConfig.VERSION_CODE < detailUpdate.getLatestVersionCode()) {
                listener.onNeedUpdate(detailUpdate);
            } else {
                listener.onNotNeedUpdate();
            }
        }
    }

    private DetailUpdate getDetailUpdate() {
        DetailUpdate detailUpdate = new DetailUpdate();
        detailUpdate.setNeedUpdate(remoteConfig.getBoolean(MAINAPP_IS_NEED_UPDATE));
        detailUpdate.setLatestVersionCode(remoteConfig.getLong(MAINAPP_LATEST_VERSION_CODE));
        detailUpdate.setForceUpdate(remoteConfig.getBoolean(MAINAPP_IS_FORCE_UPDATE));
        detailUpdate.setUpdateTitle(remoteConfig.getString(MAINAPP_UPDATE_TITLE));
        detailUpdate.setUpdateMessage(remoteConfig.getString(MAINAPP_UPDATE_MESSAGE));
        detailUpdate.setUpdateLink(remoteConfig.getString(MAINAPP_UPDATE_LINK));
        return detailUpdate;
    }
}
