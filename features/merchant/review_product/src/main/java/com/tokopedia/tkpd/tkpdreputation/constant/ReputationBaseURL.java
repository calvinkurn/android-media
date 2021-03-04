package com.tokopedia.tkpd.tkpdreputation.constant;
import com.tokopedia.url.TokopediaUrl;

public class ReputationBaseURL {

    public static String BASE_DOMAIN = TokopediaUrl.Companion.getInstance().getWS();

    public static final String URL_REPUTATION = BASE_DOMAIN + "reputationapp/";
    public static final String REPUTATIONAPP_REVIEW_API = "reputationapp/review/api/";
    private static final String REPUTATION_VERSION = "v1";
    public static final String PATH_DELETE_REVIEW_RESPONSE = "review/api/"
            + REPUTATION_VERSION + "/response/delete";
    public static final String PATH_GET_LIKE_DISLIKE_REVIEW = "review/api/"
            + REPUTATION_VERSION + "/likedislike";
    public static final String PATH_LIKE_DISLIKE_REVIEW = "review/api/"
            + REPUTATION_VERSION + "/likedislike";
    public static final String PATH_GET_REVIEW_PRODUCT_LIST = REPUTATIONAPP_REVIEW_API
            + REPUTATION_VERSION + "/product";
    public static final String PATH_GET_REVIEW_HELPFUL_LIST = REPUTATIONAPP_REVIEW_API
            + REPUTATION_VERSION + "/mosthelpful";
    public static final String PATH_GET_REVIEW_PRODUCT_RATING = REPUTATIONAPP_REVIEW_API
            + REPUTATION_VERSION + "/rating";

}
