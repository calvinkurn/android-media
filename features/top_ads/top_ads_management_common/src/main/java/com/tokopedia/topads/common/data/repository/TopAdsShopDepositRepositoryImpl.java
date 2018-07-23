package com.tokopedia.topads.common.data.repository;

import com.tokopedia.topads.common.data.model.DataDeposit;
import com.tokopedia.topads.common.data.source.ShopDepositDataSource;
import com.tokopedia.topads.common.domain.repository.TopAdsShopDepositRepository;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by hadi.putra on 23/04/18.
 */

public class TopAdsShopDepositRepositoryImpl implements TopAdsShopDepositRepository {
    private final ShopDepositDataSource shopDepositDataSource;

    public TopAdsShopDepositRepositoryImpl(ShopDepositDataSource shopDepositDataSource) {
        this.shopDepositDataSource = shopDepositDataSource;
    }

    @Override
    public Observable<DataDeposit> getDeposit(RequestParams requestParams) {
        return shopDepositDataSource.getDeposit(requestParams);
    }
}
