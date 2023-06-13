package com.tokopedia.developer_options.presentation.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tokopedia.fcmcommon.service.SyncFcmTokenService;

public class DeleteFirebaseTokenService extends IntentService {

    private static final String TAG = "DeleteFcmTokenService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DeleteFirebaseTokenService(String name) {
        super(TAG);
    }

    public DeleteFirebaseTokenService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Service started");
        FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "Task success: " + task.isSuccessful());
                Log.d(TAG, "Token deleted");

                retriveNewToken();
            }
        });

    }

    private void retriveNewToken() {
        try {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @SuppressLint("PII Data Exposure")
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    Log.d(TAG, "Task success: " + task.isSuccessful());
                    SyncFcmTokenService.Companion.startService(DeleteFirebaseTokenService.this);
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Services destroyed");
    }
}
