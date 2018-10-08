package com.tokopedia.onboarding;

import android.content.Context;
import android.content.Intent;

/**
 * @author by nisie on 10/8/18.
 */
public interface OnboardingRouter {
    void sendEventTracking(String event, String category, String action, String label);

    void sendScreenName(String screenName);

    Intent getHomeIntent(Context context);

}
