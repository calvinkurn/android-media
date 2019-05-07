package com.tokopedia.discovery.common.repository.gql;

import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.common.repository.Specification;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

public class GqlRepository<T> implements Repository<T> {

    private GraphqlUseCase graphqlUseCase = new GraphqlUseCase();
    private GqlSpecification gqlSpecification;

    public GqlRepository(Specification gqlSpecification) {
        this.gqlSpecification = (GqlSpecification) gqlSpecification;
    }

    @Override
    public Observable<T> query(RequestParams requestParams) {
        final GraphqlRequest gqlRequest = new GraphqlRequest(gqlSpecification.getQuery(), gqlSpecification.getType(), requestParams.getParameters());

        graphqlUseCase.addRequest(gqlRequest);

        return graphqlUseCase.getExecuteObservable(requestParams).map(
                graphqlResponse -> graphqlResponse.getData(gqlSpecification.getType())
        );
    }
}