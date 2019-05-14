package com.tokopedia.search.result.data.repository;

import com.tokopedia.discovery.common.repository.Specification;
import com.tokopedia.discovery.common.repository.gql.GqlRepository;
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;
import com.tokopedia.search.utils.UrlParamUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS;

final class InitiateSearchGqlRepository extends GqlRepository<InitiateSearchModel> {

    InitiateSearchGqlRepository(Specification gqlSpecification) {
        super(gqlSpecification);
    }

    @Override
    public Observable<InitiateSearchModel> query(Map<String, Object> parameters) {
        return super.query(createParametersForQuery(parameters));
    }

    private Map<String, Object> createParametersForQuery(Map<String, Object> requestParams) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(KEY_PARAMS, UrlParamUtils.generateUrlParamString(requestParams));

        return variables;
    }
}
