package com.tokopedia.feedplus.data.mapper;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.network.entity.home.ProductItemData;
import com.tokopedia.core.var.Label;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.feedplus.domain.model.recentview.RecentViewBadgeDomain;
import com.tokopedia.feedplus.domain.model.recentview.RecentViewLabelDomain;
import com.tokopedia.feedplus.domain.model.recentview.RecentViewProductDomain;
import com.tokopedia.feedplus.domain.model.recentview.RecentViewShopDomain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author Kulomady on 12/9/16.
 */

public class RecentProductMapper
        implements Func1<Response<DataResponse<ProductItemData>>, List<RecentViewProductDomain>> {

    private final Gson gson;

    public RecentProductMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public List<RecentViewProductDomain> call(
            Response<DataResponse<ProductItemData>> stringResponse) {
        return mappingResponse(stringResponse);
    }

    private List<RecentViewProductDomain> mappingResponse(Response<DataResponse<ProductItemData>> response) {
            if (response.body() != null
                    && response.isSuccessful()
                    && response.body().getData() != null
                    && response.body().getData().getList() != null
                    && response.body().getData().getList().size() > 0) {
                ProductItemData productItemData = response.body().getData();
                return getProductsFromResponse(productItemData);
            } else {
                return Collections.emptyList();
            }
    }

    @NonNull
    private List<RecentViewProductDomain> getProductsFromResponse(ProductItemData productItemData) {
        List<RecentViewProductDomain> results = new ArrayList<>();
        for (ProductItem productItem : productItemData.getList()) {
            results.add(new RecentViewProductDomain(
                    getShopFromResponse(productItem),
                    productItem.getId(),
                    productItem.getName(),
                    productItem.getPrice(),
                    productItem.getImgUri(),
                    String.valueOf(productItem.getIsNewGold()),
                    getProductLabelFromResponse(productItem.getLabels()),
                    productItem.getRating(),
                    productItem.getReviewCount(),
                    productItem.getFree_return(),
                    getProductBadgeFromResponse(productItem),
                    productItem.getWholesale(),
                    productItem.getPreorder(),
                    productItem.getIsWishlist()));
        }
        return results;
    }

    private List<RecentViewLabelDomain> getProductLabelFromResponse(List<Label> labels) {
        List<RecentViewLabelDomain> list = new ArrayList<>();
        for (Label label : labels) {
            list.add(new RecentViewLabelDomain(label.getTitle(), label.getColor()));
        }
        return list;
    }

    private RecentViewShopDomain getShopFromResponse(ProductItem productItem) {
        return new RecentViewShopDomain(String.valueOf(productItem.getShopId()),
                productItem.getShop(),
                productItem.getIsGold(),
                productItem.getLuckyShop(),
                productItem.getShopLocation());
    }


    private List<RecentViewBadgeDomain> getProductBadgeFromResponse(ProductItem productItem) {
        List<RecentViewBadgeDomain> badgeList = new ArrayList<>();
        for (com.tokopedia.core.var.Badge badgeResponse : productItem.getBadges()) {
            badgeList.add(new RecentViewBadgeDomain(badgeResponse.getTitle(), badgeResponse.getImageUrl()));
        }
        return badgeList;
    }


}
