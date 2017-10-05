package com.tokopedia.tkpd.remoteconfig;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.tokopedia.tkpd.R;

/**
 * Created by okasurya on 9/11/17.
 */

public class RemoteConfigFetcher {
    private static final int CONFIG_CACHE_EXPIRATION = 1000;

    private FirebaseRemoteConfig firebaseRemoteConfig;
    private Activity activity;

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
                    }
                })
                .addOnFailureListener(this.activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e);
                    }
                });
    }

    public interface Listener {
        void onComplete(FirebaseRemoteConfig firebaseRemoteConfig);

        void onError(Exception e);
    }
}
