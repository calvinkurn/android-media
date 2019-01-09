package com.tokopedia.challenges;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.Map;

import permissions.dispatcher.PermissionRequest;

public interface ChallengesModuleRouter {

    Intent getLoginIntent(Context context);

    Intent getHomeIntent(Context context);

    void onShowRationale(Context context, PermissionRequest request, String permission);

    void onPermissionDenied(Context context, String permission);

    void onNeverAskAgain(Context context, String permission);

    void generateBranchUrlForChallenge(Activity context, String url, String title, String channel, String og_url, String og_title, String og_desc, String og_image, String deepLink, final BranchLinkGenerateListener branchLinkGenerateListener);

    void shareBranchUrlForChallenge(Activity context, String packageName, String url, String shareContents);

    String getStringRemoteConfig(String key);

    void sendMoengageEvents(String eventName, Map<String, Object> values);

    boolean getBooleanRemoteConfig(String key, boolean defaultValue);

    interface BranchLinkGenerateListener {
        void onGenerateLink(String shareContents, String shareUri);
    }

}
