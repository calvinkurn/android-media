package com.tokopedia.instantloan.data.soruce;

import com.tokopedia.instantloan.domain.model.BannerModelDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by lavekush on 22/03/18.
 */

public interface BannerDataStore {

    Observable<List<BannerModelDomain>> bannerList();
}
