package com.tokopedia.instantloan.common.analytics;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;

public class InstantLoanEventTracking extends UnifyTracking {

    public static void eventLoanBannerImpression(String eventLabel) {
        sendGTMEvent(new EventTracking(
                        InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                        InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                        InstantLoanEventConstants.Action.PL_BANNER_IMPRESSION,
                        eventLabel
                ).setUserId().getEvent()
        );
    }

    public static void eventLoanBannerClick(String eventLabel) {
        sendGTMEvent(new EventTracking(
                        InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                        InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                        InstantLoanEventConstants.Action.PL_BANNER_CLICK,
                        eventLabel
                ).setUserId().getEvent()
        );
    }

    public static void eventCariPinjamanClick(String eventLabel) {
        sendGTMEvent(new EventTracking(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_CARI_PINJAMAN_CLICK,
                eventLabel
        ).setUserId().getEvent());
    }

    public static void eventLoanPopupClick(String eventLabel) {
        sendGTMEvent(new EventTracking(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_POP_UP_CLICK,
                eventLabel
        ).setUserId().getEvent());
    }

    public static void eventIntroSliderScrollEvent(String eventLabel) {
        sendGTMEvent(new EventTracking(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_POP_UP_CLICK,
                eventLabel
        ).setUserId().getEvent());
    }

    public static void eventInstantLoanPermissionStatus(String eventLabel) {
        sendGTMEvent(new EventTracking(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_POP_UP_CLICK,
                eventLabel
        ).setUserId().getEvent());
    }
}
