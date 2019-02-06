package com.tokopedia.notifications.common;

/**
 * @author lalit.singh
 */
public interface CMEvents {

    interface PersistentEvent {
        String EVENT_VIEW_NOTIFICATION = "viewNotifications";
        String EVENT_ACTION_PUSH_RECEIVED = "push received";


        String EVENT_ACTION_LOGO_CLICK = "click tokopedia logo";

        String EVENT = "openPushNotification";
        String EVENT_LABEL = "-";

        String EVENT_CATEGORY = "cm_persistent_push";
        String EVENT_ACTION_CANCELED = "click close button";

    }

}
