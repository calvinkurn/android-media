package com.tokopedia.nps;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import static com.tokopedia.nps.NpsConstant.Analytic.*;

/**
 * Created by meta on 03/10/18.
 */
public class NpsAnalytics {

    private AnalyticTracker analyticTracker;

    public NpsAnalytics(Context context) {
        this.analyticTracker = ((AbstractionRouter)context.getApplicationContext()).getAnalyticTracker();
    }

    public void eventAppRatingImpression(String label) {
        analyticTracker.sendEventTracking(
                IMPRESSION_APP_RATING,
                APP_RATING,
                IMPRESSION,
                label
        );
    }

    public void eventClickAppRating(String label) {
        analyticTracker.sendEventTracking(
                CLICK_APP_RATING,
                APP_RATING,
                CLICK,
                label);
    }

    public void eventCancelAppRating(String label) {
        analyticTracker.sendEventTracking(
                CANCEL_APP_RATING,
                APP_RATING,
                CLICK,
                label
        );
    }
}
