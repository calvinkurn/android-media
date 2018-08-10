package com.tokopedia.shop.common.graphql.domain.usecase.base;

import android.content.Context;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public abstract class SingleGraphQLUseCase<T> extends UseCase<T> {

    private Context context;
    private GraphqlUseCase graphqlUseCase;
    private Class<T> tClass;

    public SingleGraphQLUseCase(Context context, Class<T> tClass) {
        this.context = context;
        this.graphqlUseCase = new GraphqlUseCase();
        this.tClass = tClass;
    }

    @Override
    public Observable<T> createObservable(RequestParams requestParams) {

        HashMap<String, Object> variables = createGraphQLVariable(requestParams);

        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                getGraphQLRawResId()), tClass, variables);

        /*GraphqlCacheStrategy graphqlCacheStrategy = new GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.HOUR.val())
                .setSessionIncluded(true)
                .build();*/
        graphqlUseCase.setCacheStrategy(createGraphQLCacheStrategy());

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);

        return graphqlUseCase.createObservable(RequestParams.create()).flatMap(new Func1<GraphqlResponse, Observable<T>>() {
            @Override
            public Observable<T> call(GraphqlResponse graphqlResponse) {
                T data = graphqlResponse.getData(tClass);
                if (data == null) {
                    List<GraphqlError> graphqlErrorList = graphqlResponse.getError(tClass);
                    if (graphqlErrorList != null && graphqlErrorList.size() > 0) {
                        GraphqlError graphqlError = graphqlErrorList.get(0);
                        return Observable.error(new MessageErrorException(graphqlError.getMessage()));
                    }
                    return Observable.just(null);
                } else {
                    return Observable.just(data);
                }
            }
        });

    }

    protected HashMap<String, Object> createGraphQLVariable(RequestParams requestParams) {
        return new HashMap<>();
    }

    protected abstract int getGraphQLRawResId();

    protected GraphqlCacheStrategy createGraphQLCacheStrategy() {
        return null;
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphqlUseCase.unsubscribe();
    }
}
