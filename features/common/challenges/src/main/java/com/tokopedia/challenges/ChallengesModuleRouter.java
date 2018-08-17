package com.tokopedia.challenges;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public interface ChallengesModuleRouter {

    Intent getLoginIntent(Context context);

    void actionOpenGeneralWebView(Activity activity, String mobileUrl);

    Intent getHomeIntent(Context context);

    void generateBranchUrlForChallenge(Activity context, String url, String title, String og_url, String og_title, String og_image, String deepLink, final BranchLinkGenerateListener branchLinkGenerateListener);

    public interface BranchLinkGenerateListener {
        void onGenerateLink(String shareContents, String shareUri);
    }

    void shareBranchUrlForChallenge(Activity context, String packageName, String url, String shareContents);

}
