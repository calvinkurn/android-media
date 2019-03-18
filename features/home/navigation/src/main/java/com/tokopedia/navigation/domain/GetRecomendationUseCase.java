package com.tokopedia.navigation.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.data.entity.RecomendationEntity;
import com.tokopedia.navigation.domain.model.Recomendation;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Author errysuprayogi on 14,March,2019
 */
public class GetRecomendationUseCase extends UseCase<RecomendationEntity.RecomendationData> {

    public static final String USER_ID = "userID";
    public static final String X_SOURCE = "xSource";
    public static final String PAGE_NUMBER = "pageNumber";
    public static final String X_DEVICE = "xDevice";
    public static final String PAGE_NAME = "pageName";
    public static final String DEFAULT_VALUE_X_SOURCE = "inbox";
    public static final String DEFAULT_VALUE_X_DEVICE = "android";
    public static final String DEFAULT_PAGE_NAME = "default";
    private final GraphqlUseCase graphqlUseCase;
    private final Context context;
    private UserSession userSession;

    @Inject
    public GetRecomendationUseCase(Context context, GraphqlUseCase graphqlUseCase, UserSession userSession) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
        this.userSession = userSession;
    }

    @Override
    public Observable<RecomendationEntity.RecomendationData> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.query_inbox_recomendation), RecomendationEntity.class, requestParams.getParameters());
        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map(new Func1<GraphqlResponse, RecomendationEntity.RecomendationData>() {
                    @Override
                    public RecomendationEntity.RecomendationData call(GraphqlResponse graphqlResponse) {
                        RecomendationEntity entity = graphqlResponse.getData(RecomendationEntity.class);
                        return entity.getProductRecommendationWidget().getData().get(0);
                    }
                });
    }

    public RequestParams getRecomParams(int pageNumber) {
        RequestParams params = RequestParams.create();
        params.putInt(USER_ID, Integer.parseInt(userSession.getUserId()));
        params.putInt(PAGE_NUMBER, pageNumber);
        params.putString(X_SOURCE, DEFAULT_VALUE_X_SOURCE);
        params.putString(X_DEVICE, DEFAULT_VALUE_X_DEVICE);
        params.putString(PAGE_NAME, DEFAULT_PAGE_NAME);
        return params;
    }
}
