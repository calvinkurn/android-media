package com.tokopedia.core.router;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.util.RouterUtils;

/**
 * Created by stevenfredian on 11/23/16.
 */

public class SessionRouter {

    private static final String LOGIN_ACTIVITY = "com.tokopedia.session.session.activity.Login";

    /////////// INTENT
    public static Intent getLoginActivityIntent(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, LOGIN_ACTIVITY);
        return intent;
    }
}
