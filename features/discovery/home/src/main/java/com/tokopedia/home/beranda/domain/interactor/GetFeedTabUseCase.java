package com.tokopedia.home.beranda.domain.interactor;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.data.mapper.FeedTabMapper;
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedGqlResponse;
import com.tokopedia.home.beranda.presentation.view.viewmodel.FeedTabModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

public class GetFeedTabUseCase extends UseCase<List<FeedTabModel>> {

    private Context context;
    private GraphqlUseCase graphqlUseCase;
    private FeedTabMapper feedTabMapper;

    public GetFeedTabUseCase(Context context,
                             GraphqlUseCase graphqlUseCase,
                             FeedTabMapper feedTabMapper) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
        this.feedTabMapper = feedTabMapper;
    }

    @Override
    public Observable<List<FeedTabModel>> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
        R.raw.gql_home_feed_tab), HomeFeedGqlResponse.class, requestParams.getParameters());

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map(feedTabMapper);
    }
}
