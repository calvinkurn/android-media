package com.tokopedia.topads.common.sourcetagging.domain.repository;

import com.tokopedia.topads.common.sourcetagging.data.TopAdsSourceTaggingModel;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by hadi.putra on 17/04/18.
 */

public interface TopAdsSourceTaggingRepository {
    Observable<Void> saveSource(RequestParams requestParams);

    Observable<TopAdsSourceTaggingModel> getSource();

    Observable<Void> deleteSource();

    Observable<Void> checkTimeAndSaveSource(RequestParams requestParams);
}
