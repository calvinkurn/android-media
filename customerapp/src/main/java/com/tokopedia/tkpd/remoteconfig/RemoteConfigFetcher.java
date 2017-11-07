package com.tokopedia.tkpd.remoteconfig;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.R;

/**
 * Created by okasurya on 9/11/17.
 */

public class RemoteConfigFetcher {
    private static final int CONFIG_CACHE_EXPIRATION = 1000;

    private FirebaseRemoteConfig firebaseRemoteConfig;
    private Activity activity;
    private static final String SHOW_HIDE_APP_SHARE_BUTTON = "mainapp_show_app_share_button";
    private static final String APP_SHARE_DESCRIPTION = "app_share_description";
    private static final String MAINAPP_ACTIVATE_BRANCH_LINKS = "mainapp_activate_branch_links";


    public RemoteConfigFetcher(Activity activity) {
        this.firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        this.activity = activity;
    }

    public void fetch(final Listener listener) {
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_default);
        firebaseRemoteConfig.fetch(CONFIG_CACHE_EXPIRATION)
                .addOnCompleteListener(this.activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.activateFetched();
                        }
                        listener.onComplete(firebaseRemoteConfig);
                        saveFetchedDataToCache();
                    }
                })
                .addOnFailureListener(this.activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e);
                    }
                });
    }

    private void saveFetchedDataToCache() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(activity, TkpdCache.FIREBASE_REMOTE_CONFIG);
        localCacheHandler.putBoolean(TkpdCache.Key.SHOW_HIDE_APP_SHARE_BUTTON_KEY, firebaseRemoteConfig.getBoolean(SHOW_HIDE_APP_SHARE_BUTTON));
        localCacheHandler.putString(TkpdCache.Key.APP_SHARE_DESCRIPTION_KEY, firebaseRemoteConfig.getString(APP_SHARE_DESCRIPTION));
        localCacheHandler.putBoolean(TkpdCache.Key.MAINAPP_ACTIVATE_BRANCH_LINKS_KEY, firebaseRemoteConfig.getBoolean(MAINAPP_ACTIVATE_BRANCH_LINKS));
        localCacheHandler.applyEditor();
    }

    public interface Listener {
        void onComplete(FirebaseRemoteConfig firebaseRemoteConfig);

        void onError(Exception e);
    }
}
