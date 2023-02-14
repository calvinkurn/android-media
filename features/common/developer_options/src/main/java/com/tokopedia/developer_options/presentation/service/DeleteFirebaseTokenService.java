package com.tokopedia.developer_options.presentation.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

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
        FirebaseMessaging.getInstance().deleteToken();
        Log.d(TAG, "Token deleted");

        FirebaseMessaging.getInstance().getToken();
        Log.d(TAG, "New Token Requested");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Services destroyed");
    }
}
