package com.tokopedia.instantloan.common.analytics;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import javax.inject.Inject;

public class InstantLoanAnalytics {

    private AnalyticTracker tracker;

    @Inject
    public InstantLoanAnalytics(@ApplicationContext Context context) {
        if (context != null && context.getApplicationContext() instanceof AbstractionRouter) {
            tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        }
    }

    public void eventLoanBannerImpression(String eventLabel) {
        if (tracker == null) {
            return;
        }
        tracker.sendEventTracking(InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_BANNER_IMPRESSION,
                eventLabel);
    }

    public void eventLoanBannerClick(String eventLabel) {
        if (tracker == null) {
            return;
        }
        tracker.sendEventTracking(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_BANNER_CLICK,
                eventLabel
        );
    }

    public void eventCariPinjamanClick(String eventLabel) {
        if (tracker == null) {
            return;
        }
        tracker.sendEventTracking(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_CARI_PINJAMAN_CLICK,
                eventLabel
        );
    }

    public void eventLoanPopupClick(String eventLabel) {
        if (tracker == null) {
            return;
        }
        tracker.sendEventTracking(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_POP_UP_CLICK,
                eventLabel
        );
    }

    public void eventIntroSliderScrollEvent(String eventLabel) {
        if (tracker == null) {
            return;
        }
        tracker.sendEventTracking(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_POP_UP_CLICK,
                eventLabel
        );
    }

    public void eventInstantLoanPermissionStatus(String eventLabel) {
        if (tracker == null) {
            return;
        }
        tracker.sendEventTracking(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_POP_UP_CLICK,
                eventLabel
        );
    }
}
