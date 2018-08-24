package com.tokopedia.challenges;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import java.util.HashMap;

import javax.inject.Inject;

public class ChallengesAnalytics {

    public static String EVENT_CLICK_CHALLENGES = "clickChallenges";
    public static String EVENT_CLICK_SHARE = "clickShare";
    public static String EVENT_CLICK_LIKE = "clickLike";

    public static String EVENT_CATEGORY_ACTIVE_CHALLENGES = "active challenges";
    public static String EVENT_CATEGORY_CHALLENGES = "challenges";
    public static String EVENT_CATEGORY_MYSUBMISSIONS = "my submissions";
    public static String EVENT_CATEGORY_PAST_CHALLENGES = "past challenges";
    public static String EVENT_CATEGORY_OTHER_SUBMISSION = "other submission";
    public static String EVENT_CATEGORY_OTHER_SUBMISSION_SEE_ALL = "other submission - view all";
    public static String EVENT_CATEGORY_SUBMISSION_PAGE = "submission page";
    public static String EVENT_CATEGORY_SUBMISSIONS = "submissions";
    public static String EVENT_CATEGORY_POST_PAGE = "post page";
    public static String EVENT_CATEGORY_SUBMIT_POST = "submit post";

    public static String EVENT_ACTION_CLICK = "click";
    public static String EVENT_ACTION_LIKE = "like";
    public static String EVENT_ACTION_UNLIKE = "unlike";
    public static String EVENT_ACTION_SHARE = "share";

    public static String EVENT_TNC = "tnc";
    public static String EVENT_SUBMIT = "submit";
    public static String EVENT_CANCEL = "cancel";


    private AnalyticTracker tracker;


    @Inject
    public ChallengesAnalytics(@ApplicationContext Context context) {
        if (context != null && context.getApplicationContext() instanceof AbstractionRouter) {
            tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        }
    }

    public void sendEventChallenges(String event, String category, String action, String label) {
        if (tracker == null)
            return;
        tracker.sendEventTracking(event, category, action, label);

    }


}