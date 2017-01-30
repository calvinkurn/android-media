package com.tokopedia.tkpd.home.feed.data.source;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.feed.domain.model.TopAds;

import java.util.List;

import rx.Observable;

/**
 * @author Kulomady on 12/9/16.
 */

public interface TopAdsDataSource {

    Observable<List<TopAds>> getTopAdsCloud(TKPDMapParam<String, String> queryMap);

    Observable<List<TopAds>> getTopAdsCache();
}
