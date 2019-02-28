package com.tokopedia.groupchat.common.analytics;

import android.app.Activity;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleProductViewModel;
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel;
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentViewModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author by StevenFredian on 05/03/18.
 * https://docs.google.com/spreadsheets/d/1Td-vIErrumFrKJ1fzv5D63R2iwyvWm87EJMiYV6xqXk/
 */

public class GroupChatAnalytics {
    public static final int DEFAULT_EE_POSITION = 1;
    private static final String EVENT_VIEW_GROUP_CHAT = "viewGroupChat";
    public static final String PLAY_TRACE = "mp_play_detail";
    private AnalyticTracker analyticTracker;

    private static final String EVENT_NAME = "event";
    private static final String EVENT_CATEGORY = "eventCategory";
    private static final String EVENT_ACTION = "eventAction";
    private static final String EVENT_LABEL = "eventLabel";
    private static final String ECOMMERCE = "ecommerce";
    private static final String TRACKER_ATTRIBUTION = "tracker_attribution";
    private static final String ATTRIBUTION = "attribution";


    private static final String EVENT_CATEGORY_GROUPCHAT_LIST = "groupchat";
    private static final String EVENT_CATEGORY_GROUPCHAT_ROOM = "groupchat room";
    private static final String EVENT_CATEGORY_SHARE = "share page";
    public static final String EVENT_CATEGORY_LEFT_NAVIGATION = "left navigation";

    private static final String EVENT_ACTION_GROUPCHAT_LIST = "click on group chat list";
    private static final String EVENT_ACTION_VOTE = "click on vote";
    private static final String EVENT_ACTION_SHARE = "click on share";
    private static final String EVENT_ACTION_SHARE_CHANNEL = "click share channel";
    private static final String EVENT_ACTION_JOIN_VOTE_NOW = "click on join";
    private static final String EVENT_ACTION_CLICK_THUMBNAIL = "click on image thumbnail";
    private static final String EVENT_ACTION_CLICK_COMPONENT = "click on component - ";
    private static final String EVENT_ACTION_VIEW_COMPONENT = "view on component - ";
    public static final String EVENT_ACTION_CLICK_GROUP_CHAT = "click on groupchat";
    private static final String EVENT_ACTION_CLICK_LOYALTY_WIDGET = "click on loyalty widget";
    private static final String EVENT_ACTION_LEAVE_ROOM = "leave room";
    private static final String EVENT_ACTION_PLAY_VIDEO = "click on play button video";
    private static final String EVENT_ACTION_WATCH_VIDEO_DURATION = "watch duration play";
    private static final String EVENT_ACTION_CLICK_VOTE_INFO = "click on info vote link";
    private static final String EVENT_ACTION_CLICK_OFFICIAL_PARTNER = "click on official partner link";
    private static final String EVENT_ACTION_CLICK_OVERLAY_BUTTON = "click on overlay button";
    private static final String EVENT_ACTION_CLICK_OVERLAY_CLOSE = "click on close button";
    private static final String EVENT_ACTION_CLICK_OVERLAY_IMAGE = "click on image overlay";
    private static final String EVENT_ACTION_VIEW_OVERLAY = "view on overlay";


    private static final String EVENT_NAME_CLICK_GROUPCHAT = "clickGroupChat";
    private static final String EVENT_NAME_CLICK_SHARE = "clickShare";
    public static final String EVENT_NAME_CLICK_NAVIGATION_DRAWER = "clickNavigationDrawer";
    private static final String EVENT_NAME_PROMO_CLICK = "promoClick";
    private static final String EVENT_NAME_PROMO_VIEW = "promoView";
    private static final String EVENT_NAME_CLICK_BACK = "clickBack";
    private static final String EVENT_NAME_INTERNAL_PROMOTION = "InternalPromotion";


    public static final String COMPONENT_FLASH_SALE = "flashsale";
    public static final String COMPONENT_BANNER = "banner"; //Sponsor Banner
    public static final String COMPONENT_VOTE = "vote";
    public static final String COMPONENT_PARTNER = "partner";


