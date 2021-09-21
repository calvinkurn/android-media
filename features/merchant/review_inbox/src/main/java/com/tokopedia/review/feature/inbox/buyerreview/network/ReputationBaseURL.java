package com.tokopedia.review.feature.inbox.buyerreview.network;
import com.tokopedia.url.TokopediaUrl;

public class ReputationBaseURL {

    public static String BASE_DOMAIN = TokopediaUrl.Companion.getInstance().getWS();

    public static final String URL_REPUTATION = BASE_DOMAIN + "reputationapp/";
    public static final String PATH_SEND_REPUTATION_SMILEY = "reputation/api/v1/insert";
    public static final String REPUTATIONAPP_REVIEW_API = "reputationapp/review/api/";
    private static final String REPUTATION_VERSION = "v1";
    public static final String PATH_GET_INBOX_REPUTATION = "reputation/api/"
            + REPUTATION_VERSION + "/inbox";
    public static final String PATH_GET_DETAIL_INBOX_REPUTATION = "review/api/"
            + REPUTATION_VERSION + "/list";
    public static final String PATH_REPORT_REVIEW = "review/api/"
            + REPUTATION_VERSION + "/report";
    public static final String PATH_DELETE_REVIEW_RESPONSE = "review/api/"
            + REPUTATION_VERSION + "/response/delete";
    public static final String PATH_INSERT_REVIEW_RESPONSE = "review/api/"
            + REPUTATION_VERSION + "/response/insert";
    public static final String PATH_GET_LIKE_DISLIKE_REVIEW = "review/api/"
            + REPUTATION_VERSION + "/likedislike";
    public static final String PATH_LIKE_DISLIKE_REVIEW = "review/api/"
            + REPUTATION_VERSION + "/likedislike";

    // Tome
    public static final String PATH_IS_FAVORITE_SHOP = "v1/user/isfollowing";

    public static final String PATH_SHOP = "v4/shop/";


    // upload
    public static final String V4_ACTION_GENERATE_HOST = "v4/action/generate-host/";

}
