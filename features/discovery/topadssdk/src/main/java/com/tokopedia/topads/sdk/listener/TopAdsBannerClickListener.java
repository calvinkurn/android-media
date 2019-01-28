package com.tokopedia.topads.sdk.listener;

import com.tokopedia.topads.sdk.domain.model.CpmData;

/**
 * Created by errysuprayogi on 12/29/17.
 */

public interface TopAdsBannerClickListener {

    void onBannerAdsClicked(String applink, CpmData data);

}
