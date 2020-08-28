package com.tokopedia.review.feature.reputationhistory.data.repository;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.review.feature.reputationhistory.data.source.cloud.CloudReputationReviewDataSource;
import com.tokopedia.review.feature.reputationhistory.domain.ReputationReviewRepository;
import com.tokopedia.review.feature.reputationhistory.domain.model.SellerReputationDomain;
import com.tokopedia.review.feature.reputationhistory.util.ShopNetworkController;
import com.tokopedia.usecase.RequestParams;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author normansyahputa on 3/16/17.
 */

public class ReputationReviewRepositoryImpl implements ReputationReviewRepository {
    private CloudReputationReviewDataSource cloudReputationReviewDataSource;
    private ShopInfoRepository shopInfoRepository;

    @Deprecated
    public ReputationReviewRepositoryImpl(
            CloudReputationReviewDataSource cloudReputationReviewDataSource,
            ShopNetworkController shopNetworkController) {
        this.cloudReputationReviewDataSource = cloudReputationReviewDataSource;
    }

    @Inject
    public ReputationReviewRepositoryImpl(CloudReputationReviewDataSource cloudReputationReviewDataSource, ShopInfoRepositoryImpl shopInfoRepository) {
        this.cloudReputationReviewDataSource = cloudReputationReviewDataSource;
        this.shopInfoRepository = shopInfoRepository;
    }

    @Override
    public Observable<SellerReputationDomain> getReputationHistory(String shopId, Map<String, String> param) {
        return cloudReputationReviewDataSource.getReputationHistory(shopId, param);
    }

    @Override
    public Observable<ShopModel> getShopInfo(String userid, String deviceId, ShopNetworkController.ShopInfoParam shopInfoParam) {
        return shopInfoRepository.getShopInfo();
    }

    @Override
    public Observable<SellerReputationDomain> getReputationHistory(RequestParams requestParams) {
        return cloudReputationReviewDataSource.getReputationHistory(requestParams);
    }

    @Override
    public Observable<ShopModel> getShopInfo(RequestParams requestParams) {
        return shopInfoRepository.getShopInfo();
    }
}
