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
    private static final String IS_APPLICATION_NEED_UPDATE = "is_application_need_update";
    private static final String LATEST_APP_VERSION_CODE = "latest_app_version_code";
    private static final String UPDATE_TITLE = "update_title";
    private static final String UPDATE_MESSAGE = "update_message";
    private static final String IS_FORCE_UPDATE = "is_force_update";
    private static final String UPDATE_LINK = "update_link";

    private static FirebaseRemoteAppUpdate instance;
    private Activity activity;
    private FirebaseRemoteConfig firebaseRemoteConfig;

    private FirebaseRemoteAppUpdate(Activity activity) {
        this.activity = activity;

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    }

    public static FirebaseRemoteAppUpdate getInstance(Activity activity) {
        if(instance == null) {
            instance = new FirebaseRemoteAppUpdate(activity);
        }

        return instance;
    }

    @Override
    public void checkApplicationUpdate(final OnUpdateListener listener) {
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_default);
        firebaseRemoteConfig.fetch(1000)
                .addOnCompleteListener(this.activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.activateFetched();
                        }
                        DetailUpdate detailUpdate = getDetailUpdate();
                        if(detailUpdate.isNeedUpdate() && BuildConfig.VERSION_CODE < detailUpdate.getLatestVersionCode()) {
                            listener.onNeedUpdate(detailUpdate);
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
        detailUpdate.setNeedUpdate(firebaseRemoteConfig.getBoolean(IS_APPLICATION_NEED_UPDATE));
        detailUpdate.setLatestVersionCode(firebaseRemoteConfig.getLong(LATEST_APP_VERSION_CODE));
        detailUpdate.setForceUpdate(firebaseRemoteConfig.getBoolean(IS_FORCE_UPDATE));
        detailUpdate.setUpdateTitle(firebaseRemoteConfig.getString(UPDATE_TITLE));
        detailUpdate.setUpdateMessage(firebaseRemoteConfig.getString(UPDATE_MESSAGE));
        detailUpdate.setUpdateLink(firebaseRemoteConfig.getString(UPDATE_LINK));
        return detailUpdate;
    }
}