    private static final String ATTRIBUTE_GROUP_CHAT = "Group Chat";
    public static final String ATTRIBUTE_FLASH_SALE = "Flash Sale";
    public static final String ATTRIBUTE_BANNER = "Banner";
    public static final String ATTRIBUTE_PARTNER_LOGO = "Logo";
    public static final String ATTRIBUTE_OVERLAY_IMAGE = "Overlay Image";
    public static final String ATTRIBUTE_OVERLAY_BUTTON = "Overlay Button";
    public static final String ATTRIBUTE_PROMINENT_BUTTON = "Prominent Button";
    public static final String ATTRIBUTE_STICKY = "Sticky Button";


    public static final String SCREEN_CHAT_ROOM = "/group-chat-room/";
    public static final String SCREEN_PLAY_WEBVIEW_FULL = "/group-chat-webvie-full/";

    private static final String EE_PROMO_CLICK = "promoClick";
    private static final String EE_PROMO_VIEW = "promoView";

    private static final String EE_PROMOTIONS = "promotions";

    public static final String VIEW_LOGO = "Logo";

    @Inject
    public GroupChatAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void sendScreen(Activity activity, String screenName) {
        analyticTracker.sendScreen(activity, screenName);
    }

    //#4
    public void eventClickJoin(String channelId) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "click on join",
                channelId
        );
    }

    //#5
    public void eventUserExit(String channelLabel) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_BACK,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "leave room",
                channelLabel
        );
    }

    //#6
    public void eventClickQuickReply(String channelLabel) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "click on quick reply component",
                channelLabel
        );
    }

    //#7
    public void eventClickAdminPinnedMessage(String channelLabel) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "click on admin pinned message",
                channelLabel
        );
    }

    //#9
    public void eventViewBanner(ChannelInfoViewModel viewModel,
                                String adsId, String adsName, String adsImageUrl) {
        analyticTracker.sendEventTracking(EVENT_NAME_PROMO_VIEW,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                String.format("%s%s", EVENT_ACTION_VIEW_COMPONENT, COMPONENT_BANNER),
                String.format("%s - %s", viewModel.getChannelId(), adsName)
        );

        ArrayList<EEPromotion> list = new ArrayList<>();
        list.add(new EEPromotion(adsId,
                EEPromotion.NAME_GROUPCHAT,
                GroupChatAnalytics.DEFAULT_EE_POSITION,
                adsName,
                adsImageUrl,
                generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_BANNER, viewModel.getChannelUrl(), viewModel.getTitle())
        ));

        analyticTracker.sendEnhancedEcommerce(DataLayer.mapOf(
                EVENT_NAME, EVENT_NAME_PROMO_VIEW,
                EVENT_CATEGORY, EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION, String.format("%s%s", EVENT_ACTION_VIEW_COMPONENT, COMPONENT_BANNER),
                EVENT_LABEL, String.format("%s - %s", viewModel.getChannelId(), adsName),
                ECOMMERCE, getEEDataLayer(list, EE_PROMO_CLICK),
                ATTRIBUTION, generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_BANNER, viewModel.getChannelUrl(), viewModel.getTitle()
                )));
    }

    //#9
    public void eventClickSprintSaleProduct(@Nullable SprintSaleProductViewModel productViewModel,
                                            Integer position, ChannelInfoViewModel viewModel) {
        ArrayList<EEPromotion> list = new ArrayList<>();
        list.add(new EEPromotion(productViewModel.getProductId(),
                EEPromotion.NAME_GROUPCHAT,
                position,
                productViewModel.getProductName(),
                productViewModel.getProductImage(),
                generateTrackerAttribution(GroupChatAnalytics.ATTRIBUTE_FLASH_SALE,
                        viewModel.getChannelUrl(), viewModel.getTitle())
        ));

        analyticTracker.sendEnhancedEcommerce(DataLayer.mapOf(
                EVENT_NAME, EVENT_NAME_PROMO_CLICK,
                EVENT_CATEGORY, EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION, EVENT_ACTION_CLICK_COMPONENT + COMPONENT_FLASH_SALE,
                EVENT_LABEL, GroupChatAnalytics.COMPONENT_FLASH_SALE + " " + productViewModel.getProductName(),
                ECOMMERCE, getEEDataLayer(list, EE_PROMO_CLICK),
                ATTRIBUTION, generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_FLASH_SALE, viewModel.getChannelUrl(), viewModel.getTitle())
        ));

        if (viewModel.getSprintSaleViewModel() != null && viewModel.getSprintSaleViewModel().getCampaignName() != null)
            eventClickFlashSale(String.format("%s - %s", viewModel.getChannelId(),
                    viewModel.getSprintSaleViewModel().getCampaignName()));

    }

    //#9
    public void eventClickSprintSaleComponent(@NotNull SprintSaleAnnouncementViewModel sprintSaleAnnouncementViewModel,
                                              int position,
                                              @NotNull ChannelInfoViewModel viewModel) {
        ArrayList<EEPromotion> list = new ArrayList<>();
        for (SprintSaleProductViewModel productViewModel : sprintSaleAnnouncementViewModel
                .getListProducts()) {
            list.add(new EEPromotion(productViewModel.getProductId(),
                    EEPromotion.NAME_GROUPCHAT,
                    GroupChatAnalytics.DEFAULT_EE_POSITION,
                    productViewModel.getProductName(),
                    productViewModel.getProductImage(),
                    generateTrackerAttribution(GroupChatAnalytics
                            .ATTRIBUTE_FLASH_SALE, viewModel.getChannelUrl(), viewModel.getTitle()
                    )));
        }

        analyticTracker.sendEnhancedEcommerce(DataLayer.mapOf(
                EVENT_NAME, EVENT_NAME_PROMO_CLICK,
                EVENT_CATEGORY, EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION, EVENT_ACTION_CLICK_COMPONENT + COMPONENT_FLASH_SALE,
                EVENT_LABEL,
                GroupChatAnalytics.COMPONENT_FLASH_SALE + " " + sprintSaleAnnouncementViewModel.getCampaignName(),
                ECOMMERCE, getEEDataLayer(list, EE_PROMO_CLICK),
                ATTRIBUTION, generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_FLASH_SALE, viewModel.getChannelUrl(), viewModel.getTitle())
        ));

        if (viewModel.getSprintSaleViewModel() != null && viewModel.getSprintSaleViewModel().getCampaignName() != null)
            eventClickFlashSale(String.format("%s - %s", viewModel.getChannelId(),
                    viewModel.getSprintSaleViewModel().getCampaignName()));

    }

    //#10
    public void eventClickBanner(ChannelInfoViewModel viewModel,
                                 String adsId, String adsName, String adsImageUrl) {
        analyticTracker.sendEventTracking(EVENT_NAME_PROMO_VIEW,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                String.format("%s%s", EVENT_ACTION_CLICK_COMPONENT, COMPONENT_BANNER),
                String.format("%s - %s", viewModel.getChannelId(), adsId
                )
        );

        ArrayList<EEPromotion> list = new ArrayList<>();
        list.add(new EEPromotion(adsId,
                EEPromotion.NAME_GROUPCHAT,
                GroupChatAnalytics.DEFAULT_EE_POSITION,
                adsName,
                adsImageUrl,
                generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_BANNER, viewModel.getChannelUrl(), viewModel.getTitle())
        ));

        analyticTracker.sendEnhancedEcommerce(DataLayer.mapOf(
                EVENT_NAME, EVENT_NAME_PROMO_VIEW,
                EVENT_CATEGORY, EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION, String.format("%s%s", EVENT_ACTION_CLICK_COMPONENT, COMPONENT_BANNER),
                EVENT_LABEL, String.format("%s - %s", viewModel.getChannelId(), adsId),
                ECOMMERCE, getEEDataLayer(list, EE_PROMO_CLICK),
                ATTRIBUTION, generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_BANNER, viewModel.getChannelUrl(), viewModel.getTitle()
                )));
    }

    //#11
    public void eventClickShare(String channelId) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "click on share",
                channelId
        );
    }

    //#12
    public void eventClickLogin(String channelId) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "click on masuk untuk chat (non login user)",
                channelId
        );
    }

    //#13
    public void eventViewImageAnnouncement(ChannelInfoViewModel viewModel,
                                           String imageUrl, String bannerId, String bannerName) {
        analyticTracker.sendEventTracking(
                EVENT_NAME_INTERNAL_PROMOTION,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "view banner push promo",
                String.format("%s - %s", viewModel.getChannelId(), imageUrl)
        );

        ArrayList<EEPromotion> list = new ArrayList<>();
        list.add(new EEPromotion(bannerId,
                EEPromotion.NAME_GROUPCHAT + "-banner",
                GroupChatAnalytics.DEFAULT_EE_POSITION,
                bannerName,
                imageUrl,
                generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_BANNER, viewModel.getChannelUrl(), viewModel.getTitle())
        ));

        analyticTracker.sendEnhancedEcommerce(DataLayer.mapOf(
                EVENT_NAME, EVENT_NAME_INTERNAL_PROMOTION,
                EVENT_CATEGORY, EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION, "view banner push promo",
                EVENT_LABEL, String.format("%s - %s", viewModel.getChannelId(), imageUrl),
                ECOMMERCE, getEEDataLayer(list, EE_PROMO_CLICK),
                ATTRIBUTION, generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_BANNER, viewModel.getChannelUrl(), viewModel.getTitle()
                )));
    }

    //#14
    public void eventClickThumbnail(ChannelInfoViewModel viewModel,
                                    String imageUrl, String bannerId, String bannerName) {
        analyticTracker.sendEventTracking(EE_PROMO_CLICK,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "click on image thumbnail",
                String.format("%s - %s", viewModel.getChannelId(), imageUrl)
        );

        ArrayList<EEPromotion> list = new ArrayList<>();
        list.add(new EEPromotion(bannerId,
                EEPromotion.NAME_GROUPCHAT + "-banner",
                GroupChatAnalytics.DEFAULT_EE_POSITION,
                bannerName,
                imageUrl,
                generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_BANNER, viewModel.getChannelUrl(), viewModel.getTitle())
        ));

        analyticTracker.sendEnhancedEcommerce(DataLayer.mapOf(
                EVENT_NAME, EE_PROMO_CLICK,
                EVENT_CATEGORY, EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION, "click on image thumbnail",
                EVENT_LABEL, String.format("%s - %s", viewModel.getChannelId(), imageUrl),
                ECOMMERCE, getEEDataLayer(list, EE_PROMO_CLICK),
                ATTRIBUTION, generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_BANNER, viewModel.getChannelUrl(), viewModel.getTitle()
                )));
    }

    //#17
    public void eventWatchVideoDuration(String channelId, String duration) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "watch duration play",
                channelId + " - " + duration
        );
    }

    //#18
    public void eventClickAutoPlayVideo(String channelId) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "click on play button video",
                channelId
        );
    }

    //#19
    public void eventClickLoyaltyWidget(String channelId) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "click on loyalty widget",
                channelId
        );
    }

    //#26
    public void eventClickOverlayButton(@NotNull ChannelInfoViewModel channelInfoViewModel,
                                        String overlayId, String overlayName, String imageUrl) {

        ArrayList<EEPromotion> list = new ArrayList<>();
        list.add(new EEPromotion(overlayId,
                EEPromotion.NAME_GROUPCHAT,
                GroupChatAnalytics.DEFAULT_EE_POSITION,
                overlayName,
                imageUrl,
                generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_BANNER, channelInfoViewModel.getChannelUrl(), channelInfoViewModel.getTitle())
        ));

        analyticTracker.sendEnhancedEcommerce(DataLayer.mapOf(
                EVENT_NAME, EVENT_NAME_PROMO_CLICK,
                EVENT_CATEGORY, EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION, "click on overlay button",
                EVENT_LABEL, channelInfoViewModel.getChannelId() + " - " + overlayName,
                ECOMMERCE, getEEDataLayer(list, EE_PROMO_CLICK),
                ATTRIBUTION, generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_BANNER, channelInfoViewModel.getChannelUrl(), channelInfoViewModel.getTitle())
        ));
    }

    //#27
    public void eventClickCloseOverlayCloseButton(String channelId) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_CLICK_OVERLAY_CLOSE,
                channelId + " - " + "button x"
        );
    }

    //#27
    public void eventClickCloseOverlayBackButton(String channelId) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_CLICK_OVERLAY_CLOSE,
                channelId + " - " + "back button"
        );
    }

    //#27
    public void eventClickCloseOverlayOutside(String channelId) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_CLICK_OVERLAY_CLOSE,
                channelId + " - " + "outside"
        );
    }

    //#28
    public void eventClickOverlayImage(@NotNull ChannelInfoViewModel viewModel,
                                       String overlayImageId, String overlayImageName,
                                       String imageUrl) {
        ArrayList<EEPromotion> list = new ArrayList<>();
        list.add(new EEPromotion(overlayImageId,
                EEPromotion.NAME_GROUPCHAT+"-banner",
                GroupChatAnalytics.DEFAULT_EE_POSITION,
                overlayImageName,
                imageUrl,
                generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_BANNER, viewModel.getChannelUrl(), viewModel.getTitle())
        ));

        analyticTracker.sendEnhancedEcommerce(DataLayer.mapOf(
                EVENT_NAME, EVENT_NAME_PROMO_CLICK,
                EVENT_CATEGORY, EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION, "click on image overlay",
                EVENT_LABEL, viewModel.getChannelId(),
                ECOMMERCE, getEEDataLayer(list, EE_PROMO_CLICK),
                ATTRIBUTION, generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_BANNER, viewModel.getChannelUrl(), viewModel.getTitle())
        ));

    }

    //#29
    public void eventViewOverlay(String channelId) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "view on overlay",
                channelId
        );
    }

    //#31
    public void eventClickSendChat(String channelId) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "click on button send chat",
                channelId
        );
    }

    //#32
    public void eventShowStickyComponent(@NotNull StickyComponentViewModel item,
                                         ChannelInfoViewModel viewModel) {

        //TODO sticky component id
        ArrayList<EEPromotion> list = new ArrayList<>();
        list.add(new EEPromotion("",
                EEPromotion.NAME_GROUPCHAT+"-stickycomponent",
                GroupChatAnalytics.DEFAULT_EE_POSITION,
                item.getTitle(),
                item.getImageUrl(),
                generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_STICKY, viewModel.getChannelUrl(), viewModel.getTitle())
        ));

        analyticTracker.sendEnhancedEcommerce(DataLayer.mapOf(
                EVENT_NAME, EVENT_VIEW_GROUP_CHAT,
                EVENT_CATEGORY, EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION, "view on sticky product",
                EVENT_LABEL, viewModel.getChannelId(),
                ECOMMERCE, getEEDataLayer(list, EE_PROMO_CLICK),
                ATTRIBUTION, generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_BANNER, viewModel.getChannelUrl(), viewModel.getTitle())
        ));
    }

    //#33
    public void eventClickStickyComponent(@NotNull StickyComponentViewModel item,
                                       @NotNull ChannelInfoViewModel viewModel) {
        //TODO sticky component id
        ArrayList<EEPromotion> list = new ArrayList<>();
        list.add(new EEPromotion("",
                EEPromotion.NAME_GROUPCHAT+"-stickycomponent",
                GroupChatAnalytics.DEFAULT_EE_POSITION,
                item.getTitle(),
                item.getImageUrl(),
                generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_STICKY, viewModel.getChannelUrl(), viewModel.getTitle())
        ));

        analyticTracker.sendEnhancedEcommerce(DataLayer.mapOf(
                EVENT_NAME, EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY, EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION, "click on sticky product",
                EVENT_LABEL, viewModel.getChannelId(),
                ECOMMERCE, getEEDataLayer(list, EE_PROMO_CLICK),
                ATTRIBUTION, generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_BANNER, viewModel.getChannelUrl(), viewModel.getTitle())
        ));
    }

    //#34
    public void eventViewProminentButton(ChannelInfoViewModel channelInfoViewModel,
                                         DynamicButtonsViewModel.Button prominentButton) {
        analyticTracker.sendEventTracking(
                EVENT_VIEW_GROUP_CHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "view on prominent button",
                channelInfoViewModel.getChannelId() + " - " + prominentButton.getImageUrl()
        );
    }

    //#35
    public void eventClickProminentButton(ChannelInfoViewModel channelInfoViewModel,
                                          DynamicButtonsViewModel.Button prominentButton) {
        analyticTracker.sendEventTracking(
                EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "click on prominent button",
                channelInfoViewModel.getChannelId() + " - " + prominentButton.getImageUrl()
        );
    }

    //#36
    public void eventViewDynamicButtons(ChannelInfoViewModel viewModel,
                                        @NotNull ArrayList<DynamicButtonsViewModel.Button> listDynamicButton) {

        //TODO button id
        String buttonNames= "";

        ArrayList<EEPromotion> list = new ArrayList<>();
        for(DynamicButtonsViewModel.Button button : listDynamicButton) {
            list.add(new EEPromotion("",
                    EEPromotion.NAME_GROUPCHAT + "-stickycomponent",
                    GroupChatAnalytics.DEFAULT_EE_POSITION,
                    button.getLinkUrl(),
                    button.getImageUrl(),
                    generateTrackerAttribution(GroupChatAnalytics
                            .ATTRIBUTE_STICKY, viewModel.getChannelUrl(), viewModel.getTitle())
            ));
//            buttonNames += button.getId() + ",";
        }

        analyticTracker.sendEnhancedEcommerce(DataLayer.mapOf(
                EVENT_NAME, EVENT_VIEW_GROUP_CHAT,
                EVENT_CATEGORY, EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION, "view on dynamic button",
                EVENT_LABEL, viewModel.getChannelId() + " - " + buttonNames,
                ECOMMERCE, getEEDataLayer(list, EE_PROMO_CLICK),
                ATTRIBUTION, generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_BANNER, viewModel.getChannelUrl(), viewModel.getTitle())
        ));

    }

    //#36
    public void eventClickDynamicButtons(ChannelInfoViewModel viewModel,
                                         DynamicButtonsViewModel.Button button) {

        //TODO button id

        ArrayList<EEPromotion> list = new ArrayList<>();
            list.add(new EEPromotion("",
                    EEPromotion.NAME_GROUPCHAT + "-stickycomponent",
                    GroupChatAnalytics.DEFAULT_EE_POSITION,
                    button.getLinkUrl(),
                    button.getImageUrl(),
                    generateTrackerAttribution(GroupChatAnalytics
                            .ATTRIBUTE_STICKY, viewModel.getChannelUrl(), viewModel.getTitle())
            ));

        analyticTracker.sendEnhancedEcommerce(DataLayer.mapOf(
                EVENT_NAME, EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY, EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION, "click on dynamic button",
                EVENT_LABEL, viewModel.getChannelId() + " - ", //button id
                ECOMMERCE, getEEDataLayer(list, EE_PROMO_CLICK),
                ATTRIBUTION, generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_BANNER, viewModel.getChannelUrl(), viewModel.getTitle())
        ));

    }

    public void eventClickGroupChatList(String id) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_LIST,
                EVENT_ACTION_GROUPCHAT_LIST,
                id
        );
    }

    public static String generateTrackerAttribution(String attributeName, String channelUrl, String
            channelName) {
        return String.format("%s - " + ATTRIBUTE_GROUP_CHAT + " -" +
                " %s - %s", attributeName, channelUrl, channelName);
    }

    public void eventClickVoteComponent(String componentType, String componentName) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_CLICK_COMPONENT + componentType,
                componentType + " " + componentName
        );
    }

    public void eventClickComponentEnhancedEcommerce(String componentType, String componentName,
                                                     String attributeName, String channelUrl,
                                                     String channelName, List<EEPromotion> listPromotion) {

        analyticTracker.sendEnhancedEcommerce(DataLayer.mapOf(
                EVENT_NAME, EVENT_NAME_PROMO_CLICK,
                EVENT_CATEGORY, EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION, EVENT_ACTION_CLICK_COMPONENT + componentType,
                EVENT_LABEL, componentType + " " + componentName,
                ECOMMERCE, getEEDataLayer(listPromotion, EE_PROMO_CLICK),
                ATTRIBUTION, generateTrackerAttribution(attributeName, channelUrl, channelName)
        ));
    }

    public void eventViewComponentEnhancedEcommerce(String componentType, String componentName,
                                                    String attributeName, String channelUrl,
                                                    String channelName, List<EEPromotion> listPromotion) {

        analyticTracker.sendEnhancedEcommerce(DataLayer.mapOf(
                EVENT_NAME, EVENT_NAME_PROMO_VIEW,
                EVENT_CATEGORY, EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION, EVENT_ACTION_VIEW_COMPONENT + componentType,
                EVENT_LABEL, componentType + " " + componentName,
                ECOMMERCE, getEEDataLayer(listPromotion, EE_PROMO_VIEW),
                ATTRIBUTION, generateTrackerAttribution(attributeName, channelUrl, channelName)
        ));
    }


    public Map<String, Object> getEEDataLayer(List<EEPromotion> listPromotion, String eventName) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(eventName, getEEPromoClickData(listPromotion));
        return hashMap;
    }

    public Map<String, Object> getEEPromoClickData(List<EEPromotion> listPromotion) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(EE_PROMOTIONS, createList(listPromotion));
        return hashMap;
    }

    private static List<Object> createList(List<EEPromotion> listPromotion) {
        List<Object> list = new ArrayList<>();
        for (EEPromotion promo : listPromotion) {
            Map<String, Object> map = createPromotionMap(promo);
            list.add(map);
        }
        return list;
    }

    private static Map<String, Object> createPromotionMap(EEPromotion promo) {
        Map<String, Object> map = new HashMap<>();
        map.put(EEPromotion.KEY_ID, promo.getId());
        map.put(EEPromotion.KEY_NAME, promo.getName());
        map.put(EEPromotion.KEY_POSITION, String.valueOf(promo.getPosition()));
        map.put(EEPromotion.KEY_CREATIVE, promo.getCreative());
        map.put(EEPromotion.KEY_CREATIVE_URL, promo.getCreativeUrl());
        map.put(EEPromotion.ATTRIBUTION, promo.getAttribution());
        return map;
    }

    @Deprecated
    public void eventClickFlashSale(String id) {
        analyticTracker.sendEventTracking(EE_PROMO_CLICK,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                String.format("%s%s", EVENT_ACTION_CLICK_COMPONENT, COMPONENT_FLASH_SALE),
                id
        );
    }

    @Deprecated
    public void eventViewImageAnnouncement(String channelLabel) {
        analyticTracker.sendEventTracking(
                EVENT_NAME_INTERNAL_PROMOTION,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "view banner push promo",
                channelLabel
        );
    }

    public void eventClickOverlayCTAButton(@Nullable String channelId, @NotNull String contentButtonText) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "click on menu button",
                channelId + " - " + contentButtonText
        );
    }

    @Deprecated
    public void eventClickVote(String type, String channelName) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_VOTE,
                type + " - " + channelName
        );
    }

    @Deprecated
    public void eventClickShareChannel(String channelType, String channelName) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_SHARE,
                EVENT_CATEGORY_SHARE,
                EVENT_ACTION_SHARE_CHANNEL,
                channelType + " - " + channelName
        );
    }

    @Deprecated
    public void eventViewFlashSale(String id) {
        analyticTracker.sendEventTracking(EE_PROMO_CLICK,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                String.format("%s%s", EVENT_ACTION_VIEW_COMPONENT, COMPONENT_FLASH_SALE),
                id
        );
    }


    @Deprecated
    public void eventActionClickVoteInfo(String channelLabel) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_CLICK_VOTE_INFO,
                channelLabel
        );
    }

    @Deprecated
    public void eventActionClickOfficialPartner(String officialPartner) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_CLICK_OFFICIAL_PARTNER,
                officialPartner
        );
    }


    @Deprecated
    public void eventActionViewOfficialPartner(String label) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                String.format("%s%s", EVENT_ACTION_VIEW_COMPONENT, COMPONENT_PARTNER),
                label
        );
    }

    @Deprecated
    public void eventViewBanner(String channelLabel) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                String.format("%s%s", EVENT_ACTION_VIEW_COMPONENT, COMPONENT_BANNER),
                channelLabel
        );
    }


    @Deprecated
    public void eventClickThumbnail(String id) {
        analyticTracker.sendEventTracking(EE_PROMO_CLICK,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_CLICK_THUMBNAIL,
                id
        );
    }

    @Deprecated
    public void eventClickBanner(String channelLabel) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                String.format("%s%s", EVENT_ACTION_CLICK_COMPONENT, COMPONENT_BANNER),
                channelLabel
        );
    }

}
