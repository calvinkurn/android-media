package com.tokopedia.graphql.domain;


import com.tokopedia.graphql.FingerprintManager;
import com.tokopedia.graphql.GraphqlCacheManager;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.data.repository.GraphqlRepositoryImpl;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Common use-case class for all graphql network response
 */
public class GraphqlUseCase extends UseCase<GraphqlResponse> {

    private List<GraphqlRequest> mRequests;
    private GraphqlCacheStrategy mCacheStrategy;
    private GraphqlRepositoryImpl graphqlRepository;

    private GraphqlCacheManager mCacheManager;
    private FingerprintManager mFingerprintManager;

    @Inject
    public GraphqlUseCase() {
        this.mRequests = new ArrayList<>();
        graphqlRepository = new GraphqlRepositoryImpl();
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

    public void clearCache() {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                initCacheManager();
                if (mRequests != null && !mRequests.isEmpty() && mCacheStrategy != null) {
                    mCacheManager.delete(mFingerprintManager.generateFingerPrint(mRequests.toString(),
                            mCacheStrategy.isSessionIncluded()));
                    return true;
                }
                return false;
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private void initCacheManager() {
        if (mCacheManager == null) {
            mCacheManager = new GraphqlCacheManager();
        }
        if (mFingerprintManager == null) {
            mFingerprintManager = GraphqlClient.getFingerPrintManager();
        }
    }

    @Override
    public Observable<GraphqlResponse> createObservable(RequestParams params) {
        if (mRequests == null || mRequests.isEmpty()) {
            throw new RuntimeException("Please set valid request parameter before executing the use-case");
        }
        return graphqlRepository.getResponse(mRequests, mCacheStrategy);
    }
}
