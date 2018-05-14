package com.tokopedia.topads.keyword.domain.repository;

import com.tokopedia.topads.keyword.view.model.KeywodDashboardViewModel;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by hadi.putra on 11/05/18.
 */

public interface TopAdsKeywordRepository {
    Observable<KeywodDashboardViewModel> getDashboardKeyword(RequestParams requestParams);
}
