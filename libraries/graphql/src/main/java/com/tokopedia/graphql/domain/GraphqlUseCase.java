package com.tokopedia.graphql.domain;


import com.tokopedia.graphql.data.ObservableFactory;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Common use-case class for all graphql network response
 */
public class GraphqlUseCase extends UseCase<GraphqlResponse> {

    private List<GraphqlRequest> mRequests;
    private GraphqlCacheStrategy mCacheStrategy;

    @Inject
    public GraphqlUseCase() {
        this.mRequests = new ArrayList<>();
    }

    /**
     * Method name refactoring
     *
     * @deprecated use {@link this.addRequest(List<GraphqlRequest>)} instead.
     */
    @Deprecated
    public void setRequest(List<GraphqlRequest> requests) {
        mRequests.addAll(requests);
    }

    /**
     * Method name refactoring
     *
     * @deprecated use {@link this.addRequest(GraphqlRequest )} instead.
     */
    @Deprecated
    public void setRequest(GraphqlRequest requestObject) {
        this.mRequests.add(requestObject);
    }

    public void addRequests(List<GraphqlRequest> requests) {
        mRequests.addAll(requests);
    }

    public void addRequest(GraphqlRequest requestObject) {
        this.mRequests.add(requestObject);
    }

    /**
     * For clearing all previous request, in order to reuse same UseCase instance
     */
    public void clearRequest() {
        this.mRequests.clear();
    }

    public void setCacheStrategy(GraphqlCacheStrategy cacheStrategy) {
        this.mCacheStrategy = cacheStrategy;
    }

    @Override
    public Observable<GraphqlResponse> createObservable(RequestParams params) {
        if (mRequests == null || mRequests.isEmpty()) {
            throw new RuntimeException("Please set valid request parameter before executing the use-case");
        }

        return ObservableFactory.create(mRequests, mCacheStrategy);
    }
}
