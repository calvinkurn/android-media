package com.tokopedia.browse.common.util;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

/**
 * @author by furqan on 30/08/18.
 */

public class DigitalBrowseAnalytics {

    private AnalyticTracker analyticTracker;

    @Inject
    public DigitalBrowseAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }
}
