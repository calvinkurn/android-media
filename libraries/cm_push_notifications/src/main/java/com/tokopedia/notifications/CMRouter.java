package com.tokopedia.notifications;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

/**
 * Created by Ashwani Tyagi on 29/10/18.
 */
public interface CMRouter {
    String getUserId();

    AnalyticTracker getAnalyticTracker();
}
