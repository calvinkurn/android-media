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

        String EVENT_ACTION_PERISTENT_CLICK = "Click_Action_Persistent";

    }


    interface PersistentMoengageEventAttribute {
        String ICON_NAME = "icon_name";
        String DEEPLINK = "deeplink";
        String ICON_URL = "icon_url";
        String CAMPAIGN_ID = "campaign_id";
        String CAMPAIGN_NAME = "campaign_name";
    }

}
