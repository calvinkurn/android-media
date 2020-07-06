package com.tokopedia.core.network;

import android.content.Intent;

import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.GCMHandler;

@Deprecated
public interface CoreNetworkRouter {

    Intent getMaintenancePageIntent();

    GCMHandler legacyGCMHandler();

    SessionHandler legacySessionHandler();
}
