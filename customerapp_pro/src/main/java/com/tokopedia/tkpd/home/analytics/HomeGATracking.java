package com.tokopedia.tkpd.home.analytics;

import android.content.Context;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.app.MainApplication;

public class HomeGATracking extends UnifyTracking {

    private static final String EVENT_VIDEO_VIEW = "viewVideo";
    private static final String EVENT_VIDEO_GENERAL_EVENT = "videoGeneralEvent";
    private static final String CATEGORY_VIDEO_PUSH = "video push";
    private static final String ACTION_VIDEO_NOTIFICATION_PAGE = "view video notification page";
    private static final String ACTION_CLICK_CTA_BUTTON = "click cta button";
    private static final String ACTION_CLICK_BNANNER_BUTTON = "banner_click";
    private static final String ACTION_VIEW_BNANNER_BUTTON = "banner_view";


    public static void eventYoutubeVideoImpression(Context context) {
        sendGTMEvent(context, new EventTracking(EVENT_VIDEO_VIEW,
                CATEGORY_VIDEO_PUSH,
                ACTION_VIDEO_NOTIFICATION_PAGE,
                "-")
                .getEvent());
    }

    public static void eventClickCTAButton(Context context) {
        sendGTMEvent(context, new EventTracking(EVENT_VIDEO_GENERAL_EVENT,
                CATEGORY_VIDEO_PUSH,
                ACTION_CLICK_CTA_BUTTON,
                "-")
                .getEvent());
    }

    public static void eventClickVideoBannerImpression(String label) {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(EVENT_VIDEO_VIEW,
                CATEGORY_VIDEO_PUSH,
                ACTION_VIEW_BNANNER_BUTTON,
                label)
                .getEvent());
    }

    public static void eventClickVideoBannerClick(String label) {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(EVENT_VIDEO_GENERAL_EVENT,
                CATEGORY_VIDEO_PUSH,
                ACTION_CLICK_BNANNER_BUTTON,
                label)
                .getEvent());
    }
}
