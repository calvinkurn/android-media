package com.tokopedia.contactus.common.api;


import com.tokopedia.url.TokopediaUrl;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

public interface ContactUsURL {
    String BASE_URL = TokopediaUrl.Companion.getInstance().getWEB();
    String TOPBOT_STATUS = "contact-us/ws/topbot/status";
    String POPULAR_ARTICLE = "/bantuan/wp-json/sf/v1/popular/?per_page=5";
    String BUYER_LIST ="/contact-us/ws/order/buyer";
    String SELLER_LIST ="contact-us/ws/order/seller";
    String TICKET_OPTION_LIST = "contact-us/ws/problem/46/solutions";
    String TICKET_SUBMIT_STEP_1 = "contact-us/ws/create/step/1";
    String TICKET_SUBMIT_STEP_2 = "contact-us/ws/create/step/2";
    String ARTICLE_POPULAR_URL = "tokopedia://webview?url=https://www.tokopedia.com/bantuan/";
    String NAVIGATE_NEXT_URL = "https://www.tokopedia.com/contact-us?flag_app=1&utm_source=android#step1";
    String TOP_BOT_BASE_URL = "tokopedia://topchat/";
    String PATH_GET_SOLUTION = "contact-us/ajax/solution/{id}";
    String PATH_CREATE_STEP_2 = "contact-us/ajax/create/step/2";
    String PATH_CREATE_STEP_1 = "contact-us/ajax/create/step/1";
    String PATH_COMMENT_RATING = "contact-us/ws/contact-us/rating";
    String CONTENT_BASE_URL = "help/faq/";
}
