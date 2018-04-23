package com.tokopedia.tkpd.tkpdcontactus.common.api;


/**
 * Created by sandeepgoyal on 15/12/17.
 */

public interface ContactUsURL {
    String BASE_URL ="https://www.tokopedia.com/";

    String POPULAR_ARTICLE = "/bantuan/wp-json/wp/v2/posts";
    String BUYER_LIST ="/contact-us/ws/order/buyer";
    String TICKET_OPTION_LIST = "contact-us/ws/problem/46/solutions";
    String TICKET_SUBMIT_STEP_1 = "contact-us/ws/create/step/1";
    String TICKET_SUBMIT_STEP_2 = "contact-us/ws/create/step/2";

}
