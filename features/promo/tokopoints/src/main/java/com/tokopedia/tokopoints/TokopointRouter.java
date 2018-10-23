package com.tokopedia.tokopoints;

import android.content.Context;
import android.content.Intent;

public interface TokopointRouter {
    void openTokoPoint(Context context, String url);

    Intent getHomeIntent(Context context);

    String getStringRemoteConfig(String key);

    long getLongRemoteConfig(String key,long defaultValue);
}
