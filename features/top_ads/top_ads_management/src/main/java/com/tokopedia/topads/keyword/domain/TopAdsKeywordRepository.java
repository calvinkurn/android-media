package com.tokopedia.topads.keyword.domain;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.topads.keyword.domain.model.EditTopAdsKeywordDetailDomainModel;
import com.tokopedia.topads.keyword.domain.model.KeywordDashboardDomain;
import com.tokopedia.topads.keyword.domain.model.TopAdsKeywordEditDetailInputDomainModel;
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

@Deprecated
public interface TopAdsKeywordRepository {
    Observable<KeywordDashboardDomain> getDashboardKeyword(RequestParams requestParams);

    Observable<EditTopAdsKeywordDetailDomainModel> editTopAdsKeywordDetail(TopAdsKeywordEditDetailInputDomainModel modelInput);

    Observable<AddKeywordDomainModel> addKeyword(AddKeywordDomainModel addKeywordDomainModel);
}
