package com.tokopedia.challenges;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public interface ChallengesModuleRouter {

    Intent getLoginIntent(Context context);

    Intent getHomeIntent(Context context);

    void generateBranchUrlForChallenge(Activity context, String url, String title, String channel, String og_url, String og_title, String og_desc, String og_image, String deepLink, final BranchLinkGenerateListener branchLinkGenerateListener);

    void shareBranchUrlForChallenge(Activity context, String packageName, String url, String shareContents);

    String getStringRemoteConfig(String key);

    boolean getBooleanRemoteConfig(String key, boolean defaultValue);

    interface BranchLinkGenerateListener {
        void onGenerateLink(String shareContents, String shareUri);
    }

}
