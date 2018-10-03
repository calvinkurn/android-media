package com.tokopedia.nps;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

/**
 * Created by meta on 03/10/18.
 */
public class NpsAnalytics {

    private AnalyticTracker analyticTracker;

    public NpsAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }
}
