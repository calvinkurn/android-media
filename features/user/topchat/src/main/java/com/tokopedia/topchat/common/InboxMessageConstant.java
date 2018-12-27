package com.tokopedia.topchat.common;

/**
 * Created by Nisie on 5/10/16.
 */
public interface InboxMessageConstant {

    int OPEN_DETAIL_MESSAGE = 1;

    String MUST_REFRESH = "MUST_REFRESH";

    String MESSAGE_ALL = "inbox-message";
    String MESSAGE_ARCHIVE = "inbox-message-archive";
    String MESSAGE_SENT = "inbox-message-sent";
    String MESSAGE_TRASH = "inbox-message-trash";

    String PARAM_NAV = "nav";
    String PARAM_ALL = "all";
    String PARAM_UNREAD = "unread";
    String PARAM_MESSAGE = "message";
    String PARAM_MESSAGE_ID = "message_id";
    String PARAM_POSITION = "position";
    String PARAM_SENT_MESSAGE = "sent_message";
    String PARAM_SENDER_NAME = "fullname";
    String PARAM_SENDER_TAG = "tag";
    String PARAM_SENDER_ID = "sender_id";
    String PARAM_SENDER_IMAGE = "image";
    String PARAM_MODE = "mode";
    String PARAM_KEYWORD = "keyword";

    int STATE_CHAT_UNREAD = 1;
    int STATE_CHAT_READ = 2;
    int STATE_CHAT_BOTH = 3;

    int RESEND = 1;
    int DELETE = 2;
}
