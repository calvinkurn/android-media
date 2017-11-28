package com.tokopedia.tkpd.remoteconfig;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.beloo.widget.chipslayoutmanager.util.log.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.R;

import rx.Emitter;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;

/**
 * Created by okasurya on 9/11/17.
 */

public class RemoteConfigFetcher {
    private static final int THREE_HOURS = 10800000;
    private static final int CONFIG_CACHE_EXPIRATION = THREE_HOURS;

    private FirebaseRemoteConfig firebaseRemoteConfig;
    private Activity activity;


    public RemoteConfigFetcher(Activity activity) {
        this.firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        this.activity = activity;
    }

    public void fetch(@Nullable final Listener listener) {
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_default);
        firebaseRemoteConfig.fetch(CONFIG_CACHE_EXPIRATION)
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.activateFetched();
                        }
                        if(activity != null && listener != null) {
                            listener.onComplete(firebaseRemoteConfig);
                        }
                    }
                })
                .addOnFailureListener(this.activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(activity != null && listener != null) {
                            listener.onError(e);
                        }
                    }
                });
    }

    public interface Listener {
        void onComplete(FirebaseRemoteConfig firebaseRemoteConfig);

        void onError(Exception e);
    }
}