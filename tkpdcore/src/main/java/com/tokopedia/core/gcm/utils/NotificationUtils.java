package com.tokopedia.core.gcm.utils;

import android.content.Intent;

/**
 * Created by alvarisi on 1/12/17.
 */

public class NotificationUtils {
    public static Intent configureGeneralIntent(Intent intent){
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("from_notif", true);
        intent.putExtra("unread", false);
        return intent;
    }
}
