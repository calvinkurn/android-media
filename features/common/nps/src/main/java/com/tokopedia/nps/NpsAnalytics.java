package com.tokopedia.nps;

import android.content.Context;

import javax.inject.Inject;

import static com.tokopedia.nps.NpsConstant.Analytic.*;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

/**
 * Created by meta on 03/10/18.
 */
public class NpsAnalytics {

    public NpsAnalytics() {
    }

    public void eventAppRatingImpression(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                IMPRESSION_APP_RATING,
                APP_RATING,
                IMPRESSION,
                label
        ));
    }

    public void eventClickAppRating(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                CLICK_APP_RATING,
                APP_RATING,
                CLICK,
                label));
    }

    public void eventCancelAppRating(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                CANCEL_APP_RATING,
                APP_RATING,
                CLICK,
                label
        ));
    }
}
