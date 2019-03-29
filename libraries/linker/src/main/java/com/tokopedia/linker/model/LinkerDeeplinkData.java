package com.tokopedia.linker.model;

import android.app.Activity;
import android.net.Uri;

public class LinkerDeeplinkData {
    private String clientId;
    private Uri referrable;
    private Activity activity;


    public Uri getReferrable() {
        return referrable;
    }

    public void setReferrable(Uri referrable) {
        this.referrable = referrable;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}
