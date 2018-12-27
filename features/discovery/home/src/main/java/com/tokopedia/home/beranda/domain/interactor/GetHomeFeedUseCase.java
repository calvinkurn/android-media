package com.tokopedia.home.beranda.domain.interactor;

import android.content.Context;

import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedGqlResponse;
import com.tokopedia.home.beranda.domain.model.feed.FeedResult;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;

import rx.Observable;

public class GetHomeFeedUseCase extends UseCase<FeedResult> {
    public static final String PARAM_USER_ID = "userID";
    public static final String PARAM_CURSOR = "cursor";
    public static final String PARAM_PAGE = "limit";

    private Context context;
    private GraphqlUseCase graphqlUseCase;

    public GetHomeFeedUseCase(Context context, GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    @Override
    public Observable<FeedResult> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
        R.raw.gql_home_feed), HomeFeedGqlResponse.class, requestParams.getParameters());

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(RequestParams.EMPTY);
    }

    public RequestParams getFeedPlusParam(int page, String userId, String
            currentCursor) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_USER_ID, Integer.parseInt(userId));
        params.putString(PARAM_CURSOR, currentCursor);
        params.putInt(PARAM_PAGE, page);
        return params;
    }
}
