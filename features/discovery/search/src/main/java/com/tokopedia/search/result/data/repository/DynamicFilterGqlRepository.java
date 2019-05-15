package com.tokopedia.search.result.data.repository;

import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.common.repository.Specification;
import com.tokopedia.discovery.common.repository.gql.GqlRepository;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.search.utils.UrlParamUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS;
import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_QUERY;
import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_SOURCE;

final class DynamicFilterGqlRepository extends GqlRepository<DynamicFilterModel> {

    DynamicFilterGqlRepository(Specification gqlSpecification) {
        super(gqlSpecification);
    }

    @Override
    public Observable<DynamicFilterModel> query(Map<String, Object> parameters) {
        return super.query(createParametersForQuery(parameters));
    }

    private Map<String, Object> createParametersForQuery(Map<String, Object> parameters) {
        Map<String, Object> variables = new HashMap<>();

        variables.put(KEY_QUERY, getQueryFromParameters(parameters));
        variables.put(KEY_PARAMS, UrlParamUtils.generateUrlParamString(parameters));
        variables.put(KEY_SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_PRODUCT);

        return variables;
    }

    private Object getQueryFromParameters(Map<String, Object> parameters) {
        Object query = parameters.get(SearchApiConst.Q);

        return query == null ? "" : query;
    }
}
