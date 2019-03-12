package com.tokopedia.home.account.analytics.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.analytics.data.model.UserAttributeData;
import com.tokopedia.navigation_common.model.SaldoModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private String saldoQuery;
    private UserSessionInterface userSession;

    @Inject
    public GetUserAttributesUseCase(GraphqlUseCase graphqlUseCase,
                                    @ApplicationContext Context context,
                                    UserSessionInterface userSession) {
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
                    GraphqlRequest userAttributeGqlRequest = new GraphqlRequest(query, UserAttributeData.class, variables);
                    GraphqlRequest saldoGraphql = new GraphqlRequest(saldoQuery, SaldoModel.class);

                    List<GraphqlRequest> graphqlRequestList = new ArrayList<>();

                    graphqlRequestList.add(userAttributeGqlRequest);
                    graphqlRequestList.add(saldoGraphql);

                    return graphqlRequestList;
                })
                .flatMap((Func1<List<GraphqlRequest>, Observable<GraphqlResponse>>) request -> {
                    GraphqlCacheStrategy cacheStrategy =
                            new GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                    .setExpiryTime(cacheDuration).setSessionIncluded(true).build();
                    graphqlUseCase.clearRequest();
                    graphqlUseCase.addRequests(request);
                    graphqlUseCase.setCacheStrategy(cacheStrategy);
                    return graphqlUseCase.createObservable(null);
                }).map(graphqlResponse -> {
                    UserAttributeData userAttributeData = graphqlResponse.getData(UserAttributeData.class);
                    SaldoModel saldoModel = graphqlResponse.getData(SaldoModel.class);
                    userAttributeData.setSaldo(saldoModel);
                    return userAttributeData;
                });
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphqlUseCase.unsubscribe();
    }

    public void setSaldoQuery(String saldoQuery) {
        this.saldoQuery = saldoQuery;
    }
}