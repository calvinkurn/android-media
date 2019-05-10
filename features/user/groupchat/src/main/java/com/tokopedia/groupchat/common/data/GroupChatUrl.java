package com.tokopedia.groupchat.common.data;

import com.tokopedia.config.url.TokopediaUrl;

/**
 * @author by nisie on 2/23/18.
 */

public class GroupChatUrl {

    public static String BASE_URL = TokopediaUrl.Companion.getInstance().getCHAT();
    public static String BASE_GCP_URL = TokopediaUrl.Companion.getInstance().getGROUPCHAT();

    public static final String ERROR_WEBVIEW_IMAGE_URL = "https://ecs7.tokopedia.net/img/android/others/ic_error_webview.png";
}
