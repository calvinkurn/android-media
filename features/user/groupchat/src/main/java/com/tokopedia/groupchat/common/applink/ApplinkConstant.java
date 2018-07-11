package com.tokopedia.groupchat.common.applink;

/**
 * @author by nisie on 3/1/18.
 */

public class ApplinkConstant {

    //Also possible with channel_url
    public static final String GROUPCHAT_ROOM = "tokopedia://groupchat/{channel_id}";
    public static final String GROUPCHAT_VOTE = "tokopedia://groupchat/{channel_id}/vote";
    public static final String GROUPCHAT_LIST = "tokopedia://groupchat";
    public static final String GROUPCHAT_ROOM_VIA_LIST = "tokopedia://groupchat/list/{channel_id}";
    public static final String GROUPCHAT_VOTE_VIA_LIST = "tokopedia://groupchat/list/{channel_id}/vote";
    public static final String PARAM_CHANNEL_ID = "channel_id";
}
