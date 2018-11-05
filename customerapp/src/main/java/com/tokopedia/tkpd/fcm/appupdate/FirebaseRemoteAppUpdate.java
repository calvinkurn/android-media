package com.tokopedia.tkpd.fcm.appupdate;

import android.content.Context;

import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;

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

    public FirebaseRemoteAppUpdate(Context context) {
        remoteConfig = new FirebaseRemoteConfigImpl(context);
    }

    @Override
    public void checkApplicationUpdate(final OnUpdateListener listener) {
        if (remoteConfig != null) {
            DetailUpdate detailUpdate = getDetailUpdate();
            if (detailUpdate.isNeedUpdate() && GlobalConfig.VERSION_CODE < detailUpdate.getLatestVersionCode()) {
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
