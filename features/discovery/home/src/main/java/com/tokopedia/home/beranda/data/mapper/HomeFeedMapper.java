package com.tokopedia.home.beranda.data.mapper;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedGqlResponse;
import com.tokopedia.home.beranda.domain.gql.feed.Product;
import com.tokopedia.home.beranda.domain.gql.feed.RecommendationProduct;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedListModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by henrypriyono on 12/29/17.
 */

public class HomeFeedMapper implements Func1<GraphqlResponse, HomeFeedListModel> {

    @Override
    public HomeFeedListModel call(GraphqlResponse graphqlResponse) {
        HomeFeedGqlResponse gqlResponse = graphqlResponse.getData(HomeFeedGqlResponse.class);
        RecommendationProduct recommendationProduct
                = gqlResponse.getHomeRecommendation().getRecommendationProduct();
        return convertToHomeFeedListModel(recommendationProduct);
    }

    private HomeFeedListModel convertToHomeFeedListModel(RecommendationProduct recommendationProduct) {
        HomeFeedListModel homeFeedListModel = new HomeFeedListModel();
        homeFeedListModel.setHomeFeedViewModels(
                convertToHomeFeedViewModels(recommendationProduct.getProduct()));
        homeFeedListModel.setHasNextPage(recommendationProduct.getHasNextPage());
        return homeFeedListModel;
    }

    private List<HomeFeedViewModel> convertToHomeFeedViewModels(List<Product> products) {
        List<HomeFeedViewModel> homeFeedViewModels = new ArrayList<>();
        for (int position = 0 ; position < products.size() ; position++) {
            Product product = products.get(position);

            homeFeedViewModels.add(new HomeFeedViewModel(
                    product.getId(),
                    product.getName(),
                    product.getCategoryBreadcrumbs(),
                    product.getRecommendationType(),
                    product.getImageUrl(),
                    product.getPrice(),
                    product.getPriceInt(),
                    product.getIsTopads(),
                    (position+1)
                    ));
        }
        return homeFeedViewModels;
    }
}