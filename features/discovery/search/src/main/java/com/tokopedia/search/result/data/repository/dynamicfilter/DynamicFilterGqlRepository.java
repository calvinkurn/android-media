package com.tokopedia.search.result.data.repository.dynamicfilter;

import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.common.repository.gql.GqlRepository;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.search.result.data.gql.dynamicfilter.GqlDynamicFilterResponse;
import com.tokopedia.search.utils.UrlParamUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS;
import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_QUERY;
import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_SOURCE;

final class DynamicFilterGqlRepository implements Repository<DynamicFilterModel> {

    private GqlRepository<GqlDynamicFilterResponse> gqlDynamicFilterResponseRepository;

    DynamicFilterGqlRepository(GqlRepository<GqlDynamicFilterResponse> gqlDynamicFilterResponseRepository) {
        this.gqlDynamicFilterResponseRepository = gqlDynamicFilterResponseRepository;
    }

    @Override
    public Observable<DynamicFilterModel> query(Map<String, Object> parameters) {
        return gqlDynamicFilterResponseRepository
                .query(createParametersForQuery(parameters))
                .map(GqlDynamicFilterResponse::getDynamicFilterModel);
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
