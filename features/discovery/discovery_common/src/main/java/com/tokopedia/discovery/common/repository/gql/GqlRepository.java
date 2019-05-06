package com.tokopedia.discovery.common.repository.gql;

import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.common.repository.Specification;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

class GqlRepository<T> implements Repository<T> {

    private GraphqlUseCase graphqlUseCase = new GraphqlUseCase();

    @Override
    public Observable<T> query(Specification specification, RequestParams requestParams) {
        final GqlSpecification gqlSpecification = (GqlSpecification)specification;
        final GraphqlRequest gqlRequest = new GraphqlRequest(gqlSpecification.getQuery(), gqlSpecification.getType(), requestParams.getParameters());

        graphqlUseCase.addRequest(gqlRequest);

        return graphqlUseCase.getExecuteObservable(requestParams).map(
                graphqlResponse -> graphqlResponse.getData(gqlSpecification.getType())
        );
    }
}
