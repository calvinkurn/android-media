package com.tokopedia.graphql.data.repository;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
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
import com.tokopedia.graphql.util.CacheHelper;
import com.tokopedia.graphql.util.LoggingUtils;
import com.tokopedia.graphql.util.NullCheckerKt;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * This class will responsible for data fetching either from cloud or cache based on provided GraphqlCacheStrategy
 */
public class GraphqlRepositoryImpl implements GraphqlRepository {
    private GraphqlCloudDataStore mGraphqlCloudDataStore;
    private GraphqlCacheDataStore mGraphqlCache;
    private Map<Type, Object> mResults;

    private List<GraphqlRequest> mRefreshRequests;
    private Map<Type, Boolean> mIsCachedData;

    @Inject
    public GraphqlRepositoryImpl() {
        this.mGraphqlCloudDataStore = new GraphqlCloudDataStore();
        this.mGraphqlCache = new GraphqlCacheDataStore();

        mResults = new HashMap<>();
        mIsCachedData = new HashMap<>();
        mRefreshRequests = new ArrayList<>();
    }

    @Override
    public Observable<GraphqlResponse> getResponse(List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy) {
        mResults.clear();

        return Observable.defer(() -> {
            if (cacheStrategy == null
                    || cacheStrategy.getType() == CacheType.NONE
                    || cacheStrategy.getType() == CacheType.ALWAYS_CLOUD) {
                return getCloudResponse(requests, cacheStrategy);
            } else if (cacheStrategy.getType() == CacheType.CACHE_ONLY) {
                return mGraphqlCache.getResponse(requests, cacheStrategy);
            } else if (cacheStrategy.getType() == CacheType.CLOUD_THEN_CACHE) {
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
            Map<Type, List<GraphqlError>> errors = new HashMap<>();

            if (response.getOriginalResponse() != null) {
                for (int i = 0; i < response.getOriginalResponse().size(); i++) {
                    try {
                        JsonElement data = response.getOriginalResponse().get(i).getAsJsonObject().get(GraphqlConstant.GqlApiKeys.DATA);
                        if (data != null && !data.isJsonNull()) {
                            Type type = requests.get(i).getTypeOfT();
                            Object object = CommonUtils.fromJson(data.toString(), requests.get(i).getTypeOfT());
                            checkForNull(object, requests.get(i).getQuery(), requests.get(i).isShouldThrow());
                            //Lookup for data
                            mResults.put(type, object);
                            mIsCachedData.put(type, false);
                        }

                        JsonElement error = response.getOriginalResponse().get(i).getAsJsonObject().get(GraphqlConstant.GqlApiKeys.ERROR);
                        if (error != null && !error.isJsonNull()) {
                            //Lookup for error
                            errors.put(requests.get(i).getTypeOfT(), CommonUtils.fromJson(error.toString(), new TypeToken<List<GraphqlError>>() {
                            }.getType()));
                        }
                    } catch (JsonSyntaxException jse) {
                        jse.printStackTrace();
                        Map<String, String> messageMap = new HashMap<>();
                        messageMap.put("type", "json");
                        messageMap.put("err", Log.getStackTraceString(jse));
                        messageMap.put("req", String.valueOf(requests));
                        ServerLogger.log(Priority.P1, "GQL_PARSE_ERROR", messageMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Just to avoid any accidental data loss
                    }
                }
            }

            GraphqlResponse graphqlResponse = new GraphqlResponse(mResults, errors, mIsCachedData);

            if (mRefreshRequests.isEmpty()) {
                return graphqlResponse;
            }

            //adding cached request.
            graphqlResponse.setRefreshRequests(mRefreshRequests);

            return graphqlResponse;
        });
    }

    private Observable<GraphqlResponseInternal> getCloudResponse(List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy) {
        try {
            List<GraphqlRequest> copyRequests = new ArrayList<>();
            copyRequests.addAll(requests);

            int counter = copyRequests.size();
            for (int i = 0; i < counter; i++) {
                if (copyRequests.get(i).isNoCache()) {
                    continue;
                }

                String cachesResponse = mGraphqlCloudDataStore.getCacheManager()
                        .get(copyRequests.get(i).cacheKey());

                if (TextUtils.isEmpty(cachesResponse)) {
                    continue;
                }

                Object object = CommonUtils.fromJson(cachesResponse, copyRequests.get(i).getTypeOfT());
                checkForNull(object, copyRequests.get(i).getQuery(), copyRequests.get(i).isShouldThrow());
                //Lookup for data
                mResults.put(copyRequests.get(i).getTypeOfT(), object);

                LoggingUtils.logGqlSizeCached("java", requests.toString(), cachesResponse);

                mIsCachedData.put(copyRequests.get(i).getTypeOfT(), true);
                copyRequests.get(i).setNoCache(true);
                mRefreshRequests.add(copyRequests.get(i));
                requests.remove(copyRequests.get(i));

                Timber.d("Android CLC - Request served from cache " + CacheHelper.getQueryName(copyRequests.get(i).getQuery()) + " KEY: " + copyRequests.get(i).cacheKey());
            }
        } catch (JsonSyntaxException jse) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "json");
            messageMap.put("err", Log.getStackTraceString(jse));
            messageMap.put("req", String.valueOf(requests));
            ServerLogger.log(Priority.P1, "GQL_PARSE_ERROR", messageMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mGraphqlCloudDataStore.getResponse(requests, cacheStrategy);
    }

    private Observable<GraphqlResponseInternal> getCachedResponse(List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy) {
        if (cacheStrategy != null) {
            return mGraphqlCache.getResponse(requests, cacheStrategy);
        } else {
            return mGraphqlCache.getResponse(requests, new GraphqlCacheStrategy.Builder(CacheType.NONE).build());
        }
    }

    private void checkForNull(Object object, String request, boolean shouldThrow) {
        if (shouldThrow && !TextUtils.isEmpty(request)) {
            NullCheckerKt.throwIfNull(object, GraphqlUseCase.class, request);
        }
    }
}
