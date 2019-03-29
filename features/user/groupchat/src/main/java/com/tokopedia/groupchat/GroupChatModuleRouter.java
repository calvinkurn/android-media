package com.tokopedia.groupchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * @author by nisie on 3/1/18.
 */

public interface GroupChatModuleRouter {

    Intent getHomeIntent(Context context);

    Intent getInboxChannelsIntent(Context context);

    void openRedirectUrl(Activity activity, String url);

    Intent getLoginIntent(Context context);

    void generateBranchLink(String channelId, String title, String contentMessage, String imgUrl,
                            String
            shareUrl, Activity activity, final ShareListener
                                    listener);

    String getNotificationPreferenceConstant();

    public interface ShareListener {
        void onGenerateLink(String shareContents, String shareUri);
    }

    void shareGroupChat(Activity activity, String channelId, String title, String contentMessage, String imgUrl,
                        String shareUrl, String userId, String sharing);

    void sendAnalyticsGroupChat(String url, String error);

    boolean isSupportedDelegateDeepLink(String appLinks);

}
