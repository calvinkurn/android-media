package com.tokopedia.tkpd.home.feed.data.mapper;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.network.entity.home.ProductFeedData3;
import com.tokopedia.tkpd.home.feed.domain.model.Badge;
import com.tokopedia.tkpd.home.feed.domain.model.Feed;
import com.tokopedia.tkpd.home.feed.domain.model.Label;
import com.tokopedia.tkpd.home.feed.domain.model.Paging;
import com.tokopedia.tkpd.home.feed.domain.model.ProductFeed;
import com.tokopedia.tkpd.home.feed.domain.model.Shop;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author Kulomady on 12/9/16.
 */

public class FeedMapperResult implements Func1<Response<String>, Feed> {
    private final Gson mGson;

    public FeedMapperResult(Gson gson) {
        mGson = gson;
    }

    @Override
    public Feed call(Response<String> stringResponse) {
        return mappingResponse(stringResponse);
    }

    private Feed mappingResponse(Response<String> response) {
        if (response.isSuccessful() && response.body() != null) {
            ProductFeedData3 feedResponse = mGson.fromJson(response.body(), ProductFeedData3.class);
            if (feedResponse != null) {
                return mappingValidFeedResponse(feedResponse);
            } else {
                return mappingInvalidFeedResponse();
            }
        } else {
            return mappingInvalidFeedResponse();
        }
    }


    @NonNull
    private Feed mappingInvalidFeedResponse() {
        Feed feed = new Feed();
        feed.setIsValid(false);
        return feed;
    }

    @NonNull
    private Feed mappingValidFeedResponse(ProductFeedData3 feedResponse) {
        ProductFeedData3.Result responseResult = feedResponse.getData();
        Feed feed = new Feed();
        feed.setIsValid(true);
        feed.setBreadcrumb(responseResult.getBreadcrumb());
        feed.setPaging(getPagingFromResponse(responseResult.getPaging()));
        feed.setDepartmentId(responseResult.getDepartmentId());
        feed.setHasCatalog(responseResult.getHasCatalog());
        feed.setHashtag(responseResult.getHashtag());
        feed.setProducts(getProductsFromResponse(responseResult));

        return feed;
    }

    @NonNull
    private List<ProductFeed> getProductsFromResponse(ProductFeedData3.Result responseResult) {
        if (responseResult.getProducts() != null) {
            List<ProductFeed> productList = new ArrayList<>();
            for (int i = 0; i < responseResult.getProducts().length; i++) {
                ProductFeedData3.Products productResponse = responseResult.getProducts()[i];
                ProductFeed product = new ProductFeed();
                product.setId(productResponse.getProductId());
                product.setImgUrl(productResponse.getProductImage());
                product.setName(productResponse.getProductName());
                product.setPrice(productResponse.getProductPrice());
                product.setWholesale(productResponse.getProductWholesale());
                product.setShop(getShopFromResponse(productResponse));
                product.setBadges(getBadgesFromResponse(productResponse));
                product.setLabels(getLabelsFromProduct(productResponse));
                productList.add(product);
            }
            return productList;
        } else {
            return new ArrayList<>();
        }
    }

    private List<Label> getLabelsFromProduct(ProductFeedData3.Products productResponse) {
        if (productResponse.getLabels() != null
                && productResponse.getLabels().size() > 0) {
            List<Label> labelList = new ArrayList<>();
            for (com.tokopedia.core.var.Label labelResponse : productResponse.getLabels()) {
                labelList.add(new Label(labelResponse.getTitle(), labelResponse.getColor()));
            }
            return labelList;
        } else {
            return new ArrayList<>();
        }
    }

    private List<Badge> getBadgesFromResponse(ProductFeedData3.Products productResponse) {

        if (productResponse.getBadges() != null && productResponse.getBadges().size() > 0) {
            List<Badge> badgeList = new ArrayList<>();
            for (com.tokopedia.core.var.Badge badgeResponse : productResponse.getBadges()) {
                Badge badge = new Badge(badgeResponse.getTitle(), badgeResponse.getImageUrl());
                badgeList.add(badge);
            }
            return badgeList;
        } else {
            return new ArrayList<>();
        }
    }

    private Shop getShopFromResponse(ProductFeedData3.Products productResponse) {
        Shop shop = new Shop();
        shop.setName(productResponse.getShopName());
        shop.setId(productResponse.getShopId());
        shop.setLocation(productResponse.getShopLocation());
        shop.setLucky(productResponse.getShopLucky());
        shop.setGoldStatus(productResponse.getShopGoldStatus());
        return shop;
    }

    private Paging getPagingFromResponse(ProductFeedData3.Paging pagingResult) {
        return new Paging(pagingResult.getUriNext(), pagingResult.getUriPrevious());
    }


}
