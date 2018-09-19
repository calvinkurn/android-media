package com.tokopedia.tkpd.home.analytics;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;

public class HomeGATracking extends UnifyTracking {

    private static final String EVENT_VIDEO_VIEW = "videoView";
    private static final String EVENT_VIDEO_GENERAL_EVENT = "videoGeneralEvent";
    private static final String CATEGORY_VIDEO_PUSH = "video push";
    private static final String ACTION_VIDEO_NOTIFICATION_PAGE = "view video notification page";
    private static final String ACTION_CLICK_CTA_BUTTON = "click cta button";
    private static final String LABEL_LANDING_SCREEN = "landing screenname";

    public static void eventYoutubeVideoImpression() {
        sendGTMEvent(new EventTracking(EVENT_VIDEO_VIEW,
                CATEGORY_VIDEO_PUSH,
                ACTION_VIDEO_NOTIFICATION_PAGE,
                LABEL_LANDING_SCREEN)
                .getEvent());
    }

    public static void eventClickCTAButton() {
        sendGTMEvent(new EventTracking(EVENT_VIDEO_GENERAL_EVENT,
                CATEGORY_VIDEO_PUSH,
                ACTION_CLICK_CTA_BUTTON,
                "-")
                .getEvent());
    }
}
