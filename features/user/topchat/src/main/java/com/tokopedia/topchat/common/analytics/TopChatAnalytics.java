package com.tokopedia.topchat.common.analytics;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.collection.ArrayMap;

import com.tokopedia.abstraction.processor.ProductListClickBundler;
import com.tokopedia.abstraction.processor.ProductListClickProduct;
import com.tokopedia.abstraction.processor.ProductListImpressionBundler;
import com.tokopedia.abstraction.processor.ProductListImpressionProduct;
import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.atc_common.domain.model.response.DataModel;
import com.tokopedia.chat_common.data.AttachInvoiceSentViewModel;
import com.tokopedia.chat_common.data.BannedProductAttachmentViewModel;
import com.tokopedia.chat_common.data.ProductAttachmentViewModel;
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.ChatOrderProgress;
import com.tokopedia.topchat.chatroom.view.uimodel.ReviewUiModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.InvoicePreviewUiModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.QuotationUiModel;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_PARAM_CATEGORY_ID;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_PARAM_DIMENSION_38;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_PARAM_DIMENSION_45;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_PARAM_DIMENSION_83;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_PARAM_ITEM_BRAND;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_PARAM_ITEM_CATEGORY;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_PARAM_ITEM_ID;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_PARAM_ITEM_NAME;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_PARAM_ITEM_VARIANT;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_PARAM_PICTURE;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_PARAM_PRICE;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_PARAM_QUANTITY;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_PARAM_SHOP_ID;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_PARAM_SHOP_NAME;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_PARAM_SHOP_TYPE;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_PARAM_URL;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_VALUE_BEBAS_ONGKIR;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_VALUE_ITEMS;
import static com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics.EE_VALUE_NONE_OTHER;

/**
 * Created by stevenfredian on 11/6/17.
 */

public class TopChatAnalytics {

    private String sourcePage = "";

    @Inject
    public TopChatAnalytics() {
    }

    private static final String EVENT_NAME = "event";
    private static final String EVENT_CATEGORY = "eventCategory";
    private static final String EVENT_ACTION = "eventAction";
    private static final String EVENT_LABEL = "eventLabel";
    private static final String KEY_BUSINESS_UNIT = "businessUnit";
    private static final String KEY_CURRENT_SITE = "currentSite";
    private static final String USER_ID = "userId";
    private static final String ECOMMERCE = "ecommerce";
    public static final String SCREEN_CHAT_LIST = "inbox-chat";
    public static final String SCREEN_CHAT_ROOM = "chatroom";

    public static final String FPM_DETAIL_CHAT = "mp_detail_chat";
    public static final String FPM_DETAIL_CHAT_SELLERAPP = "mp_detail_chat_sellerapp";
    public static final String FPM_CHAT_LIST = "mp_chat_list";
    public static final String FPM_CHAT_LIST_SELLERAPP = "mp_chat_list_sellerapp";

    public static final String SELLERAPP_PUSH_NOTIF = "sellerapp_push_notif";

    public interface Category {
        public static final String PRODUCT_PAGE = "product page";
        public static final String SEND_MESSAGE_PAGE = "send message page";
        public static final String SHOP_PAGE = "shop page";
        public static final String INBOX_CHAT = "inbox-chat";
        public static final String CHAT_DETAIL = "chat detail";
        public static final String UPDATE_TEMPLATE = "update template";
        public static final String ADD_TEMPLATE = "add template";
        String MESSAGE_ROOM = "message room";
        String INBOX_CHAT_2 = "inbox chat";

        static String EVENT_CATEGORY_INBOX_CHAT = "inbox-chat";
        String PUSH_NOTIFICATION = "push notification";

    }

