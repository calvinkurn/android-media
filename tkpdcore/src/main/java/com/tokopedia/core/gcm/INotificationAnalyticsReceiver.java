package com.tokopedia.core.gcm;

import android.os.Bundle;

/**
 * @author  by alvarisi on 12/20/16.
 */

public interface INotificationAnalyticsReceiver {
    void onNotificationReceived(Bundle bundle);

    Bundle buildAnalyticNotificationData(Bundle bundle);
}
