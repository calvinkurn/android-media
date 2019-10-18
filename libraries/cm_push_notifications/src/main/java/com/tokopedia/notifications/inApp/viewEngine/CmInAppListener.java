package com.tokopedia.notifications.inApp.viewEngine;

import android.net.Uri;

import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;

/**
 * @author lalit.singh
 */
public interface CmInAppListener {

    boolean showCmInAppMessage();

    void onCMInAppShown(CMInApp cmInApp);

    void onCMinAppDismiss();

    void onCMInAppLinkClick(Uri deepLinkUri, CMInApp cmInApp, ElementType elementType);

    void onCMInAppClosed(CMInApp cmInApp);

    void onCMInAppInflateException(CMInApp inApp);
}
