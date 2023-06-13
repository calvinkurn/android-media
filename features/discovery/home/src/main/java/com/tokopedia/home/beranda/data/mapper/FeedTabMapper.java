package com.tokopedia.home.beranda.data.mapper;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedTabGqlResponse;
import com.tokopedia.home.beranda.domain.gql.feed.RecommendationTab;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.RecommendationTabDataModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

public class FeedTabMapper implements Func1<GraphqlResponse, List<RecommendationTabDataModel>> {
    @Override
    public List<RecommendationTabDataModel> call(GraphqlResponse graphqlResponse) {
        HomeFeedTabGqlResponse gqlResponse = graphqlResponse.getData(HomeFeedTabGqlResponse.class);
        return convertToFeedTabModelList(gqlResponse.getHomeRecommendation().getRecommendationTabs());
    }

    private List<RecommendationTabDataModel> convertToFeedTabModelList(List<RecommendationTab> recommendationTabs) {
        List<RecommendationTabDataModel> recommendationTabDataModelList = new ArrayList<>();
        for (int position = 0 ; position < recommendationTabs.size() ; position++){
            RecommendationTab recommendationTab = recommendationTabs.get(position);
            recommendationTabDataModelList.add(new RecommendationTabDataModel(
                    recommendationTab.getId(),
                    recommendationTab.getName(),
                    recommendationTab.getImageUrl(),
                    (position+1),
                    recommendationTab.getSourceType()
                    ));
        }
        return recommendationTabDataModelList;
    }
}
