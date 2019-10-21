package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.topads.sdk.domain.model.CpmData;

public interface BannerAdsListener {

    void onBannerAdsClicked(int position, String applink, CpmData data);

    void onBannerAdsImpressionListener(int position, CpmData data);
}