package com.tokopedia.topads.dashboard.domain;

import com.tokopedia.topads.dashboard.data.model.data.Etalase;

import java.util.List;

import rx.Observable;

/**
 * Created by Hendry on 2/20/17.
 */
public interface TopAdsEtalaseListRepository {
    Observable<List<Etalase>> getEtalaseList(String shopId);
}
