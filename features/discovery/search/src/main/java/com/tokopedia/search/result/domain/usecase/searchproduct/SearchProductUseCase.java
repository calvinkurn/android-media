package com.tokopedia.search.result.domain.usecase.searchproduct;

import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.utils.UrlParamUtils;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_HEADLINE_PARAMS;
import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS;
import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_QUERY;
import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_SOURCE;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.HEADLINE;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.HEADLINE_ITEM_VALUE;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.HEADLINE_TEMPLATE_VALUE;

class SearchProductUseCase extends UseCase<SearchProductModel> {

    private Repository<SearchProductModel> repository;

    SearchProductUseCase(Repository<SearchProductModel> repository) {
        this.repository = repository;
    }

    @Override
    public Observable<SearchProductModel> createObservable(RequestParams requestParams) {
        return repository.query(requestParams.getParameters());
    }
}
