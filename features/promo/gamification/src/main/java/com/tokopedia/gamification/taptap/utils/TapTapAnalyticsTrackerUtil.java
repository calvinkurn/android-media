package com.tokopedia.gamification.taptap.utils;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.track.TrackApp;

public class TapTapAnalyticsTrackerUtil {
    public interface EventKeys {
        String EVENT = "event";
        String EVENT_CATEGORY = "eventCategory";
        String EVENT_ACTION = "eventAction";
        String EVENT_LABEL = "eventLabel";
        String VIEW_GAME= "viewGame";
        String CLICK_GAME = "clickGame";
    }

    public interface CategoryKeys {
        String CATEGORY_TAP_TAP = "gaming - tap the egg";
    }

    public interface ActionKeys {
        String TAP_TAP_IMPRESSION = "tap the egg - impression on egg";
        String TAP_EGG_CLICK = "tap the egg - click ";
        String TAP_EGG_CLICKED = "tap the egg - click on egg";
        String REWARDS_IMPRESSION = "tap the egg - impression on rewards";
        String POPUP_AND_ERROR_CLICK = "pop up and error - click";
        String POPUP_AND_ERROR_IMPRESSION = "pop up and error - impression on error";
        String REWARD_SUMMARY_IMPRESSION = "reward summary - impression on reward summary page";
        String REWARD_SUMMARY_CLICK = "reward summary - click";
        String REWARD_SUMMARY_IMPRESSION_ON_ERROR_TOASTER = "reward summary - impression on Error Toaster";
        String EMPTY_STATE_IMPRESSION = "empty state - impression on empty state page";
        String EMPTY_STATE_CLICK = "empty state - click";

    }

    public interface LabelKeys{
        String PRESS_BACK_BUTTON ="back button";
        String PRESS_SHARE_BUTTON="share button";
    }

    public static void sendScreenEvent(Activity context, String screenName) {
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName);
    }

    public static void sendEvent(Context context, String event, String category,
                                 String action, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(event, category, action, label);
    }

}
