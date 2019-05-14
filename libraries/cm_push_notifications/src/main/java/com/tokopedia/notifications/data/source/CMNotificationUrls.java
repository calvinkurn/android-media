package com.tokopedia.notifications.data.source;

import com.tokopedia.config.url.TokopediaUrl;

/**
 * Created by Ashwani Tyagi on 24/10/18.
 */
public class CMNotificationUrls {
    public static String CAMPAIGN_MANAGEMENT_DOMAIN = TokopediaUrl.Companion.getInstance().getIMT();
    public static String CM_TOKEN_UPDATE = CAMPAIGN_MANAGEMENT_DOMAIN + "api/v1/user/add";

}
