package com.tokopedia.topchat.common.analytics;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import javax.inject.Inject;

public class ChatSettingsAnalytics {


    public static final String EVENT_NAME = "ClickChatDetail";
    public static final String EVENT_CLICK_CHAT_SETTING = "clickChatSetting";
    public static final String CHAT_OPEN_CATEGORY = "chat detail";
    public static final String CHAT_SETTINGS_ACTION = "click on atur penerimaan chat button";
    public static final String CHAT_SETTINGS_CATEGORY = "chat page setting";
    public static final String CHAT_BLOCK_ACTION = "chat toggle un-receive";
    public static final String CHAT_UNBLOCK_ACTION = "chat toggle receive";
    public static final String CHAT_PERSONAL_LABEL = "personal chat";
    public static final String CHAT_PROMOTION_LABEL = "promotion chat";
    public static final String CHAT_ENABLE_TEXT_LINK_ACTION = "click on text link (kembali menerima pesan)";
    public static final String CHAT_ENABLE_TEXT_LABEL = "kembali menerima pesan";


    @Inject
    public ChatSettingsAnalytics() {
    }

    public void sendTrackingEvent(String eventCategory, String eventAction, String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME, eventCategory, eventAction, eventLabel));

    }

    // #APC5
    public void sendTrackingUnblockPromotion(String shopId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_CLICK_CHAT_SETTING, CHAT_SETTINGS_CATEGORY, CHAT_UNBLOCK_ACTION, String.format("%s - %s", shopId, CHAT_PROMOTION_LABEL)));
    }

    // #APC3
    public void sendTrackingBlockPromotion(String shopId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_CLICK_CHAT_SETTING, CHAT_SETTINGS_CATEGORY, CHAT_BLOCK_ACTION, String.format("%s - %s", shopId, CHAT_PROMOTION_LABEL)));
    }


    // #APC4
    public void sendTrackingUnblockPersonal(String shopId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_CLICK_CHAT_SETTING, CHAT_SETTINGS_CATEGORY, CHAT_UNBLOCK_ACTION, String.format("%s - %s", shopId, CHAT_PERSONAL_LABEL)));
    }

    // #APC2
    public void sendTrackingBlockPersonal(String shopId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_CLICK_CHAT_SETTING, CHAT_SETTINGS_CATEGORY, CHAT_BLOCK_ACTION, String.format("%s - %s", shopId, CHAT_PERSONAL_LABEL)));
    }

    public void sendOpenChatSettingTacking() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME, ChatSettingsAnalytics.CHAT_OPEN_CATEGORY, ChatSettingsAnalytics.CHAT_SETTINGS_ACTION, ""));
    }

    public void sendEnableChatSettingTracking() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME, ChatSettingsAnalytics.CHAT_OPEN_CATEGORY, ChatSettingsAnalytics.CHAT_ENABLE_TEXT_LINK_ACTION, ChatSettingsAnalytics.CHAT_ENABLE_TEXT_LABEL));
    }
}
