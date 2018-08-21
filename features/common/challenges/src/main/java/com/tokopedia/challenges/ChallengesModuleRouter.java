package com.tokopedia.challenges;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import permissions.dispatcher.PermissionRequest;

public interface ChallengesModuleRouter {

    Intent getLoginIntent(Context context);

    void actionOpenGeneralWebView(Activity activity, String mobileUrl);

    Intent getHomeIntent(Context context);

    public Intent getGalleryVideoIntent(Context activity);

    public Intent getGalleryVideoImageIntent(Context activity);
    //void shareChallenge(Activity context, String packageName, String url, String title, String imageUrl, String og_url, String og_title, String og_image_url);

    String getResultSelectionPath(Intent data);

    void onShowRationale(Context context, PermissionRequest request,  String permission);
    void onPermissionDenied(Context context,   String permission);
    void onNeverAskAgain(Context context,  String permission);
    void generateBranchUrlForChallenge(Activity context, String url, String title, String og_url, String og_title, String og_image, String deepLink, final BranchLinkGenerateListener branchLinkGenerateListener);

    public interface BranchLinkGenerateListener {
        void onGenerateLink(String shareContents, String shareUri);
    }

    void shareBranchUrlForChallenge(Activity context, String packageName, String url, String shareContents);

}
