package com.tokopedia.onboarding.analytics;

import android.content.Context;

import com.tokopedia.onboarding.OnboardingRouter;

/**
 * @author by nisie on 10/8/18.
 */
public class ConsumerOnboardingAnalytics {
    String EVENT_ONBOARDING = "onBoardingEvent";
    String CATEGORY_ONBOARDING = "onboarding";
    String ACTION_ONBOARDING_SKIP = "click - skip button";
    String ACTION_ONBOARDING_START = "click - mulai";

    String ONBOARDING_SKIP_LABEL = "skip - ";
    String ONBOARDING_START_LABEL = "click mulai sekarang";



    public void eventOnboardingSkip(Context applicationContext, int skipPage) {
        ((OnboardingRouter) applicationContext).sendEventTracking(
                EVENT_ONBOARDING,
                CATEGORY_ONBOARDING,
                ACTION_ONBOARDING_SKIP,
                ONBOARDING_SKIP_LABEL + skipPage
        );

    }

    public void sendScreen(Context applicationContext, String screenName) {
        ((OnboardingRouter) applicationContext).sendScreenName(screenName);
    }

    public void eventOnboardingStartNow(Context applicationContext) {
        ((OnboardingRouter) applicationContext).sendEventTracking(
                EVENT_ONBOARDING,
                CATEGORY_ONBOARDING,
                ACTION_ONBOARDING_START,
                ONBOARDING_START_LABEL
        );
    }
}
