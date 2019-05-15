package com.tokopedia.search.result.data.repository.searchproduct;

import com.tokopedia.discovery.common.repository.Specification;
import com.tokopedia.discovery.common.repository.gql.GqlRepository;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.utils.UrlParamUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS;

final class SearchProductLoadMoreGqlRepository extends GqlRepository<SearchProductModel> {

    SearchProductLoadMoreGqlRepository(Specification gqlSpecification) {
        super(gqlSpecification);
    }

    @Override
    public Observable<SearchProductModel> query(Map<String, Object> parameters) {
        return super.query(createParametersForQuery(parameters));
    }

    private Map<String, Object> createParametersForQuery(Map<String, Object> parameters) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(KEY_PARAMS, UrlParamUtils.generateUrlParamString(parameters));

        return variables;
    }
}
