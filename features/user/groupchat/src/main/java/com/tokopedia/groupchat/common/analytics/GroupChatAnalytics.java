package com.tokopedia.groupchat.common.analytics;

import android.app.Activity;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ImageAnnouncementViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author by StevenFredian on 05/03/18.
 */

public class GroupChatAnalytics {
    public static final int DEFAULT_EE_POSITION = 1;
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


    private static final String EVENT_NAME_CLICK_GROUPCHAT = "clickGroupChat";
    private static final String EVENT_NAME_CLICK_SHARE = "clickShare";
    public static final String EVENT_NAME_CLICK_NAVIGATION_DRAWER = "clickNavigationDrawer";
    private static final String EVENT_NAME_PROMO_CLICK = "promoClick";
    private static final String EVENT_NAME_PROMO_VIEW = "promoView";
    private static final String EVENT_NAME_CLICK_BACK = "clickBack";
    private static final String EVENT_NAME_INTERNAL_PROMOTION = "internalPromotion";


    public static final String COMPONENT_FLASH_SALE = "flashsale";
    public static final String COMPONENT_BANNER = "banner";
    public static final String COMPONENT_VOTE = "vote";
    public static final String COMPONENT_PARTNER = "partner";


    private static final String ATTRIBUTE_GROUP_CHAT = "Group Chat";
    public static final String ATTRIBUTE_FLASH_SALE = "Flash Sale";
    public static final String ATTRIBUTE_BANNER = "Banner";
    public static final String ATTRIBUTE_PARTNER_LOGO = "Logo";

    public static final String SCREEN_CHAT_ROOM = "/group-chat-room/";

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

    public void eventClickGroupChatList(String id) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_LIST,
                EVENT_ACTION_GROUPCHAT_LIST,
                id
        );
    }

    public void eventClickVote(String type, String channelName) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_VOTE,
                type + " - " + channelName
        );
    }

    public void eventClickShare() {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_SHARE,
                ""
        );
    }

    public void eventClickShareChannel(String channelType, String channelName) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_SHARE,
                EVENT_CATEGORY_SHARE,
                EVENT_ACTION_SHARE_CHANNEL,
                channelType + " - " + channelName
        );
    }

    public void eventClickJoin() {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_JOIN_VOTE_NOW,
                ""
        );
    }

    public void eventClickThumbnail(String id) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_CLICK_THUMBNAIL,
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

    public void eventClickLoyaltyWidget(String channelName) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_CLICK_LOYALTY_WIDGET,
                channelName
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

    public void eventUserExit(String channelName) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_BACK,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_LEAVE_ROOM,
                ""
        );
    }

    public void eventClickQuickReply(String channelId) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "click on quick reply component",
                channelId
        );
    }

    public void eventClickAdminPinnedMessage(String channelId) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "click on admin pinned message",
                channelId
        );
    }

    public void eventClickLogin(String channelId) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "click on masuk untuk chat (non login user)",
                channelId
        );
    }

    public void eventViewBannerPushPromo(ImageAnnouncementViewModel messageItem) {
        analyticTracker.sendEventTracking(
                EVENT_NAME_INTERNAL_PROMOTION,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                "view banner push promo",
                messageItem.getMessageId()
        );
    }

    public void eventClickAutoPlayVideo(String channelId) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_PLAY_VIDEO,
                channelId
        );
    }

    public void eventWatchVideoDuration(String channelId, String duration) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_WATCH_VIDEO_DURATION,
                channelId +" - "+duration
        );
    }


    public void eventActionClickVoteInfo() {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_CLICK_VOTE_INFO,
                ""
        );
    }

    public void eventActionClickOfficialPartner(String officialPartner) {
        analyticTracker.sendEventTracking(EVENT_NAME_CLICK_GROUPCHAT,
                EVENT_CATEGORY_GROUPCHAT_ROOM,
                EVENT_ACTION_CLICK_OFFICIAL_PARTNER,
                officialPartner
        );
    }


}
