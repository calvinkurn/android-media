package com.tokopedia.topads.keyword.domain.repository;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword;
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel;
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModelDatum;
import com.tokopedia.topads.keyword.view.model.KeywodDashboardViewModel;
import com.tokopedia.usecase.RequestParams;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import rx.Observable;

/**
 * Created by hadi.putra on 11/05/18.
 */

public interface TopAdsKeywordRepository {
    Observable<KeywodDashboardViewModel> getDashboardKeyword(RequestParams requestParams);

    Observable<PageDataResponse<DataBulkKeyword>> bulkActionKeyword(DataRequest<DataBulkKeyword> dataBulkKeyword);

    Observable<AddKeywordDomainModel> addKeywords(List<AddKeywordDomainModelDatum> keywords, String source);
}
