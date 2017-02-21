package com.tokopedia.core.gcm;

/**
 * @author  by alvarisi on 12/20/16.
 */

public interface Constants {
    String FIREBASE_PROJECT_ID = "673352445777";
    String ARG_NOTIFICATION_CODE = "tkp_code";
    String ARG_NOTIFICATION_TITLE = "title";
    String ARG_NOTIFICATION_DESCRIPTION = "desc";
    String ARG_NOTIFICATION_IMAGE = "url_img";
    String ARG_NOTIFICATION_ICON = "url_icon";
    String ARG_NOTIFICATION_URL = "url";
    String ARG_NOTIFICATION_UPDATE_APPS_TITLE = "title_update";
    int REGISTRATION_STATUS_OK = 1;
    int REGISTRATION_STATUS_ERROR = 2;
    String REGISTRATION_MESSAGE_OK = "FCM Sucessfully";
    String REGISTRATION_MESSAGE_ERROR = "FCM Error";
    String EXTRA_PLAYSTORE_URL = "market://details?id=com.tokopedia.tkpd";
    String EXTRA_FROM_PUSH = "from_notif";
    String EXTRA_UNREAD = "unread";
}
