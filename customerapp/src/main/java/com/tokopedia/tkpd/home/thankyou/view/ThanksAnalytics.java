package com.tokopedia.tkpd.home.thankyou.view;

import com.tokopedia.tkpd.home.thankyou.view.viewmodel.ThanksAnalyticsData;

/**
 * Created by okasurya on 12/4/17.
 */

public interface ThanksAnalytics {
    interface Presenter {
        void doAnalytics(ThanksAnalyticsData data);
    }
}
