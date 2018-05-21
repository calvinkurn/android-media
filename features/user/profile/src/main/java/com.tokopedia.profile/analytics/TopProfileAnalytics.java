package com.tokopedia.profile.analytics;

/**
 * @author by milhamj on 08/03/18.
 */

public class TopProfileAnalytics {
    public static class Event {
        public static final String EVENT_CLICK_TOP_PROFILE = "clickTopProfile";
    }

    public static class Category {
        public static final String TOP_PROFILE = "Top Profile";
        public static final String KOL_TOP_PROFILE = "kol top profile";
    }

    public static class Action {
        public static final String CLICK_ON_COMPLETE_NOW = "Click on Lengkapi Sekarang";
        public static final String CLICK_ON_MANAGE_ACCOUNT = "Click on Atur Akun";
        public static final String CLICK_ON_FAVORITE = "Click on Favoritkan";
        public static final String CLICK_ON_UNFAVORITE = "Click on Unfavorite";
        public static final String CLICK_PROMPT = "click prompt";
    }

    public static class Label {
        public static final String GO_TO_PROFILE_FORMAT = "go to feed - %s";
    }
}
