package com.tokopedia.challenges.view.utils;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;

import java.util.HashMap;
/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public class ChallengesCacheHandler {
    private static final String CACHE_CHALLENGE_SUBMISSIN = "cache_challenge_submissin";
    public static boolean OPEN_CHALLENGES_LIST_CACHE;
    public static boolean MY_SUBMISSTIONS_LIST_CACHE;
    public static boolean CHALLENGES_SUBMISSTIONS_LIST_CACHE;
    public static boolean SUBMISSTION_DETAILS_CACHE;
    public static boolean CHALLENGES_DETAILS_CACHE;
    public static boolean CHALLENGES_ALL_SUBMISSTIONS_LIST_CACHE;
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

    public static String getLocalVideoPath(Context context, String submissionId){
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, CACHE_CHALLENGE_SUBMISSIN);
        return localCacheHandler.getString(submissionId);
    }

    public static void saveLocalVideoPath(Context context, String submissionId , String filePath){
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, CACHE_CHALLENGE_SUBMISSIN);
        localCacheHandler.putString(submissionId, filePath);
        localCacheHandler.applyEditor();
    }


}
