package com.tokopedia.graphql.data;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.graphql.CommonUtils;
import com.tokopedia.graphql.GraphqlConstant;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.data.repository.GraphqlRepositoryImpl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

public class ObservableFactory {
    public static Observable<GraphqlResponse> create(@NonNull List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy) {
        GraphqlRepositoryImpl repository = new GraphqlRepositoryImpl();
        return repository.getResponse(requests, cacheStrategy)
                .map(response -> {
                    Map<Type, Object> results = new HashMap<>();
                    Map<Type, List<GraphqlError>> errors = new HashMap<>();
                    for (int i = 0; i < response.getOriginalResponse().size(); i++) {
                        try {
                            //Lookup for data
                            results.put(requests.get(i).getTypeOfT(),
                                    CommonUtils.fromJson(response.getOriginalResponse().get(i).getAsJsonObject().get(GraphqlConstant.GqlApiKeys.DATA).toString(),
                                            requests.get(i).getTypeOfT()));
                            //Lookup for error
                            errors.put(requests.get(i).getTypeOfT(),
                                    CommonUtils.fromJson(response.getOriginalResponse().get(i).getAsJsonObject().get(GraphqlConstant.GqlApiKeys.ERROR).toString(),
                                            new TypeToken<List<GraphqlError>>() {
                                            }.getType()));
                        } catch (Exception e) {
                            e.printStackTrace();
                            //Just to avoid any accidental data loss
                        }
                    }

                    return new GraphqlResponse(results, errors, response.isCached());
                });
    }
}
