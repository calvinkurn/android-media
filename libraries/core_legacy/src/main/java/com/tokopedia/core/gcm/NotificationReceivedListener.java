package com.tokopedia.core.gcm;

import android.os.Bundle;

/**
 * @author  by alvarisi on 1/9/17.
 */
public interface NotificationReceivedListener {
    void onGetNotif();

    void onRefreshCart(int status);

    void onGetNotif(Bundle data);
}
