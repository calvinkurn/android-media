package com.tokopedia.review.feature.reputationhistory.domain;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;

import com.tokopedia.review.feature.reputationhistory.domain.model.SellerReputationDomain;
import com.tokopedia.review.feature.reputationhistory.util.ShopNetworkController;
import com.tokopedia.usecase.RequestParams;

import java.util.Map;

import rx.Observable;

/**
 * @author normansyahputa on 3/16/17.
 */

public interface ReputationReviewRepository {
    Observable<SellerReputationDomain> getReputationHistory(String shopId, Map<String, String> param);

    Observable<ShopModel> getShopInfo(String userid, String deviceId, ShopNetworkController.ShopInfoParam shopInfoParam);

    Observable<SellerReputationDomain> getReputationHistory(RequestParams requestParams);

    Observable<ShopModel> getShopInfo(RequestParams requestParams);
}
