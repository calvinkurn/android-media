package com.tokopedia.challenges;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.Map;

public interface ChallengesModuleRouter {

    Intent getLoginIntent(Context context);

    Intent getHomeIntent(Context context);

    void generateBranchUrlForChallenge(Activity context, String url, String title, String channel, String og_url, String og_title, String og_image, String deepLink, final BranchLinkGenerateListener branchLinkGenerateListener);

    interface BranchLinkGenerateListener {
        void onGenerateLink(String shareContents, String shareUri);
    }

    void shareBranchUrlForChallenge(Activity context, String packageName, String url, String shareContents);

    String getStringRemoteConfig(String key);

    void sendMoengageEvents(String eventName, Map<String, Object> values);

    boolean getBooleanRemoteConfig(String key, boolean defaultValue);

}
