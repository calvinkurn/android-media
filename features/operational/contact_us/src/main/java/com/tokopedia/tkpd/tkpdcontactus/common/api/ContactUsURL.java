package com.tokopedia.tkpd.tkpdcontactus.common.api;


import com.tokopedia.core.network.constants.TkpdBaseURL;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

public interface ContactUsURL {
    String BASE_URL = TkpdBaseURL.CONTACT_US_BASE;
    String TOPBOT_STATUS = "contact-us/api/v2/topbot/status";
    String POPULAR_ARTICLE = "/bantuan/wp-json/sf/v1/popular/?per_page=5";
    String BUYER_LIST ="/contact-us/ws/order/buyer";
    String TICKET_OPTION_LIST = "contact-us/ws/problem/46/solutions";
    String TICKET_SUBMIT_STEP_1 = "contact-us/ws/create/step/1";
    String TICKET_SUBMIT_STEP_2 = "contact-us/ws/create/step/2";

}
