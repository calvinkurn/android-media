package com.tokopedia.gm.common.data.repository;

import com.tokopedia.gm.common.data.source.GMCommonDataSource;
import com.tokopedia.gm.common.data.source.cloud.model.GMGetCashbackModel;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.gm.common.domain.repository.GMCommonRepository;

import java.util.List;

import rx.Observable;

/**
 * @author hendry on 4/20/17.
 */

public class GMCommonRepositoryImpl implements GMCommonRepository {
    private final GMCommonDataSource gmCommonDataSource;

    public GMCommonRepositoryImpl(GMCommonDataSource gmCommonDataSource) {
        this.gmCommonDataSource = gmCommonDataSource;
    }

    @Override
    public Observable<List<GMFeaturedProduct>> getFeaturedProductList(String shopId) {
        return gmCommonDataSource.getFeaturedProductList(shopId);
    }

    @Override
    public Observable<Boolean> setCashback(String productId, int cashback) {
        return gmCommonDataSource.setCashback(productId, cashback);
    }

    @Override
    public Observable<List<GMGetCashbackModel>> getCashbackList(List<String> productIdList, String shopId) {
        return gmCommonDataSource.getCashbackList(productIdList, shopId);
    }

}
