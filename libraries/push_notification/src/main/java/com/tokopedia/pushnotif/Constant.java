package com.tokopedia.pushnotif;

/**
 * @author ricoharisin .
 */

public interface Constant {

    String EXTRA_APPLINK_FROM_PUSH = "applink_from_notif";

     interface NotificationGroup {
         String TALK = "com.tokopedia.tkpd.TALK";
         String TOPCHAT = "com.tokopedia.tkpd.TOPCHAT";
         String TRANSACTION = "com.tokopedia.tkpd.TRANSACTION";
         String NEW_ORDER = "com.tokopedia.tkpd.NEWORDER";
         String RESOLUTION = "com.tokopedia.tkpd.RESOLUTION";
         String GENERAL = "com.tokopedia.tkpd.GENERAL";
    }

    interface NotificationChannel {
        String GENERAL = "ANDROID_GENERAL_CHANNEL";
    }

    interface NotificationId {
        int GENERAL = 100;
        int TALK = 200;
        int CHAT = 300;
        int TRANSACTION = 400;
        int SELLER = 500;
        int RESOLUTION = 600;
    }
}
