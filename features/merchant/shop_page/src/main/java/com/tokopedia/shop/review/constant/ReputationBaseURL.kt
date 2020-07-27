package com.tokopedia.shop.review.constant

import com.tokopedia.url.TokopediaUrl.Companion.getInstance

object ReputationBaseURL {
    var BASE_DOMAIN = getInstance().WS
    val URL_REPUTATION = BASE_DOMAIN + "reputationapp/"
    const val PATH_SEND_REPUTATION_SMILEY = "reputation/api/v1/insert"
    const val REPUTATIONAPP_REVIEW_API = "reputationapp/review/api/"
    private const val REPUTATION_VERSION = "v1"
    const val PATH_GET_INBOX_REPUTATION = ("reputation/api/"
            + REPUTATION_VERSION + "/inbox")
    const val PATH_GET_DETAIL_INBOX_REPUTATION = ("review/api/"
            + REPUTATION_VERSION + "/list")
    const val PATH_SEND_REVIEW_VALIDATE = ("review/api/"
            + REPUTATION_VERSION + "/insert/validate")
    const val PATH_SEND_REVIEW_SUBMIT = ("review/api/"
            + REPUTATION_VERSION + "/insert/submit")
    const val PATH_SKIP_REVIEW = "review/api/$REPUTATION_VERSION/skip"
    const val PATH_EDIT_REVIEW_VALIDATE = ("review/api/"
            + REPUTATION_VERSION + "/edit/validate")
    const val PATH_EDIT_REVIEW_SUBMIT = ("review/api/"
            + REPUTATION_VERSION + "/edit/submit")
    const val PATH_REPORT_REVIEW = ("review/api/"
            + REPUTATION_VERSION + "/report")
    const val PATH_DELETE_REVIEW_RESPONSE = ("review/api/"
            + REPUTATION_VERSION + "/response/delete")
    const val PATH_INSERT_REVIEW_RESPONSE = ("review/api/"
            + REPUTATION_VERSION + "/response/insert")
    const val PATH_GET_LIKE_DISLIKE_REVIEW = ("review/api/"
            + REPUTATION_VERSION + "/likedislike")
    const val PATH_LIKE_DISLIKE_REVIEW = ("review/api/"
            + REPUTATION_VERSION + "/likedislike")
    const val PATH_GET_REVIEW_PRODUCT_LIST = (REPUTATIONAPP_REVIEW_API
            + REPUTATION_VERSION + "/product")
    const val PATH_GET_REVIEW_SHOP_LIST = (REPUTATIONAPP_REVIEW_API
            + REPUTATION_VERSION + "/shop")
    const val PATH_GET_REVIEW_HELPFUL_LIST = (REPUTATIONAPP_REVIEW_API
            + REPUTATION_VERSION + "/mosthelpful")
    const val PATH_GET_REVIEW_PRODUCT_RATING = (REPUTATIONAPP_REVIEW_API
            + REPUTATION_VERSION + "/rating")
    // Tome
    const val PATH_IS_FAVORITE_SHOP = "v1/user/isfollowing"
    // fave shop
    const val PATH_FAVE_SHOP = "fav_shop.pl"
    val URL_FAVE_SHOP_ACTION = BASE_DOMAIN + "v4/action/favorite-shop/"
    // shop
    const val PATH_DELETE_REP_REVIEW_RESPONSE = "delete_reputation_review_response.pl"
    const val PATH_INSERT_REP_REVIEW_RESPONSE = "insert_reputation_review_response.pl"
    val URL_REPUTATION_ACTION = BASE_DOMAIN + "v4/action/reputation/"
    const val PATH_GET_SHOP_ETALASE = "get_shop_etalase.pl"
    const val PATH_GET_SHOP_INFO = "get_shop_info.pl"
    const val PATH_GET_PEOPLE_FAV_MY_SHOP = "get_people_who_favorite_myshop.pl"
    const val PATH_GET_SHOP_LOCATION = "get_shop_location.pl"
    const val PATH_GET_SHOP_NOTES = "get_shop_notes.pl"
    const val PATH_GET_SHOP_PRODUCT = "get_shop_product.pl"
    const val PATH_GET_SHOP_REVIEW = "get_shop_review.pl"
    const val PATH_SHOP = "v4/shop/"
    val URL_SHOP = BASE_DOMAIN + PATH_SHOP
    // product
    const val PATH_LIKE_DISLIKE_REVIEW_PRODUCT = "like_dislike_review.pl"
    const val PATH_REPORT_REVIEW_PRODUCT = "report_review.pl"
    val URL_REVIEW_ACTION = BASE_DOMAIN + "v4/action/review/"
    // upload
    const val V4_ACTION_GENERATE_HOST = "v4/action/generate-host/"
    val URL_GENERATE_HOST_ACTION = BASE_DOMAIN + V4_ACTION_GENERATE_HOST
    const val PATH_GENERATE_HOST = "generate_host.pl"
}