    public interface Name {
        public static final String PRODUCT_PAGE = "ClickProductDetailPage";
        public static final String SEND_MESSAGE_PAGE = "ClickMessageRoom";
        public static final String SHOP_PAGE = "ClickShopPage";
        public static final String INBOX_CHAT = "clickInboxChat";
        public static final String CHAT_DETAIL = "clickChatDetail";
        public static final String VIEW_CHAT_DETAIL = "viewChatDetailIris";
        String VIEW_INBOX_CHAT_IRIS = "viewInboxChatIris";

        String EVENT_NAME_CLICK_INBOXCHAT = "clickInboxChat";
        String EVENT_NAME_PRODUCT_CLICK = "productClick";
        String EVENT_NAME_ATC = "addToCart";
        String EVENT_NAME_PRODUCT_PREVIEW = "productView";

        String CLICK_CHAT_DETAIL = "ClickChatDetail";
        String ATC = "add_to_cart";
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
        public static final String CLICK_PRODUCT_IMAGE = "click on product thumbnail";
        public static final String VIEW_PRODUCT_PREVIEW = "view on product thumbnail";
        public static final String CLICK_THUMBNAIL = "click on thumbnail";
        public static final String CLICK_COPY_VOUCHER_THUMBNAIL = "click copy on shop voucher thumbnail";
        public static final String CLICK_VOUCHER_THUMBNAIL = "click shop voucher thumbnail";
        public static final String CLICK_ATC_PRODUCT_THUMBNAIL = "click atc on product thumbnail";
        public static final String CLICK_BUY_PRODUCT_THUMBNAIL = "click buy on product thumbnail";
        public static final String SENT_INVOICE_ATTACHMENT = "click kirim after attach invoice";
        public static final String CLICK_SEE_BUTTON_ON_ATC_SUCCESS_TOASTER = "click lihat button on atc success toaster";
        public static final String CLICK_ADD_ATTACHMENT = "click add attachment";
        public static final String CLICK_IMAGE_ATTACHMENT = "click on image on chat";
        public static final String CLICK_INVOICE_ATTACHMENT = "click invoice on chat detail";
        public static final String CLICK_REPORT_USER = "click report user on chat";
        public static final String CLICK_BANNED_PRODUCT = "click on lanjut browser";
        public static final String VIEW_BANNED_PRODUCT = "view banned product bubble";
        static final String EVENT_ACTION_CLICK_COMMUNITY_TAB = "click on community tab";
        String CLICK_HEADER_SHOP = "click - shop - login";
        String CLICK_ADD_TO_WISHLIST = "add wishlist - chat";
        String CLICK_REMOVE_FROM_WISHLIST = "remove wishlist - chat";
        String CLICK_REPLY_BUTTON = "click on reply button";
        String CLICK_QUOTATION_ATTACHMENT = "click bayar on quotation thumbnail";
        String CLICK_IMAGE_THUMBNAIL = "click image on product thumbnail ";
        String CLICK_OP_CARD_DESCRIPTION = "click on order progress card";
        String CLICK_OP_CTA_DESCRIPTION = "click cta on order progress card";
        String CLICK_OP_ORDER_HISTORY = "click on order history";
        String VIEW_ORDER_PROGRESS_WIDGET = "view on order progress widget";
        String CLICK_PRODUCT_REAL_IMAGE = "click on product image";
        String VIEW_REVIEW_REMINDER_WIDGET = "view on review reminder widget";
        String CLICK_REVIEW_REMINDER_WIDGET = "click on review reminder widget";
        String VIEW_SRW = "view smart reply widget";
    }

    public interface Label {
        public static final String PRODUCT_PAGE = "message shop";
        public static final String FOLLOW_SHOP = "follow shop";
        public static final String UNFOLLOW_SHOP = "unfollow shop";
        String BUYER = "buyer";
    }

    interface BusinessUnit {
        String Communication = "communication";
    }

    interface CurrentSite {
        String TokopediaMarketplace = "tokopediamarketplace";
    }

