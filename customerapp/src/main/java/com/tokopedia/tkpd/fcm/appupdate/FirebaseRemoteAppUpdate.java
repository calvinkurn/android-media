package com.tokopedia.tkpd.fcm.appupdate;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.tokopedia.core.appupdate.ApplicationUpdate;
import com.tokopedia.core.appupdate.model.DetailUpdate;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.tkpd.R;

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
    private static final int CONFIG_CACHE_EXPIRATION = 1000;

    private Activity activity;
    private FirebaseRemoteConfig firebaseRemoteConfig;

    public FirebaseRemoteAppUpdate(Activity activity) {
        this.activity = activity;
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    }

    @Override
    public void checkApplicationUpdate(final OnUpdateListener listener) {
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_default);
        firebaseRemoteConfig.fetch(CONFIG_CACHE_EXPIRATION)
                .addOnCompleteListener(this.activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.activateFetched();
                        }
                        DetailUpdate detailUpdate = getDetailUpdate();
                        if (detailUpdate.isNeedUpdate() && BuildConfig.VERSION_CODE < detailUpdate.getLatestVersionCode()) {
                            listener.onNeedUpdate(detailUpdate);
                        } else {
                            listener.onNotNeedUpdate();
                        }
                    }
                })
                .addOnFailureListener(this.activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e);
                    }
                });
    }

    private DetailUpdate getDetailUpdate() {
        DetailUpdate detailUpdate = new DetailUpdate();
        detailUpdate.setNeedUpdate(firebaseRemoteConfig.getBoolean(MAINAPP_IS_NEED_UPDATE));
        detailUpdate.setLatestVersionCode(firebaseRemoteConfig.getLong(MAINAPP_LATEST_VERSION_CODE));
        detailUpdate.setForceUpdate(firebaseRemoteConfig.getBoolean(MAINAPP_IS_FORCE_UPDATE));
        detailUpdate.setUpdateTitle(firebaseRemoteConfig.getString(MAINAPP_UPDATE_TITLE));
        detailUpdate.setUpdateMessage(firebaseRemoteConfig.getString(MAINAPP_UPDATE_MESSAGE));
        detailUpdate.setUpdateLink(firebaseRemoteConfig.getString(MAINAPP_UPDATE_LINK));
        return detailUpdate;
    }
}
