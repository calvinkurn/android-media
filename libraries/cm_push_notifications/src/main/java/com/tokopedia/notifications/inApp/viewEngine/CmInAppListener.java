package com.tokopedia.notifications.inApp.viewEngine;

import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;

/**
 * @author lalit.singh
 */
public interface CmInAppListener {
    void onCMinAppDismiss(CMInApp inApp);
    void onCMinAppInteraction(CMInApp cmInApp);
    void onCMInAppLinkClick(String appLink, CMInApp cmInApp, ElementType elementType);
    void onCMInAppClosed(CMInApp cmInApp);
    void onCMInAppInflateException(CMInApp inApp);
}
