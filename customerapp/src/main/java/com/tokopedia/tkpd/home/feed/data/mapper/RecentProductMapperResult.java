package com.tokopedia.tkpd.home.feed.data.mapper;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.network.entity.home.ProductItemData;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.tkpd.home.feed.data.source.cloud.entity.RecentProductResponse;
import com.tokopedia.tkpd.home.feed.domain.model.Badge;
import com.tokopedia.tkpd.home.feed.domain.model.Label;
import com.tokopedia.tkpd.home.feed.domain.model.ProductFeed;
import com.tokopedia.tkpd.home.feed.domain.model.Shop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author Kulomady on 12/9/16.
 */

public class RecentProductMapperResult
        implements Func1<Response<String>, List<ProductFeed>> {

    private final Gson mGson;

    public RecentProductMapperResult(Gson gson) {
        mGson = gson;
    }

    @Override
    public List<ProductFeed> call(Response<String> stringResponse) {
        return mappingResponse(stringResponse);
    }

    private List<ProductFeed> mappingResponse(Response<String> response) {
        if (response.body() != null && response.isSuccessful()) {
            RecentProductResponse recentProduct = mGson
                    .fromJson(response.body(), RecentProductResponse.class);

            if (recentProduct != null
                    && recentProduct.getData() != null
                    && recentProduct.getData().getList() != null
                    && recentProduct.getData().getList().size() > 0) {

                ProductItemData productItemData = recentProduct.getData();
                return getProductsFromResponse(productItemData);
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }

    @NonNull
    private List<ProductFeed> getProductsFromResponse(ProductItemData productItemData) {
        List<ProductFeed> results = new ArrayList<>();
        for (ProductItem productItem : productItemData.getList()) {
            ProductFeed product = new ProductFeed();
            product.setName(productItem.getName());
            product.setBadges(getProductBadgeFromResponse(productItem));
            product.setFreeReturn(productItem.getFree_return());
            product.setId(productItem.getId());
            product.setImgUrl(productItem.getImgUri());
            product.setLabels(getProductLabelsFromResponse(productItem));
            product.setPreorder(productItem.getPreorder());
            product.setPrice(productItem.getPrice());
            product.setShop(getShopFromResponse(productItem));
            product.setWholesale(productItem.getWholesale());
            results.add(product);
        }
        return results;
    }

    private Shop getShopFromResponse(ProductItem productItem) {
        Shop shop = new Shop();
        shop.setId(String.valueOf(productItem.getShopId()));
        shop.setLocation(productItem.getShopLocation());
        shop.setLucky(productItem.getLuckyShop());
        shop.setName(productItem.getShop());
        shop.setGoldStatus(productItem.getIsGold());
        return shop;
    }

    private List<Label> getProductLabelsFromResponse(ProductItem productItem) {
        List<Label> labelList = new ArrayList<>();
        for (com.tokopedia.core.var.Label labelResponse : productItem.getLabels()) {
            Label label = new Label(labelResponse.getTitle(), labelResponse.getColor());
            labelList.add(label);
        }
        return labelList;
    }

    private List<Badge> getProductBadgeFromResponse(ProductItem productItem) {
        List<Badge> badgeList = new ArrayList<>();
        for (com.tokopedia.core.var.Badge badgeResponse : productItem.getBadges()) {
            Badge badge = new Badge(badgeResponse.getTitle(), badgeResponse.getImageUrl());
            badgeList.add(badge);
        }
        return badgeList;
    }


}