    public void setSourcePage(String sourcePage) {
        this.sourcePage = sourcePage;
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

    public void trackHeaderClicked(long shopId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_HEADER_SHOP,
                String.valueOf(shopId)
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

    public void trackClickUnblockChat(long shopId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                ChatSettingsAnalytics.EVENT_NAME,
                ChatSettingsAnalytics.CHAT_OPEN_CATEGORY,
                ChatSettingsAnalytics.CHAT_ENABLE_TEXT_LINK_ACTION,
                String.valueOf(shopId)));
    }

    //#AV4
    public void eventVoucherCopyClicked(@NotNull String voucherCode) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_COPY_VOUCHER_THUMBNAIL,
                voucherCode
        );
    }

    //#AV5
    public void eventVoucherThumbnailClicked() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_VOUCHER_THUMBNAIL,
                ""
        );
    }


    public void eventFollowUnfollowShop(boolean actionFollow, String shopId) {
        String label = Label.FOLLOW_SHOP;
        if (!actionFollow) {
            label = Label.UNFOLLOW_SHOP;
        }

        label = String.format("%s - %s", label, shopId);

        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Name.INBOX_CHAT,
                "message room",
                "click header - three bullet",
                label
        ));
    }

    public static final int PRODUCT_INDEX = 1;

    // #AP6
    public void eventClickProductThumbnailEE(
            Context context,
            @NotNull ProductAttachmentViewModel product,
            @NotNull UserSessionInterface user
    ) {

        ArrayList<ProductListClickProduct> products = new ArrayList<>();
        ProductListClickProduct topChatProduct = new ProductListClickProduct(
                product.getIdString(),
                product.getProductName(),
                product.getCategory(),
                product.getVariants().toString(),
                null,
                product.getPriceInt(),
                null,
                getFrom(product),
                PRODUCT_INDEX,
                new ArrayMap<>()
        );
        products.add(topChatProduct);

        Bundle bundle = ProductListClickBundler.getBundle(
                getFrom(product),
                products,
                Category.CHAT_DETAIL,
                Action.CLICK_PRODUCT_IMAGE,
                ProductListClickBundler.KEY,
                null,
                null,
                null
        );
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                ProductListClickBundler.KEY, bundle
        );
    }

    // #AP5
    public void eventSeenProductAttachment(
            Context context,
            @NotNull ProductAttachmentViewModel product,
            @NotNull UserSessionInterface user
    ) {
        ArrayList<com.tokopedia.abstraction.processor.beta.ProductListImpressionProduct> products = new ArrayList<>();
        com.tokopedia.abstraction.processor.beta.ProductListImpressionProduct product1 = new com.tokopedia.abstraction.processor.beta.ProductListImpressionProduct(
                product.getIdString(),
                product.getProductName(),
                null,
                product.getCategory(),
                product.getVariants().toString(),
                product.getPriceInt() + 0.0,
                null,
                PRODUCT_INDEX,
                getFrom(product),
                getFrom(product),
                null,
                null,
                new ArrayMap<>()
        );
        products.add(product1);

        Bundle bundle = com.tokopedia.abstraction.processor.beta.ProductListImpressionBundler.getBundle(
                getFrom(product),
                products,
                null,
                ProductListImpressionBundler.KEY,
                Category.CHAT_DETAIL,
                Action.VIEW_PRODUCT_PREVIEW,
                null,
                null,
                new ArrayMap<>()
        );
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                ProductListImpressionBundler.KEY, bundle
        );
    }

    // #AP5
    public void eventSeenProductAttachmentBeta(
            Context context,
            @NotNull ProductAttachmentViewModel product,
            @NotNull UserSessionInterface user
    ) {
        String devConst = "-dev";

        ArrayList<ProductListImpressionProduct> products = new ArrayList<>();
        ProductListImpressionProduct product1 = new ProductListImpressionProduct(
                product.getIdString(),
                product.getProductName(),
                null,
                product.getCategory(),
                product.getVariants().toString(),
                product.getPriceInt() + 0.0,
                null,
                PRODUCT_INDEX,
                getFrom(product),
                getFrom(product),
                null,
                null
        );
        products.add(product1);

        Bundle bundle = ProductListImpressionBundler.getBundle(
                getFrom(product),
                products,
                null,
                ProductListImpressionBundler.KEY,
                Category.CHAT_DETAIL + devConst,
                Action.VIEW_PRODUCT_PREVIEW + devConst,
                null,
                null
        );
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                ProductListImpressionBundler.KEY, bundle
        );
    }

    private String getFrom(ProductAttachmentViewModel product) {
        String blastId = product.getStringBlastId();
        if (!sourcePage.isEmpty() && sourcePage.equals(ApplinkConst.Chat.SOURCE_CHAT_SEARCH)) {
            return "/chat - search chat";
        } else {
            return "/" + getField(blastId);
        }
    }

    public void trackProductAttachmentClicked() {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getEventClickChatAttachedProductImage()
        );
    }

    private Map<String, Object> getEventClickChatAttachedProductImage() {
        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put("event", Name.CLICK_CHAT_DETAIL);
        eventTracking.put("eventCategory", Category.CHAT_DETAIL);
        eventTracking.put("eventAction", Action.CLICK_PRODUCT_REAL_IMAGE);
        eventTracking.put("eventLabel", "");

        return eventTracking;
    }

    // #AP7
    public void eventClickAddToCartProductAttachment(
            @NotNull ProductAttachmentViewModel product,
            @NotNull UserSessionInterface user
    ) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(DataLayer.mapOf(
                EVENT_NAME, Name.CHAT_DETAIL,
                EVENT_CATEGORY, Category.CHAT_DETAIL,
                EVENT_ACTION, Action.CLICK_ATC_PRODUCT_THUMBNAIL,
                EVENT_LABEL, String.format("%s - %s", getField(product.getStringBlastId()), product.getStringBlastId()),
                USER_ID, user.getUserId()
        ));
    }

    // #AP9
    public void eventClickBuyProductAttachment(
            @NotNull ProductAttachmentViewModel product
    ) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(DataLayer.mapOf(
                EVENT_NAME, Name.CHAT_DETAIL,
                EVENT_CATEGORY, Category.CHAT_DETAIL,
                EVENT_ACTION, Action.CLICK_BUY_PRODUCT_THUMBNAIL,
                EVENT_LABEL, String.format("%s - %s", getField(product.getStringBlastId()), product.getStringBlastId())
        ));
    }

    public void invoiceAttachmentSent(@NotNull InvoicePreviewUiModel invoice) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT_NAME, Name.CHAT_DETAIL,
                        EVENT_CATEGORY, Category.CHAT_DETAIL,
                        EVENT_ACTION, Action.SENT_INVOICE_ATTACHMENT,
                        EVENT_LABEL, invoice.getId()
                )
        );
    }

    public String getField(String blastId) {
        Long blastIdNum = Long.valueOf(blastId);
        if (blastIdNum == 0) {
            return "chat";
        } else if (blastIdNum == -1) {
            return "drop price alert";
        } else if (blastIdNum == -2) {
            return "limited stock";
        } else if (blastIdNum > 0) {
            return "broadcast";
        } else {
            return "chat";
        }
    }

    public void eventClickSeeButtonOnAtcSuccessToaster() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_SEE_BUTTON_ON_ATC_SUCCESS_TOASTER,
                ""
        );
    }


    public void trackChatMenuClicked(@NotNull String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_ADD_ATTACHMENT,
                label
        );
    }

    public void trackClickImageUpload() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_IMAGE_ATTACHMENT,
                ""
        );
    }


    public void trackClickInvoice(@NotNull AttachInvoiceSentViewModel invoice) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_INVOICE_ATTACHMENT,
                invoice.getInvoiceId()
        );
    }

    // #RC1
    public void eventClickReportUser(String opponentId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_REPORT_USER,
                opponentId
        );
    }

    // #BP1
    public void eventClickBannedProduct(@NotNull BannedProductAttachmentViewModel viewModel) {
        String clientId = TrackApp.getInstance().getGTM().getCachedClientIDString();
        String eventLabel = viewModel.getProductId() + " - " + clientId;
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_BANNED_PRODUCT,
                eventLabel
        );
    }

    // #BP2
    public void eventSeenBannedProductAttachment(@NotNull BannedProductAttachmentViewModel viewModel) {
        String clientId = TrackApp.getInstance().getGTM().getCachedClientIDString();
        String eventLabel = viewModel.getProductId() + " - " + clientId;
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Name.VIEW_CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.VIEW_BANNED_PRODUCT,
                eventLabel
        );
    }

    // #AP3
    public void eventClickAddToWishList(@NotNull String productId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_ADD_TO_WISHLIST,
                productId
        );
    }

    // #AP4
    public void eventClickRemoveFromWishList(@NotNull String productId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_REMOVE_FROM_WISHLIST,
                productId
        );
    }

    public void eventClickReplyChatFromNotif(String userId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put(EVENT_NAME, Name.CHAT_DETAIL);
        payload.put(EVENT_CATEGORY, Category.PUSH_NOTIFICATION);
        payload.put(EVENT_ACTION, Action.CLICK_REPLY_BUTTON);
        payload.put(EVENT_LABEL, "");
        payload.put(USER_ID, userId);
        payload.put("source", SELLERAPP_PUSH_NOTIF);

        TrackApp.getInstance().getGTM().sendGeneralEvent(payload);
    }

    // #QT1
    public void eventClickQuotation(@NotNull QuotationUiModel msg) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_QUOTATION_ATTACHMENT,
                msg.getQuotationId()
        );
    }

    // #AP11
    public void eventClickProductThumbnail(@NotNull ProductAttachmentViewModel product) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_IMAGE_THUMBNAIL,
                getField(String.valueOf(product.getBlastId())) + " - " + product.getBlastId()
        );
    }

    // #OP1
    public void eventClickOrderProgressCardDescription() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_OP_CARD_DESCRIPTION,
                Label.BUYER
        );
    }

    // #OP2, #OP3, #OP5
    public void eventClickCtaButton(@NotNull ChatOrderProgress chatOrder) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_OP_CTA_DESCRIPTION,
                Label.BUYER + " - " + chatOrder.getCtaType()
        );
    }

    // #OP6
    public void eventClickOrderProgressBuyAgain() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Name.CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_OP_ORDER_HISTORY,
                Label.BUYER
        );
    }

    // #OP9
    public void eventViewOrderProgress(@NotNull ChatOrderProgress chatOrder) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                Name.VIEW_CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.VIEW_ORDER_PROGRESS_WIDGET,
                "buyer - " + chatOrder.getStatus()
        );
    }

    // #RR1
    public void trackReviewCardImpression(
            @NotNull ReviewUiModel element,
            boolean isSeller,
            String userId
    ) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                createGeneralEvent(
                        Name.VIEW_INBOX_CHAT_IRIS,
                        Category.INBOX_CHAT_2,
                        Action.VIEW_REVIEW_REMINDER_WIDGET,
                        element.getEventLabel(isSeller),
                        BusinessUnit.Communication,
                        CurrentSite.TokopediaMarketplace,
                        userId
                )
        );
    }

    // #RR2
    public void trackReviewCardClick(
            @NotNull ReviewUiModel element,
            boolean isSeller,
            String userId
    ) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                createGeneralEvent(
                        Name.EVENT_NAME_CLICK_INBOXCHAT,
                        Category.INBOX_CHAT_2,
                        Action.CLICK_REVIEW_REMINDER_WIDGET,
                        element.getEventLabel(isSeller),
                        BusinessUnit.Communication,
                        CurrentSite.TokopediaMarketplace,
                        userId
                )
        );
    }

    public void eventViewSrw(long shopId, String userId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                createGeneralEvent(
                        Name.VIEW_CHAT_DETAIL,
                        Category.CHAT_DETAIL,
                        Action.VIEW_SRW,
                        "buyer - " + shopId + " - " + userId,
                        BusinessUnit.Communication,
                        CurrentSite.TokopediaMarketplace,
                        userId
                )
        );
    }

    public void trackSuccessDoBuyAndAtc(
            ProductAttachmentViewModel element,
            DataModel data,
            String shopName,
            String eventAction
    ) {
        String dimen83 = "";
        if (element.hasFreeShipping()) {
            dimen83 = EE_VALUE_BEBAS_ONGKIR;
        } else {
            dimen83 = EE_VALUE_NONE_OTHER;
        }
        Bundle itemBundle = new Bundle();
        itemBundle.putString(
                EE_PARAM_ITEM_ID,
                setValueOrDefault(String.valueOf(data.getProductId()))
        );
        itemBundle.putString(EE_PARAM_ITEM_NAME, setValueOrDefault(element.getProductName()));
        itemBundle.putString(EE_PARAM_ITEM_BRAND, setValueOrDefault(""));
        itemBundle.putString(EE_PARAM_ITEM_CATEGORY, setValueOrDefault(element.getCategory()));
        itemBundle.putString(EE_PARAM_ITEM_VARIANT, setValueOrDefault(""));
        itemBundle.putString(
                EE_PARAM_SHOP_ID, setValueOrDefault(String.valueOf(data.getShopId()))
        );
        itemBundle.putString(EE_PARAM_SHOP_NAME, setValueOrDefault(shopName));
        itemBundle.putString(EE_PARAM_SHOP_TYPE, setValueOrDefault(""));
        itemBundle.putString(EE_PARAM_CATEGORY_ID, setValueOrDefault(""));
        itemBundle.putInt(EE_PARAM_QUANTITY, element.getMinOrder());
        itemBundle.putDouble(EE_PARAM_PRICE, element.getPriceInt());
        itemBundle.putString(EE_PARAM_PICTURE, element.getProductImage());
        itemBundle.putString(EE_PARAM_URL, element.getProductUrl());
        itemBundle.putString(EE_PARAM_DIMENSION_38, setValueOrDefault(""));
        itemBundle.putString(EE_PARAM_DIMENSION_45, setValueOrDefault(data.getCartId()));
        itemBundle.putString(EE_PARAM_DIMENSION_83, dimen83);
        itemBundle.putString("dimension40", element.getAtcDimension40(sourcePage));

        Bundle eventDataLayer = new Bundle();
        eventDataLayer.putString(TrackAppUtils.EVENT, Name.ATC);
        eventDataLayer.putString(TrackAppUtils.EVENT_CATEGORY, Category.CHAT_DETAIL);
        eventDataLayer.putString(TrackAppUtils.EVENT_ACTION, eventAction);
        eventDataLayer.putString(TrackAppUtils.EVENT_LABEL, element.getAtcDimension40(sourcePage));
        eventDataLayer.putParcelableArrayList(EE_VALUE_ITEMS, new ArrayList<Bundle>() {{
            add(itemBundle);
        }});
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                Name.ATC, eventDataLayer
        );
    }

    private String setValueOrDefault(String value) {
        if (value.isEmpty()) {
            return EE_VALUE_NONE_OTHER;
        }
        return value;
    }

    @SuppressLint("VisibleForTests")
    private Map<String, Object> createGeneralEvent(
            String event,
            String category,
            String action,
            String label,
            String businessUnit,
            String currentSite,
            String userId
    ) {
        return DataLayer.mapOf(
                EVENT_NAME, event,
                EVENT_CATEGORY, category,
                EVENT_ACTION, action,
                EVENT_LABEL, label,
                KEY_BUSINESS_UNIT, businessUnit,
                KEY_CURRENT_SITE, currentSite,
                USER_ID, userId
        );
    }
}
