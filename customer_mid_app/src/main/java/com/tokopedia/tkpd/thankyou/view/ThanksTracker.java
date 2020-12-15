package com.tokopedia.tkpd.thankyou.view;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.tkpd.thankyou.view.viewmodel.ThanksTrackerData;

/**
 * Created by okasurya on 12/4/17.
 */

public interface ThanksTracker {
    interface Presenter {
        void doAnalytics(ThanksTrackerData data);

        void doAppsFlyerAnalytics(LocalCacheHandler cacheHandler, ThanksTrackerData data);
    }
}
