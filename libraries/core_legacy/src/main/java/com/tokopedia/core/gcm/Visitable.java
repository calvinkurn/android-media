package com.tokopedia.core.gcm;

import android.os.Bundle;

/**
 * @author by alvarisi on 1/12/17.
 */

public interface Visitable {
    void proccessReceivedNotification(Bundle incomingMessage);
}