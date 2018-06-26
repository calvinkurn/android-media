package com.tokopedia.groupchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;

/**
 * @author by nisie on 3/1/18.
 */

public interface GroupChatModuleRouter {

    String ENABLE_GROUPCHAT_ROOM = "enable_groupchat_room";
    String ENABLE_GROUPCHAT_IDLE_KICK = "enable_groupchat_idle_kick";

    Intent getHomeIntent(Context context);

    Intent getInboxChannelsIntent(Context context);

    void openRedirectUrl(Activity activity, String url);

    Intent getLoginIntent(Context context);

    void generateBranchLink(String channelId, String title, String contentMessage, String imgUrl,
                            String
            shareUrl, Activity activity, final ShareListener
                                    listener);

    String getNotificationPreferenceConstant();

    boolean isEnabledGroupChat();

    boolean isEnabledGroupChatRoom();

    boolean isEnabledIdleKick();

    public interface ShareListener {
        void onGenerateLink(String shareContents, String shareUri);
    }

    void shareGroupChat(Activity activity, String channelId, String title, String contentMessage, String imgUrl,
                        String shareUrl);
}
