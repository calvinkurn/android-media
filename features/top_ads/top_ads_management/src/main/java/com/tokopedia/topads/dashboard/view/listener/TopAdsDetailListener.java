package com.tokopedia.topads.dashboard.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.topads.dashboard.view.model.Ad;

/**
 * Created by zulfikarrahman on 8/14/17.
 */

public interface TopAdsDetailListener<V extends Ad> extends CustomerView {
    void onAdLoaded(V ad);

    void onAdEmpty();

    void onLoadAdError();
}
