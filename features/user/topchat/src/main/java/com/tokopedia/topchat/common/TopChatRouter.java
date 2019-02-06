package com.tokopedia.topchat.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * @author by nisie on 5/18/18.
 */
public interface TopChatRouter {

    Intent getHelpPageActivity(Context context, String url, boolean isFromChatBot);

    void openRedirectUrl(Activity activity, String url);

    Intent getSplashScreenIntent(Context context);
}
