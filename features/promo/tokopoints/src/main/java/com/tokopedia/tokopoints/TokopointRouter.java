package com.tokopedia.tokopoints;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.user.session.UserSessionInterface;

public interface TokopointRouter {
    void openTokoPoint(Context context, String url);

    void openTokopointWebview(Context context, String url, String title);

    Intent getHomeIntent(Context context);

    String getStringRemoteConfig(String key);

    long getLongRemoteConfig(String key, long defaultValue);

    boolean getBooleanRemoteConfig(String key, boolean defaultValue);
}
