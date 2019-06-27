package com.tokopedia.challenges.view.analytics;

import android.app.Activity;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import javax.inject.Inject;

public class ChallengesGaAnalyticsTracker {

    public static final String EVENT_CATEGORY_CHALLENGES_PAST_CHALLENGES = "challenges_past challenges";
    public static final String EVENT_CATEGORY_CHALLENGES_MYSUBMISSIONS = "challenges_my submissions";
    public static final String EVENT_CLICK_SORT_BY = "clickSortby";
    public static final String EVENT_SORT_RECENT = "Recent";
    public static final String EVENT_SORT_BUZZ_POINT = "highest buzz point";
    public static final String EVENT_CATEGORY_CHALLENGES_DETAIL_PAGE_CHALLENEGE = "challenges_challenge detail page";
    public static final String EVENT_ACTION_COPY = "copy";
    public static final String EVENT_CHALLENGE_OTHER_SUBMISSION = "challenges_other submission"
            ;
    public static final String EVENT_CATEGORY_CHALLENGES_SUBMIT_POST = "challenges_submit post";
    public static final String EVENT_CATEGORY_CHALLENGES_SHARE = "challenges_share challenge";
    public static String EVENT_CATEGORY_CHALLENGES_ACTIVE_CHALLENEGES = "challenges_active challenges";
    public static String EVENT_VIEW_CHALLENGES = "viewChallenges";
    public static String EVENT_CLICK_CHALLENGES = "clickChallenges";
    public static String EVENT_CLICK_SHARE = "clickShare";
    public static String EVENT_CLICK_LIKE = "clickLike";

    public static String EVENT_CATEGORY_ACTIVE_CHALLENGES = "active challenges";
    public static String EVENT_CATEGORY_CHALLENGES = "challenges";
    public static String EVENT_CATEGORY_CHALLENGES_DETAIL_PAGE = "challenge detail page";
    public static String EVENT_CATEGORY_CHALLENGES_HOME_PAGE = "challenges_homepage";
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
    public static String EVENT_ACTION_PAGE_VIEW = "pageview";
    public static String EVENT_TNC = "tnc";
    public static String EVENT_BUZZ_POINT = "How to generate buzz point";
    public static String EVENT_SUBMIT = "submit";
    public static String EVENT_CANCEL = "cancel";

    @Inject
    public ChallengesGaAnalyticsTracker() {
    }

    public void sendEventChallenges(String event, String category, String action, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(event, category, action, label));
    }

    public void sendScreenEvent(Activity activity, String screen) {
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screen);
    }

}