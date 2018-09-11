package com.tokopedia.challenges.view.utils;

import java.util.HashMap;

public class ChallengesCacheHandler {
    public static boolean OPEN_CHALLENGES_LIST_CACHE;
    public static boolean MY_SUBMISSTIONS_LIST_CACHE;
    public static boolean CHALLENGES_SUBMISSTIONS_LIST_CACHE;
    public static boolean SUBMISSTION_DETAILS_CACHE;
    public static boolean CHALLENGES_DETAILS_CACHE;
    public static boolean CHALLENGES_ALL_SUBMISSTIONS_LIST_CACHE;
    public static HashMap<String, Boolean> DELETED_ELEMENTS_MAP = new HashMap<>();
    public static HashMap<String, Boolean> LIKE_UNLIKE_ELEMENTS_MAP = new HashMap<>();
    public static HashMap<String, Integer> MANIPULATED_ELEMENTS_MAP = new HashMap<>();

    public enum Manupulated {
        DELETE, LIKE, UNLIKE,NOTFOUND

    }

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

//    public static HashMap<String, Boolean> getDeletedElementsMap() {
//        return DELETED_ELEMENTS_MAP;
//    }
//
//    public static void addDeletedElementsMap(String key, boolean value) {
//        DELETED_ELEMENTS_MAP.put(key, value);
//    }
//
//    public static HashMap<String, Boolean> getLikeUnlikeElementsMap() {
//        return LIKE_UNLIKE_ELEMENTS_MAP;
//    }

    public static void addManipulatedMap(String key, int value) {
        MANIPULATED_ELEMENTS_MAP.put(key, value);
    }

    public static int getManipulatedValue(String key) {
        if (MANIPULATED_ELEMENTS_MAP.containsKey(key)) {
            return MANIPULATED_ELEMENTS_MAP.get(key);
        } else {
            return Manupulated.NOTFOUND.ordinal();
        }
    }

}
