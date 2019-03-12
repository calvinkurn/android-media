package com.tokopedia.graphql.data.repository;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.graphql.BuildConfig;
import com.tokopedia.graphql.CommonUtils;
import com.tokopedia.graphql.GraphqlConstant;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.data.model.GraphqlResponseInternal;
import com.tokopedia.graphql.data.source.cache.GraphqlCacheDataStore;
import com.tokopedia.graphql.data.source.cloud.GraphqlCloudDataStore;
import com.tokopedia.graphql.domain.GraphqlRepository;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kotlin.util.ContainNullException;
import com.tokopedia.kotlin.util.NullCheckerKt;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import kotlin.Unit;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * This class will responsible for data fetching either from cloud or cache based on provided GraphqlCacheStrategy
 */
public class GraphqlRepositoryImpl implements GraphqlRepository {
    private GraphqlCloudDataStore mGraphqlCloudDataStore;
    private GraphqlCacheDataStore mGraphqlCache;

    @Inject
    public GraphqlRepositoryImpl() {
        this.mGraphqlCloudDataStore = new GraphqlCloudDataStore();
        this.mGraphqlCache = new GraphqlCacheDataStore();
    }

    @Override
    public Observable<GraphqlResponse> getResponse(List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy) {
        return Observable.defer(() -> {
            if (cacheStrategy == null
                    || cacheStrategy.getType() == CacheType.NONE
                    || cacheStrategy.getType() == CacheType.ALWAYS_CLOUD) {
                return getCloudResponse(requests, cacheStrategy);
            } else if (cacheStrategy.getType() == CacheType.CACHE_ONLY) {
                return mGraphqlCache.getResponse(requests, cacheStrategy);
            } else if (cacheStrategy.getType() == CacheType.CLOUD_THEN_CACHE){
                return getCloudResponse(requests, cacheStrategy)
                        .onErrorReturn(
                                throwable -> getCachedResponse(requests, cacheStrategy).map(
                                        graphqlResponseInternal -> graphqlResponseInternal).toBlocking().first()
                        );
            } else {
                return Observable.concat(getCachedResponse(requests, cacheStrategy).subscribeOn(Schedulers.computation()),
                        getCloudResponse(requests, cacheStrategy).subscribeOn(Schedulers.io()))
                        .first(data -> data != null);
            }
        }).map(response -> {
            Map<Type, Object> results = new HashMap<>();
            Map<Type, List<GraphqlError>> errors = new HashMap<>();
            for (int i = 0; i < response.getOriginalResponse().size(); i++) {
                try {
                    JsonElement data = response.getOriginalResponse().get(i).getAsJsonObject().get(GraphqlConstant.GqlApiKeys.DATA);
                    if (data != null && !data.isJsonNull()) {
                        Type type = requests.get(i).getTypeOfT();
                        Object object = CommonUtils.fromJson(data.toString(), requests.get(i).getTypeOfT());
                        checkForNull(object, requests.get(i).getQuery(), requests.get(i).isShouldThrow());
                        //Lookup for data
                        results.put(type, object);
                    }

                    JsonElement error = response.getOriginalResponse().get(i).getAsJsonObject().get(GraphqlConstant.GqlApiKeys.ERROR);
                    if (error != null && !error.isJsonNull()) {
                        //Lookup for error
                        errors.put(requests.get(i).getTypeOfT(), CommonUtils.fromJson(error.toString(), new TypeToken<List<GraphqlError>>() {
                        }.getType()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //Just to avoid any accidental data loss
                }
            }
            return new GraphqlResponse(results, errors, response.isCached());
        });
    }

    private Observable<GraphqlResponseInternal> getCloudResponse(List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy) {
        return mGraphqlCloudDataStore.getResponse(requests, cacheStrategy);
    }

    private Observable<GraphqlResponseInternal> getCachedResponse(List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy) {
        if (cacheStrategy != null) {
            return mGraphqlCache.getResponse(requests, cacheStrategy);
        } else {
            return mGraphqlCache.getResponse(requests, new GraphqlCacheStrategy.Builder(CacheType.NONE).build());
        }
    }

    private void checkForNull(Object object, String query, boolean shouldThrow) {
        /*NullCheckerKt.isContainNull(object, errorMessage -> {
            String subQuery = (query.length() >= 16) ? query.substring(0, 16) : query;
            String message = String.format("Found %s in %s | shouldThrowException: %s | query: %s",
                    errorMessage,
                    GraphqlUseCase.class.getSimpleName(),
                    shouldThrow,
                    subQuery
            );
            ContainNullException exception = new ContainNullException(message);
            if (!BuildConfig.DEBUG) {
                Crashlytics.logException(exception);
            }
            if (shouldThrow) {
                throw exception;
            }
            return Unit.INSTANCE;
        });*/
    }
}
