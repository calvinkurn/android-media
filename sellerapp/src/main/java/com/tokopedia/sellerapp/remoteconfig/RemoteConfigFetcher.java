package com.tokopedia.sellerapp.remoteconfig;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.tokopedia.sellerapp.R;

/**
 * Created by okasurya on 9/11/17.
 */

public class RemoteConfigFetcher {
    private static final int THREE_HOURS = 10800000;
    private static final int CONFIG_CACHE_EXPIRATION = THREE_HOURS;

    private FirebaseRemoteConfig firebaseRemoteConfig;
    private Activity activity;

    public static FirebaseRemoteConfig initRemoteConfig(Context context) {
        try {
            if (FirebaseApp.getInstance() == null) {
                FirebaseApp.initializeApp(context);
            }

            return FirebaseRemoteConfig.getInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public RemoteConfigFetcher(Activity activity) {
        this.firebaseRemoteConfig = initRemoteConfig(activity);
        this.activity = activity;
    }

    public void fetch(@Nullable final Listener listener) {
        if (firebaseRemoteConfig != null && activity != null) {
            firebaseRemoteConfig.setDefaults(R.xml.remote_config_default);
            firebaseRemoteConfig.fetch(CONFIG_CACHE_EXPIRATION)
                    .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                firebaseRemoteConfig.activateFetched();
                            }
                            if (activity != null && listener != null) {
                                listener.onComplete(firebaseRemoteConfig);
                            }
                        }
                    })
                    .addOnFailureListener(this.activity, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (activity != null && listener != null) {
                                listener.onError(e);
                            }
                        }
                    });
        }
    }

    public interface Listener {
        void onComplete(FirebaseRemoteConfig firebaseRemoteConfig);

        void onError(Exception e);
    }
}