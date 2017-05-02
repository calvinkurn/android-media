package com.tokopedia.core.gcm.base;

import android.app.Application;
import android.os.Bundle;

/**
 * Created by alvarisi on 3/17/17.
 */

public interface IAppNotificationReceiver {
    void init(Application application);

    void onNotificationReceived(String from, Bundle bundle);
}
