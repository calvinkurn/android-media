package com.tokopedia.search.result.domain.usecase;

import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryPresenter;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.helper.UrlParamHelper;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_QUERY;
import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS;
import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_SOURCE;
import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_HEADLINE_PARAMS;

class SearchProductFirstPageUseCase extends UseCase<SearchProductModel> {

    private Repository<SearchProductModel> repository;

    SearchProductFirstPageUseCase(Repository<SearchProductModel> repository) {
        this.repository = repository;
    }

    @Override
    public Observable<SearchProductModel> createObservable(RequestParams requestParams) {
        return repository.query(createParametersForQuery(requestParams));
    }

    private Map<String, Object> createParametersForQuery(RequestParams requestParams) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(KEY_QUERY, requestParams.getString(SearchApiConst.Q, ""));
        variables.put(KEY_PARAMS, UrlParamHelper.generateUrlParamString(requestParams.getParamsAllValueInString()));
        variables.put(KEY_SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_PRODUCT);
        variables.put(KEY_HEADLINE_PARAMS, createHeadlineParams(requestParams));

        return variables;
    }

    private String createHeadlineParams(RequestParams requestParams) {
        Map<String, String> headlineParams = requestParams.getParamsAllValueInString();
        headlineParams.put(TopAdsParams.KEY_EP, DiscoveryPresenter.HEADLINE);
        headlineParams.put(TopAdsParams.KEY_TEMPLATE_ID, DiscoveryPresenter.TEMPLATE_VALUE);
        headlineParams.put(TopAdsParams.KEY_ITEM, DiscoveryPresenter.ITEM_VALUE);

        return UrlParamHelper.generateUrlParamString(headlineParams);
    }
}
