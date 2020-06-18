package com.tokopedia.notifications.inApp.viewEngine;

import android.net.Uri;

import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;

/**
 * @author lalit.singh
 */
public interface CmInAppListener {
    void onCMinAppDismiss(CMInApp inApp);
    void onCMinAppInteraction(CMInApp cmInApp);
    void onCMInAppLinkClick(Uri deepLinkUri, CMInApp cmInApp, ElementType elementType);
    void onCMInAppClosed(CMInApp cmInApp);
    void onCMInAppInflateException(CMInApp inApp);
}
