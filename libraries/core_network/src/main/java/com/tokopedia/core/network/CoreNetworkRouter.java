package com.tokopedia.core.network;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.utils.RouterUtils;

public interface CoreNetworkRouter {

    Intent getMaintenancePageIntent();

    GCMHandler legacyGCMHandler();

    SessionHandler legacySessionHandler();
}
