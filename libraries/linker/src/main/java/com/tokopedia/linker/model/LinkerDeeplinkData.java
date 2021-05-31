package com.tokopedia.linker.model;

import android.app.Activity;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public class LinkerDeeplinkData {
    private String clientId;
    private Uri referrable;
    @Nullable
    private WeakReference<Activity> activity;


    public Uri getReferrable() {
        return referrable;
    }

    public void setReferrable(Uri referrable) {
        this.referrable = referrable;
    }

    @Nullable
    public Activity getActivity() {
        return activity.get();
    }

    public void setActivity(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}
