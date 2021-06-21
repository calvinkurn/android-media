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
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Common use-case class for all graphql network response
 * Use kotlin version
 */
@Deprecated
public class GraphqlUseCase extends UseCase<GraphqlResponse> implements GraphqlUseCaseInterface {

    private List<GraphqlRequest> mRequests;
    private GraphqlCacheStrategy mCacheStrategy;
    private GraphqlRepositoryImpl graphqlRepository;

    private GraphqlCacheManager mCacheManager;
    private FingerprintManager mFingerprintManager;
    private Subscription cacheSubscription;

    //Usecase and subcriber for refresh the network calls
    private GraphqlUseCase mRefreshUseCase;
    private Subscriber<GraphqlResponse> mRefreshSubscription;

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

    @Override
    public void addRequest(GraphqlRequest requestObject) {
        this.mRequests.add(requestObject);
    }

    /**
     * For clearing all previous request, in order to reuse same UseCase instance
     */
    @Override
    public void clearRequest() {
        this.mRequests.clear();
    }

    public void setCacheStrategy(GraphqlCacheStrategy cacheStrategy) {
        this.mCacheStrategy = cacheStrategy;
    }

    public void clearCache() {
        cacheSubscription = Observable.fromCallable(() -> {
            initCacheManager();
            if (mRequests != null && !mRequests.isEmpty() && mCacheStrategy != null) {
                mCacheManager.delete(mFingerprintManager.generateFingerPrint(mRequests.toString(),
                        mCacheStrategy.isSessionIncluded()));
                return true;
            }
            return false;
        }).subscribeOn(Schedulers.io()).subscribe(
                new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable ignored) {

                    }

                    @Override
                    public void onNext(Boolean ignored) {

                    }
                }
        );
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

        return graphqlRepository.getResponse(mRequests, mCacheStrategy).doOnNext(new Action1<GraphqlResponse>() {

            /**
             * Invoking refresh
             * @param graphqlResponse
             */
            @Override
            public void call(GraphqlResponse graphqlResponse) {
                doRefresh(graphqlResponse);
            }
        });
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        if(cacheSubscription != null && !cacheSubscription.isUnsubscribed()) {
            cacheSubscription.unsubscribe();
        }
    }

    /**
     * Attaching refresh usecase
     * @param graphqlResponse
     */
    private void doRefresh(GraphqlResponse graphqlResponse) {
        if (graphqlResponse == null
                || graphqlResponse.getRefreshRequests() == null
                || graphqlResponse.getRefreshRequests().isEmpty()
                || mRefreshSubscription == null) {
            return;
        }

        for (GraphqlRequest request : graphqlResponse.getRefreshRequests()) {
            if (request == null) {
                continue;
            }

            request.setNoCache(true);
        }

        mRefreshUseCase.addRequests(graphqlResponse.getRefreshRequests());
        mRefreshUseCase.execute(mRefreshSubscription);
    }

    /**
     * To set refresh subscription e.g. pull to refresh or reresh button
     * @param refreshSubscription
     */
    public void setOnRefreshSubscription(Subscriber<GraphqlResponse> refreshSubscription) {
        this.mRefreshUseCase = new GraphqlUseCase();
        this.mRefreshSubscription = refreshSubscription;
    }
}
