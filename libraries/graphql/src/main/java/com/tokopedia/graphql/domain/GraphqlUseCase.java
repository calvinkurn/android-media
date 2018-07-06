package com.tokopedia.graphql.domain;


import com.google.gson.Gson;
import com.tokopedia.graphql.GraphqlConstant;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.data.model.GraphqlResponseInternal;
import com.tokopedia.graphql.data.repository.GraphqlRepositoryImpl;
import com.tokopedia.graphql.data.source.cache.GraphqlCacheDataStore;
import com.tokopedia.graphql.data.source.cloud.GraphqlCloudDataStore;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Common use-case class for all graphql network response
 */
public class GraphqlUseCase extends UseCase<GraphqlResponse> {

    private volatile  List<GraphqlRequest> mRequests;
    private GraphqlRepositoryImpl mGraphqlRepository;
    private GraphqlCacheStrategy mCacheStrategy;
    private Gson mGson;

    @Inject
    public GraphqlUseCase(GraphqlRepositoryImpl graphqlRepository, Gson gson) {
        this.mGraphqlRepository = graphqlRepository;
        this.mGson = gson;
        this.mRequests = new ArrayList<>();
    }

    public GraphqlUseCase() {
        this.mGraphqlRepository = new GraphqlRepositoryImpl(new GraphqlCloudDataStore(), new GraphqlCacheDataStore());
        this.mGson = new Gson();
        this.mRequests = new ArrayList<>();
    }

    public void setRequest(List<GraphqlRequest> requests) {
        mRequests.addAll(requests);
    }

    public void setRequest(GraphqlRequest requestObject) {
        this.mRequests.add(requestObject);
    }

    public void clearRequest() {
        if (this.mRequests != null)
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

        return mGraphqlRepository.getResponse(mRequests, mCacheStrategy)
                .map(new Func1<GraphqlResponseInternal, GraphqlResponse>() {
                    @Override
                    public GraphqlResponse call(GraphqlResponseInternal response) {
                        Map<Type, Object> results = new HashMap<>();

                        for (int i = 0; i < response.getOriginalResponse().size(); i++) {
                            try {
                                results.put(mRequests.get(i).getTypeOfT(),
                                        mGson.fromJson(response.getOriginalResponse().get(i).getAsJsonObject().get(GraphqlConstant.GqlApiKeys.DATA),
                                                mRequests.get(i).getTypeOfT()));
                            } catch (Exception e) {
                                //Just to avoid any accidental data loss
                                e.printStackTrace();
                            }
                        }

                        mRequests.clear(); //Invalidate all request params, inorder to avoid the duplicate query
                        return new GraphqlResponse(results, response.isCached());
                    }
                });
    }
}
