package com.tokopedia.home.beranda.data.mapper;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedGqlResponse;
import com.tokopedia.home.beranda.domain.gql.feed.RecommendationProduct;
import com.tokopedia.home.beranda.domain.gql.feed.RecommendationTab;
import com.tokopedia.home.beranda.presentation.view.viewmodel.FeedTabModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedListModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

public class FeedTabMapper implements Func1<GraphqlResponse, List<FeedTabModel>> {
    @Override
    public List<FeedTabModel> call(GraphqlResponse graphqlResponse) {
        HomeFeedGqlResponse gqlResponse = graphqlResponse.getData(HomeFeedGqlResponse.class);
        return convertToFeedTabModelList(gqlResponse.getHomeRecommendation().getRecommendationTabs());
    }

    private List<FeedTabModel> convertToFeedTabModelList(List<RecommendationTab> recommendationTabs) {
        List<FeedTabModel> feedTabModelList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            for (RecommendationTab recommendationTab : recommendationTabs) {
                feedTabModelList.add(new FeedTabModel(
                        recommendationTab.getId(),
                        recommendationTab.getName(),
                        recommendationTab.getImageUrl()));
            }
        }
        return feedTabModelList;
    }
}
