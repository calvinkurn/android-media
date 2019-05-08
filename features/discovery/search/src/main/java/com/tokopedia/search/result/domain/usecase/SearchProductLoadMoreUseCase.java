package com.tokopedia.search.result.domain.usecase;

import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.newdiscovery.helper.UrlParamHelper;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS;

class SearchProductLoadMoreUseCase extends UseCase<SearchProductModel> {

    private Repository<SearchProductModel> repository;

    SearchProductLoadMoreUseCase(Repository<SearchProductModel> repository) {
        this.repository = repository;
    }

    @Override
    public Observable<SearchProductModel> createObservable(RequestParams requestParams) {
        return repository.query(createParametersForQuery(requestParams));
    }

    private Map<String, Object> createParametersForQuery(RequestParams requestParams) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(KEY_PARAMS, UrlParamHelper.generateUrlParamString(requestParams.getParamsAllValueInString()));

        return variables;
    }
}
