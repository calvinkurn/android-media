package com.tokopedia.groupchat.common.data;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.url.TokopediaUrl;

/**
 * @author by nisie on 2/23/18.
 */

public class GroupChatUrl {

    public static String BASE_URL = TokopediaUrl.Companion.getInstance().getPLAY();
    public static String BASE_GCP_URL = TokopediaUrl.Companion.getInstance().getGROUPCHAT();

    public static final String ERROR_WEBVIEW_IMAGE_URL = "https://ecs7.tokopedia.net/img/android/others/ic_error_webview.png";

    public static final String FAQ_URL = ApplinkConst.WEBVIEW + "?url="+
            TokopediaUrl.Companion.getInstance().getWEB() +"help/article/tokopedia-play";
}
