package com.tokopedia.tkpd.tkpdreputation.constant;
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
    public static final String PATH_SEND_REVIEW_VALIDATE = "review/api/"
            + REPUTATION_VERSION + "/insert/validate";
    public static final String PATH_SEND_REVIEW_SUBMIT = "review/api/"
            + REPUTATION_VERSION + "/insert/submit";
    public static final String PATH_SKIP_REVIEW = "review/api/" + REPUTATION_VERSION + "/skip";
    public static final String PATH_EDIT_REVIEW_VALIDATE = "review/api/"
            + REPUTATION_VERSION + "/edit/validate";
    public static final String PATH_EDIT_REVIEW_SUBMIT = "review/api/"
            + REPUTATION_VERSION + "/edit/submit";
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
    public static final String PATH_GET_REVIEW_PRODUCT_LIST = REPUTATIONAPP_REVIEW_API
            + REPUTATION_VERSION + "/product";
    public static final String PATH_GET_REVIEW_SHOP_LIST = REPUTATIONAPP_REVIEW_API
            + REPUTATION_VERSION + "/shop";
    public static final String PATH_GET_REVIEW_HELPFUL_LIST = REPUTATIONAPP_REVIEW_API
            + REPUTATION_VERSION + "/mosthelpful";
    public static final String PATH_GET_REVIEW_PRODUCT_RATING = REPUTATIONAPP_REVIEW_API
            + REPUTATION_VERSION + "/rating";

    // Tome
    public static final String PATH_IS_FAVORITE_SHOP = "v1/user/isfollowing";

    // fave shop
    public static final String PATH_FAVE_SHOP = "fav_shop.pl";
    public static final String URL_FAVE_SHOP_ACTION = BASE_DOMAIN + "v4/action/favorite-shop/";

}
