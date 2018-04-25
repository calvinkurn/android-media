package com.tokopedia.topads.common.data.source.deposit;

import com.tokopedia.topads.common.data.model.DataDeposit;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by hadi.putra on 24/04/18.
 */

public class ShopDepositDataSource {

    private final ShopDepositDataSourceCloud shopDepositDataSourceCloud;

    public ShopDepositDataSource(ShopDepositDataSourceCloud shopDepositDataSourceCloud) {
        this.shopDepositDataSourceCloud = shopDepositDataSourceCloud;
    }

    public Observable<DataDeposit> getDeposit(RequestParams requestParams){
        return shopDepositDataSourceCloud.getDeposit(requestParams);
    }
}
