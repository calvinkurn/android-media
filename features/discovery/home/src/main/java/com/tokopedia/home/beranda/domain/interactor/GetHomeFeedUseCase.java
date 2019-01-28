package com.tokopedia.home.beranda.domain.interactor;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.data.mapper.HomeFeedMapper;
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedGqlResponse;
import com.tokopedia.home.beranda.domain.model.feed.FeedResult;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class GetHomeFeedUseCase extends UseCase<FeedResult> {
    public static final String PARAM_USER_ID = "userID";
    public static final String PARAM_CURSOR = "cursor";
    public static final String PARAM_LIMIT = "limit";

    private static final int PRODUCT_PER_CARD_LIMIT = 4;

    private Context context;
    private GraphqlUseCase graphqlUseCase;
    private HomeFeedMapper homeFeedMapper;

    public GetHomeFeedUseCase(Context context,
                              GraphqlUseCase graphqlUseCase,
                              HomeFeedMapper homeFeedMapper) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
        this.homeFeedMapper = homeFeedMapper;
    }

    @Override
    public Observable<FeedResult> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
        R.raw.gql_home_feed), HomeFeedGqlResponse.class, requestParams.getParameters());

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map(homeFeedMapper);
    }

    public RequestParams getFeedPlusParam(String userId, String
            currentCursor) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_USER_ID, Integer.parseInt(userId));
        params.putString(PARAM_CURSOR, currentCursor);
        params.putInt(PARAM_LIMIT, PRODUCT_PER_CARD_LIMIT);
        return params;
    }
}
