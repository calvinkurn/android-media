package com.tokopedia.core.gcm.notification.applink;

import android.content.Context;

/**
 * Created by alvarisi on 2/23/17.
 */

public abstract class AbstractApplinkBuildAndShowNotification<T> {
    public abstract void process(Context context);
}