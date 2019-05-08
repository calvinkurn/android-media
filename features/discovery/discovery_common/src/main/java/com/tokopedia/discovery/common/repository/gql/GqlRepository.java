package com.tokopedia.discovery.common.repository.gql;

import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.common.repository.Specification;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.Map;

import rx.Observable;

public class GqlRepository<T> implements Repository<T> {

    private GraphqlUseCase graphqlUseCase = new GraphqlUseCase();
    private GqlSpecification gqlSpecification;

    public GqlRepository(Specification gqlSpecification) {
        this.gqlSpecification = (GqlSpecification) gqlSpecification;
    }

    @Override
    public Observable<T> query(Map<String, Object> parameters) {
        final GraphqlRequest gqlRequest = new GraphqlRequest(gqlSpecification.getQuery(), gqlSpecification.getType(), parameters);

        graphqlUseCase.addRequest(gqlRequest);

        return graphqlUseCase.getExecuteObservable(RequestParams.EMPTY).map(
                graphqlResponse -> graphqlResponse.getData(gqlSpecification.getType())
        );
    }
}