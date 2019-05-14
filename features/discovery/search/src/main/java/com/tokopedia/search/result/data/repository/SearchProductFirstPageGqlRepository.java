package com.tokopedia.search.result.data.repository;

import com.tokopedia.discovery.common.repository.Specification;
import com.tokopedia.discovery.common.repository.gql.GqlRepository;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.utils.UrlParamUtils;
import com.tokopedia.topads.sdk.domain.TopAdsParams;

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

final class SearchProductFirstPageGqlRepository extends GqlRepository<SearchProductModel> {

    SearchProductFirstPageGqlRepository(Specification gqlSpecification) {
        super(gqlSpecification);
    }

    @Override
    public Observable<SearchProductModel> query(Map<String, Object> parameters) {
        return super.query(createParametersForQuery(parameters));
    }

    private Map<String, Object> createParametersForQuery(Map<String, Object> parameters) {
        Map<String, Object> variables = new HashMap<>();

        variables.put(KEY_QUERY, getQueryFromParameters(parameters));
        variables.put(KEY_PARAMS, UrlParamUtils.generateUrlParamString(parameters));
        variables.put(KEY_SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_PRODUCT);
        variables.put(KEY_HEADLINE_PARAMS, createHeadlineParams(parameters));

        return variables;
    }

    private Object getQueryFromParameters(Map<String, Object> parameters) {
        Object query = parameters.get(SearchApiConst.Q);

        return query == null ? "" : query;
    }

    private String createHeadlineParams(Map<String, Object> parameters) {
        Map<String, Object> headlineParams = new HashMap<>(parameters);
        headlineParams.put(TopAdsParams.KEY_EP, HEADLINE);
        headlineParams.put(TopAdsParams.KEY_TEMPLATE_ID, HEADLINE_TEMPLATE_VALUE);
        headlineParams.put(TopAdsParams.KEY_ITEM, HEADLINE_ITEM_VALUE);

        return UrlParamUtils.generateUrlParamString(headlineParams);
    }
}
