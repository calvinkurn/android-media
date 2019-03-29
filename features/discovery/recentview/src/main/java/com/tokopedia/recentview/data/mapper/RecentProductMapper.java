package com.tokopedia.recentview.data.mapper;


import android.support.annotation.NonNull;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.recentview.data.entity.Badge;
import com.tokopedia.recentview.data.entity.Label;
import com.tokopedia.recentview.data.entity.ProductItem;
import com.tokopedia.recentview.data.entity.RecentViewData;
import com.tokopedia.recentview.domain.model.RecentViewBadgeDomain;
import com.tokopedia.recentview.domain.model.RecentViewLabelDomain;
import com.tokopedia.recentview.domain.model.RecentViewProductDomain;
import com.tokopedia.recentview.domain.model.RecentViewShopDomain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author Kulomady on 12/9/16.
 */

public class RecentProductMapper
        implements Func1<Response<DataResponse<RecentViewData>>, List<RecentViewProductDomain>> {

    @Inject
    RecentProductMapper() {
    }

    @Override
    public List<RecentViewProductDomain> call(
            Response<DataResponse<RecentViewData>> stringResponse) {
        return mappingResponse(stringResponse);
    }

    private List<RecentViewProductDomain> mappingResponse(Response<DataResponse<RecentViewData>> response) {
        if (response.body() != null
                && response.isSuccessful()
                && response.body().getData() != null
                && response.body().getData().getList() != null
                && response.body().getData().getList().size() > 0) {
            RecentViewData productItemData = response.body().getData();
            return getProductsFromResponse(productItemData);
        } else {
            return Collections.emptyList();
        }
    }

    @NonNull
    private List<RecentViewProductDomain> getProductsFromResponse(RecentViewData productItemData) {
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
                    productItem.getWishlist()));
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
                productItem.getShop_location());
    }


    private List<RecentViewBadgeDomain> getProductBadgeFromResponse(ProductItem productItem) {
        List<RecentViewBadgeDomain> badgeList = new ArrayList<>();
        for (Badge badgeResponse : productItem.getBadges()) {
            badgeList.add(new RecentViewBadgeDomain(badgeResponse.getTitle(), badgeResponse.getImageUrl()));
        }
        return badgeList;
    }


}
