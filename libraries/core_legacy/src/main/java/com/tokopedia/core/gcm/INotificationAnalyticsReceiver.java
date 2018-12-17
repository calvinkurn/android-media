package com.tokopedia.core.gcm;

import android.os.Bundle;

import rx.Observable;

/**
 * @author  by alvarisi on 12/20/16.
 */

public interface INotificationAnalyticsReceiver {
    void onNotificationReceived(Observable<Bundle> bundle);

    Bundle buildAnalyticNotificationData(Bundle bundle);
}
