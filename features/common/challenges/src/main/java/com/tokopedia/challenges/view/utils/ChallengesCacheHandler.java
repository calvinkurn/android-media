package com.tokopedia.challenges.view.utils;

public class ChallengesCacheHandler {
    public static boolean OPEN_CHALLENGES_LIST_CACHE;
    public static boolean MY_SUBMISSTIONS_LIST_CACHE;
    public static boolean CHALLENGES_SUBMISSTIONS_LIST_CACHE;
    public static boolean SUBMISSTION_DETAILS_CACHE;
    public static boolean CHALLENGES_DETAILS_CACHE;

    public static void setCache() {
        OPEN_CHALLENGES_LIST_CACHE = true;
        MY_SUBMISSTIONS_LIST_CACHE = true;
        CHALLENGES_SUBMISSTIONS_LIST_CACHE = true;
        SUBMISSTION_DETAILS_CACHE = true;
        CHALLENGES_DETAILS_CACHE = true;
    }

    public static void resetOpenChallengesListCache() {
        OPEN_CHALLENGES_LIST_CACHE = false;
    }
    public static void resetMySubmissionsListCache() {
        MY_SUBMISSTIONS_LIST_CACHE = false;
    }
    public static void resetChallengeSubmissionssListCache() {
        CHALLENGES_SUBMISSTIONS_LIST_CACHE = false;
    }
    public static void resetSubmissionsDetailsCache() {
        SUBMISSTION_DETAILS_CACHE = false;
    }
    public static void resetChallengesDetailsCache() {
        CHALLENGES_DETAILS_CACHE = false;
    }
}
