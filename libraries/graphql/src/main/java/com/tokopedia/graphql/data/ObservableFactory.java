package com.tokopedia.graphql.data;

import androidx.annotation.NonNull;

import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.data.repository.GraphqlRepositoryImpl;

import java.util.List;

import rx.Observable;

public class ObservableFactory {
    public static Observable<GraphqlResponse> create(@NonNull List<GraphqlRequest> requests, GraphqlCacheStrategy cacheStrategy) {
        GraphqlRepositoryImpl repository = new GraphqlRepositoryImpl();
        return repository.getResponse(requests, cacheStrategy);
    }
}
