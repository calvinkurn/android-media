package com.tokopedia.challenges;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public interface ChallengesModuleRouter {

    Intent getLoginIntent(Context context);

    void actionOpenGeneralWebView(Activity activity, String mobileUrl);

    Intent getHomeIntent(Context context);

    void shareChallenge(Context context, String uri, String name, String imageUrl, String og_url, String og_title, String og_image_url);
}
