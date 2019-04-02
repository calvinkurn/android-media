package com.tokopedia.contactus.inboxticket2.data;


public interface InboxEndpoint {
    String LIST_TICKET = "/ws/contact-us/v2/inbox";
    String DETAIL_TICKET = "/ws/contact-us/v2/inbox/";
    String SEND_MESSAGE = "/ws/contact-us/v2/reply/step/1";
    String COMMENT_RATING = "/ws/contact-us/v2/rating";
    String SEND_MESSAGE_ATTACHMENT = "/ws/contact-us/v2/reply/step/2";
}
