package com.tokopedia.tkpd.home.wishlist.mapper;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget;
import com.tokopedia.tkpd.home.wishlist.domain.model.GqlWishListDataResponse;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.ProductImage;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func2;

/**
 * Author errysuprayogi on 08,July,2019
 */
public class WishlistProductMapper implements Func2<GraphqlResponse, List<? extends RecommendationWidget>, GqlWishListDataResponse> {
    @Override
    public GqlWishListDataResponse call(GraphqlResponse graphqlResponse, List<? extends RecommendationWidget> recommendationWidgets) {
        GqlWishListDataResponse response = graphqlResponse.getData(GqlWishListDataResponse.class);
        response.setTopAdsModel(mappingTopAdsModel(recommendationWidgets.get(0)));
        return response;
    }

    private TopAdsModel mappingTopAdsModel(RecommendationWidget recom) {
        TopAdsModel model = new TopAdsModel();
        List<Data> data = new ArrayList<>();
        for (RecommendationItem r : recom.getRecommendationItemList()) {
            if (r.isTopAds()) {
                Data d = new Data();
                Product p = new Product();
                ProductImage img = new ProductImage();
                img.setM_url(r.getTrackerImageUrl());
                img.setM_ecs(r.getImageUrl());
                p.setId(String.valueOf(r.getProductId()));
                p.setName(r.getName());
                p.setApplinks(r.getAppUrl());
                p.setUri(r.getUrl());
                p.setImage(img);
                p.setPriceFormat(r.getPrice());
                p.setWishlist(r.isWishlist());
                d.setProduct(p);
                d.setProductClickUrl(r.getClickUrl());
                d.setProductWishlistUrl(r.getWishlistUrl());
                data.add(d);
            }
        }
        model.setData(data);
        return model;
    }
}
