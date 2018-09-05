package com.tokopedia.challenges.view.utils;

public class ChallengesCacheHandler {
    public static boolean OPEN_CHALLENGES_LIST_CACHE;
    public static boolean MY_SUBMISSTIONS_LIST_CACHE;
    public static boolean CHALLENGES_SUBMISSTIONS_LIST_CACHE;
    public static boolean SUBMISSTION_DETAILS_CACHE;
    public static boolean CHALLENGES_DETAILS_CACHE;
    public static boolean CHALLENGES_ALL_SUBMISSTIONS_LIST_CACHE;


    public static void resetCache() {
        OPEN_CHALLENGES_LIST_CACHE = true;
        MY_SUBMISSTIONS_LIST_CACHE = true;
        CHALLENGES_SUBMISSTIONS_LIST_CACHE = true;
        SUBMISSTION_DETAILS_CACHE = true;
        CHALLENGES_DETAILS_CACHE = true;
        CHALLENGES_ALL_SUBMISSTIONS_LIST_CACHE = true;
    }

    public static void setOpenChallengesListCache() {
        OPEN_CHALLENGES_LIST_CACHE = false;
    }

    public static void setMySubmissionsListCache() {
        MY_SUBMISSTIONS_LIST_CACHE = false;
    }

    public static void setChallengeSubmissionssListCache() {
        CHALLENGES_SUBMISSTIONS_LIST_CACHE = false;
    }

    public static void setSubmissionsDetailsCache() {
        SUBMISSTION_DETAILS_CACHE = false;
    }

    public static void setChallengesDetailsCache() {
        CHALLENGES_DETAILS_CACHE = false;
    }

    public static void setChallengeAllSubmissionssListCache() {
        CHALLENGES_ALL_SUBMISSTIONS_LIST_CACHE = false;
    }
}
