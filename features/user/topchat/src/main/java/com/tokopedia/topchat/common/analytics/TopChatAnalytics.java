package com.tokopedia.topchat.common.analytics;


import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.attachproduct.analytics.AttachProductAnalytics;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * Created by stevenfredian on 11/6/17.
 */

public class TopChatAnalytics {



    @Inject
    public TopChatAnalytics() {
    }

    private static final String EVENT_NAME = "event";
    private static final String EVENT_CATEGORY = "eventCategory";
    private static final String EVENT_ACTION = "eventAction";
    private static final String EVENT_LABEL = "eventLabel";
    private static final String ECOMMERCE = "ecommerce";
    public static final String SCREEN_CHAT_LIST = "inbox-chat";
    public static final String SCREEN_CHAT_ROOM = "chatroom";

    public static final String FPM_DETAIL_CHAT = "mp_detail_chat";
    public static final String FPM_CHAT_LIST = "mp_chat_list";

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
        String EVENT_NAME_PRODUCT_CLICK = "productClick";

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
        public static final String CLICK_PRODUCT_IMAGE = "click on product image";
        public static final String CLICK_THUMBNAIL = "click on thumbnail";

        static final String EVENT_ACTION_CLICK_COMMUNITY_TAB = "click on community tab";

    }

    public interface Label {
        public static final String PRODUCT_PAGE = "message shop";
    }

    public void eventClickInboxChannel() {

        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Name.EVENT_NAME_CLICK_INBOXCHAT,
                Category.EVENT_CATEGORY_INBOX_CHAT,
                Action.EVENT_ACTION_CLICK_COMMUNITY_TAB,
                ""
        ));
    }

    public void eventOpenTopChat() {

        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                TopChatAnalytics.Name.INBOX_CHAT,
                TopChatAnalytics.Category.INBOX_CHAT,
                TopChatAnalytics.Action.INBOX_CHAT_CLICK,
                ""
        ));
    }

    public void eventSearchSubmit() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                TopChatAnalytics.Name.INBOX_CHAT,
                TopChatAnalytics.Category.INBOX_CHAT,
                TopChatAnalytics.Action.INBOX_CHAT_SEARCH,
                ""
        ));
    }

    public void trackSendProductAttachment() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Name.CHAT_DETAIL,
                TopChatAnalytics.Category.CHAT_DETAIL,
                TopChatAnalytics.Action.CHAT_DETAIL_ATTACHMENT,
                ""
        ));
    }

    public void trackHeaderClicked() {

        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Name.INBOX_CHAT,
                "message room",
                "click header - shop icon",
                ""
        ));

    }

    public void trackClickImageAnnouncement(String blastId, @NotNull String attachmentId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Name.INBOX_CHAT,
                "inbox-chat",
                "click on thumbnail",
                String.format("%s - %s", blastId, attachmentId)
        ));
    }

    public void trackOpenChatSetting() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                ChatSettingsAnalytics.EVENT_NAME,
                ChatSettingsAnalytics.CHAT_OPEN_CATEGORY,
                ChatSettingsAnalytics.CHAT_SETTINGS_ACTION, ""));
    }

    public void eventPickImage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CHAT_DETAIL_ATTACH,
                ""));
    }

    public void eventAttachProduct() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CHAT_DETAIL_INSERT,
                ""));
    }

    public void eventSendMessage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CHAT_DETAIL_SEND,
                ""));
    }

    public void eventClickTemplate() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Name.INBOX_CHAT,
                Category.INBOX_CHAT,
                Action.TEMPLATE_CHAT_CLICK,
                ""));
    }

    public void trackClickUnblockChat(int shopId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                ChatSettingsAnalytics.EVENT_NAME,
                ChatSettingsAnalytics.CHAT_OPEN_CATEGORY,
                ChatSettingsAnalytics.CHAT_ENABLE_TEXT_LINK_ACTION,
                String.valueOf(shopId)));
    }

    public void eventClickProductThumbnailEE(int blastId, String productId, String productName,
                                             int productPrice, String category, String variant) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(DataLayer.mapOf(
                EVENT_NAME, Name.EVENT_NAME_PRODUCT_CLICK,
                EVENT_CATEGORY, Category.CHAT_DETAIL,
                EVENT_ACTION, Action.CLICK_PRODUCT_IMAGE,
                EVENT_LABEL, String.format("chat - %s - %s", productId, String.valueOf(blastId)),
                ECOMMERCE, DataLayer.mapOf("currencyCode", "IDR",
                        "click", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", "/chat"),
                                "products", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "name", productName,
                                                "id", productId,
                                                "price", productPrice,
                                                "brand", "none",
                                                "category", category,
                                                "variant", variant,
                                                "position", 0
                                        )
                                )
                        )
                )
        ));
    }


    public void trackProductAttachmentClicked() {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                AttachProductAnalytics.getEventClickChatAttachedProductImage().getEvent()
        );
    }
}
