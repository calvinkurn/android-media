package com.tokopedia.topchat.common.analytics;


import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

/**
 * Created by stevenfredian on 11/6/17.
 */

public class TopChatAnalytics {


    private final AnalyticTracker analyticTracker;

    @Inject
    public TopChatAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public static final String SCREEN_CHAT_LIST = "inbox-chat";

    public interface Category {
        public static final String PRODUCT_PAGE = "product page";
        public static final String SEND_MESSAGE_PAGE = "send message page";
        public static final String SHOP_PAGE = "shop page";
        public static final String INBOX_CHAT = "inbox-chat";
        public static final String CHAT_DETAIL = "chat detail";
        public static final String UPDATE_TEMPLATE = "update template";
        public static final String ADD_TEMPLATE = "add template";

        static String EVENT_CATEGORY_INBOX_CHAT = "inbox-chat";

    }

    public interface Name {
        public static final String PRODUCT_PAGE = "ClickProductDetailPage";
        public static final String SEND_MESSAGE_PAGE = "ClickMessageRoom";
        public static final String SHOP_PAGE = "ClickShopPage";
        public static final String INBOX_CHAT = "clickInboxChat";
        public static final String CHAT_DETAIL = "ClickChatDetail";

        String EVENT_NAME_CLICK_INBOXCHAT = "clickInboxChat";

    }

    public interface Action {
        public static final String PRODUCT_PAGE = "click";
        public static final String SEND_MESSAGE_PAGE = "click on kirim";
        public static final String SHOP_PAGE = "click on kirim pesan";
        public static final String INBOX_CHAT_CLICK = "click on chatlist";
        public static final String INBOX_CHAT_SEARCH = "search on chatlist";
        public static final String CHAT_DETAIL_SEND = "click on send button";
        public static final String CHAT_DETAIL_ATTACH = "click on attach image button";
        public static final String CHAT_DETAIL_INSERT = "click on insert button";
        public static final String CHAT_DETAIL_ATTACHMENT = "click on send product attachment";
        public static final String TEMPLATE_CHAT_CLICK = "click on template chat";
        public static final String UPDATE_TEMPLATE = "click on tambah template";
        public static final String CLICK_THUMBNAIL = "click on thumbnail";

        static final String EVENT_ACTION_CLICK_COMMUNITY_TAB = "click on community tab";

    }

    public interface Label {
        public static final String PRODUCT_PAGE = "message shop";
    }

    public void eventClickInboxChannel() {

        analyticTracker.sendEventTracking(
                Name.EVENT_NAME_CLICK_INBOXCHAT,
                Category.EVENT_CATEGORY_INBOX_CHAT,
                Action.EVENT_ACTION_CLICK_COMMUNITY_TAB,
                ""
        );
    }

    public void eventOpenTopChat() {

        analyticTracker.sendEventTracking(
                TopChatAnalytics.Name.INBOX_CHAT,
                TopChatAnalytics.Category.INBOX_CHAT,
                TopChatAnalytics.Action.INBOX_CHAT_CLICK,
                ""
        );
    }

    public void eventSearchSubmit() {
        analyticTracker.sendEventTracking(
                TopChatAnalytics.Name.INBOX_CHAT,
                TopChatAnalytics.Category.INBOX_CHAT,
                TopChatAnalytics.Action.INBOX_CHAT_SEARCH,
                ""
        );
    }


}
