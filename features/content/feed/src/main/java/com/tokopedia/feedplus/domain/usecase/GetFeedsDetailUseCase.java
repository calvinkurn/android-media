package com.tokopedia.feedplus.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.data.pojo.FeedQuery;
import com.tokopedia.feedplus.view.subscriber.FeedDetailSubscriber;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.Map;

import javax.inject.Inject;

/**
 * @author by nisie on 5/24/17.
 */

public class GetFeedsDetailUseCase {

    private static final String PARAM_DETAIL_ID = "detailID";
    private static final String PARAM_PAGE = "pageDetail";
    private static final String PARAM_USER_ID = "userID";
    private static final String PARAM_LIMIT_DETAIL = "limitDetail";
    private static final int LIMIT_DETAIL = 30;

    private final Context context;
    private final GraphqlUseCase graphqlUseCase;

    @Inject
    GetFeedsDetailUseCase(@ApplicationContext Context context,
                          GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    public void execute(RequestParams requestParams, FeedDetailSubscriber subscriber) {
        graphqlUseCase.clearRequest();

        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.query_feed_detail);

        Map<String, Object> variables = requestParams.getParameters();

        GraphqlRequest feedDetailGraphqlRequest =
                new GraphqlRequest(query,
                        FeedQuery.class,
                        variables);

        graphqlUseCase.addRequest(feedDetailGraphqlRequest);
        graphqlUseCase.execute(subscriber);
    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }

    public static RequestParams getFeedDetailParam(String loginID, String detailId, int page) {
        RequestParams params = RequestParams.create();
        params.putInt(GetFeedsDetailUseCase.PARAM_USER_ID, Integer.valueOf(loginID));
        params.putString(GetFeedsDetailUseCase.PARAM_DETAIL_ID, detailId);
        params.putInt(GetFeedsDetailUseCase.PARAM_PAGE, page);
        params.putInt(GetFeedsDetailUseCase.PARAM_LIMIT_DETAIL, LIMIT_DETAIL);
        return params;
    }
}
