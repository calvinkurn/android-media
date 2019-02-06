package com.tokopedia.challenges.view.analytics;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.challenges.ChallengesModuleRouter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */
public class ChallengesMoengageAnalyticsTracker {

    public static String Challenge_Screen_Launched = "Challenge_Screen_Launched";
    public static String Challenge_Detail_Open = "Challenge_Detail_Opened";
    public static String Challenge_Post_Opened = "Challenge_Post_Opened";
    public static String Challenge_Post_Shared = "Challenge_Post_Shared";
    public static String Challenge_Delete_Submission = "Challenge_Delete_Submission";
    public static String Challenge_Submit_Start = "Challenge_Submit_Start";
    public static String Challenge_Submit_Finished = "Challenge_Submit_Finished";

    public static String screen_name = "screen_name";
    public static String challenge_name = "challenge_name";
    public static String challenge_id = "challenge_id";
    public static String challenge_joined = "challenge_joined";
    public static String post_id = "post_id";
    public static String channel = "channel";



    public static void challengeScreenLaunched(Activity activity, String screenName) {
        Map<String, Object> values = new HashMap<>();
        values.put(screen_name, screenName);
        sendEvent(activity, Challenge_Screen_Launched, values);
    }

    public static void challengeDetailsOpen(Activity activity, String challengeName, String challengeId, boolean is_participated) {
        Map<String, Object> values = new HashMap<>();
        values.put(challenge_name, challengeName);
        values.put(challenge_id, challengeId);
        values.put(challenge_joined, is_participated);
        sendEvent(activity, Challenge_Detail_Open, values);
    }

    public static void challengePostOpen(Activity activity, String challengeName, String challengeId, String postId, boolean is_participated) {
        Map<String, Object> values = new HashMap<>();
        values.put(challenge_name, challengeName);
        values.put(challenge_id, challengeId);
        values.put(post_id, postId);
        values.put(challenge_joined, is_participated);
        sendEvent(activity, Challenge_Post_Opened, values);
    }

    public static void challengePostShared(Activity activity, String challengeName, String challengeId, String postId, boolean is_participated, String channelName) {
        Map<String, Object> values = new HashMap<>();
        values.put(challenge_name, challengeName);
        values.put(challenge_id, challengeId);
        values.put(post_id, postId);
        values.put(challenge_joined, is_participated);
        values.put(channel, channelName);
        sendEvent(activity, Challenge_Post_Shared, values);
    }

    public static void challenge_DeleteSubmission(Activity activity, String challengeName, String challengeId, String postId) {
        Map<String, Object> values = new HashMap<>();
        values.put(challenge_name, challengeName);
        values.put(challenge_id, challengeId);
        values.put(post_id, postId);
        sendEvent(activity, Challenge_Delete_Submission, values);
    }

    public static void challengeSubmitStart(Activity activity, String challengeName, String challengeId, String postId) {
        Map<String, Object> values = new HashMap<>();
        values.put(challenge_name, challengeName);
        values.put(challenge_id, challengeId);
        values.put(post_id, postId);
        sendEvent(activity, Challenge_Submit_Start, values);
    }

    public static void challengeSubmitFinished(Activity activity, String challengeName, String challengeId, String postId) {
        Map<String, Object> values = new HashMap<>();
        values.put(challenge_name, challengeName);
        values.put(challenge_id, challengeId);
        values.put(post_id, postId);
        sendEvent(activity, Challenge_Submit_Finished, values);
    }


    public static void sendEvent(Activity activity, String name, Map<String, Object> values) {
        ((ChallengesModuleRouter) (activity.getApplication())).sendMoengageEvents(name, values);
    }
}
