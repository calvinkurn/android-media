package com.tokopedia.home.account.analytics.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.analytics.data.model.UserAttributeData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author okasurya on 8/13/18.
 */
public class GetUserAttributesUseCase extends UseCase<UserAttributeData> {
    private static final long cacheDuration = TimeUnit.HOURS.toSeconds(3);
    private static final String PARAM_USER_ID = "userID"; //do not change this key
    private static final String OPERATION_NAME_VALUE = "ConsumerDrawerData";

    private GraphqlUseCase graphqlUseCase;
    private Context context;
    private UserSession userSession;

    @Inject
    public GetUserAttributesUseCase(GraphqlUseCase graphqlUseCase,
                                    @ApplicationContext Context context,
                                    UserSession userSession) {
        this.graphqlUseCase = graphqlUseCase;
        this.context = context;
        this.userSession = userSession;
    }

    @Override
    public Observable<UserAttributeData> createObservable(RequestParams requestParams) {
        return Observable.just(requestParams)
                .map(param -> {
                    String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.query_user_attribute);
                    Map<String, Object> variables = new HashMap<>();
                    variables.put(PARAM_USER_ID, Integer.parseInt(userSession.getUserId()));
                    return new GraphqlRequest(query, UserAttributeData.class, variables, false);
                })
                .flatMap((Func1<GraphqlRequest, Observable<GraphqlResponse>>) request -> {
                    GraphqlCacheStrategy cacheStrategy =
                            new GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                    .setExpiryTime(cacheDuration).setSessionIncluded(true).build();
                    graphqlUseCase.clearRequest();
                    graphqlUseCase.addRequest(request);
                    graphqlUseCase.setCacheStrategy(cacheStrategy);
                    return graphqlUseCase.createObservable(null);
                }).map(graphqlResponse -> graphqlResponse.getData(UserAttributeData.class));
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphqlUseCase.unsubscribe();
    }
}