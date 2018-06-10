package com.tokopedia.topads.keyword.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.topads.keyword.data.mapper.KeywordAddDomainDataMapper;
import com.tokopedia.topads.keyword.data.mapper.KeywordDashboardMapper;
import com.tokopedia.topads.keyword.data.mapper.TopAdsKeywordEditDetailDataMapper;
import com.tokopedia.topads.keyword.data.model.TopAdsKeywordEditDetailInputDataModel;
import com.tokopedia.topads.keyword.data.source.cloud.api.DashboardKeywordCloud;
import com.tokopedia.topads.keyword.domain.model.EditTopAdsKeywordDetailDomainModel;
import com.tokopedia.topads.keyword.domain.model.KeywordDashboardDomain;
import com.tokopedia.topads.keyword.domain.model.TopAdsKeywordEditDetailInputDomainModel;
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author normansyahputa on 5/18/17.
 */

public class KeywordDashboardDataSouce {

    private DashboardKeywordCloud dashboardKeywordCloud;
    private KeywordDashboardMapper keywordDashboardMapper;
    private final TopAdsKeywordEditDetailDataMapper topAdsKeywordEditDetailDataMapper;

    @Inject
    public KeywordDashboardDataSouce(
            DashboardKeywordCloud dashboardKeywordCloud,
            KeywordDashboardMapper keywordDashboardMapper,
            TopAdsKeywordEditDetailDataMapper topAdsKeywordEditDetailDataMapper) {
        this.dashboardKeywordCloud = dashboardKeywordCloud;
        this.keywordDashboardMapper = keywordDashboardMapper;
        this.topAdsKeywordEditDetailDataMapper = topAdsKeywordEditDetailDataMapper;
    }

    public Observable<KeywordDashboardDomain> getKeywordDashboard(RequestParams requestParams) {
        return dashboardKeywordCloud.getDashboardKeyword(requestParams).map(keywordDashboardMapper);
    }

    public Observable<AddKeywordDomainModel> addKeyword(AddKeywordDomainModel addKeywordDomainModel) {
        return dashboardKeywordCloud.addKeyword(addKeywordDomainModel)
                .map(new KeywordAddDomainDataMapper());
    }


    public Observable<EditTopAdsKeywordDetailDomainModel> editTopAdsKeywordDetail(TopAdsKeywordEditDetailInputDomainModel modelInput) {
        TopAdsKeywordEditDetailInputDataModel dataModel = TopAdsKeywordEditDetailDataMapper.mapDomainToData(modelInput);
        return dashboardKeywordCloud.editTopAdsKeywordDetail(dataModel).map(topAdsKeywordEditDetailDataMapper);
    }
}
