package com.tokopedia.topchat.common.analytics;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;

import javax.inject.Inject;

public class ChatSettingsAnalytics {


    public static final String EVENT_NAME = "ClickChatDetail";
    public static final String CHAT_OPEN_CATEGORY = "chat detail";
    public static final String CHAT_SETTINGS_ACTION = "click on atur penerimaan chat button";
    public static final String CHAT_SETTINGS_CATEGORY = "chat page setting";
    public static final String CHAT_BLOCK_ACTION = "chat toggle un-receive";
    public static final String CHAT_UNBLOCK_ACTION = "chat toggle receive";
    public static final String CHAT_PERSONAL_LABEL = "personal chat";
    public static final String CHAT_PROMOTION_LABEL = "promotion chat";
    public static final String CHAT_ENABLE_TEXT_LINK_ACTION = "click on text link";
    public static final String CHAT_ENABLE_TEXT_LABEL = "kembali menerima pesan";


    private AnalyticTracker tracker;


    @Inject
    public ChatSettingsAnalytics(@ApplicationContext Context context) {
        if (context != null && context.getApplicationContext() instanceof AbstractionRouter) {
            tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        }
    }

    public void sendTrackingEvent(String eventCategory, String eventAction, String eventLabel) {
        tracker.sendEventTracking(EVENT_NAME, eventCategory, eventAction, eventLabel);

    }
}
