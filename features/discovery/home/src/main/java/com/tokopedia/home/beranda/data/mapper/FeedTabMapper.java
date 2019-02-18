package com.tokopedia.home.beranda.data.mapper;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.home.BuildConfig;
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedGqlResponse;
import com.tokopedia.home.beranda.domain.gql.feed.RecommendationTab;
import com.tokopedia.home.beranda.presentation.view.viewmodel.FeedTabModel;
import com.tokopedia.kotlin.util.ContainNullException;
import com.tokopedia.kotlin.util.NullCheckerKt;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

public class FeedTabMapper implements Func1<GraphqlResponse, List<FeedTabModel>> {
    @Override
    public List<FeedTabModel> call(GraphqlResponse graphqlResponse) {
        HomeFeedGqlResponse gqlResponse = graphqlResponse.getData(HomeFeedGqlResponse.class);
        List<FeedTabModel> feedTabModelsList =
                convertToFeedTabModelList(gqlResponse.getHomeRecommendation().getRecommendationTabs());
        NullCheckerKt.isContainNull(feedTabModelsList, errorMessage -> {
            String message = String.format("Found %s in %s",
                    errorMessage, FeedTabMapper.class.getSimpleName());
            ContainNullException exception = new ContainNullException(message);
            if (!BuildConfig.DEBUG) {
                Crashlytics.logException(exception);
            }
            throw exception;
        });
        return feedTabModelsList;
    }

    private List<FeedTabModel> convertToFeedTabModelList(List<RecommendationTab> recommendationTabs) {
        List<FeedTabModel> feedTabModelList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int position = 0 ; position < recommendationTabs.size() ; position++){
                RecommendationTab recommendationTab = recommendationTabs.get(position);
                feedTabModelList.add(new FeedTabModel(
                        recommendationTab.getId(),
                        recommendationTab.getName(),
                        recommendationTab.getImageUrl(),
                        (position+1)
                        ));
            }
        }
        return feedTabModelList;
    }
}
