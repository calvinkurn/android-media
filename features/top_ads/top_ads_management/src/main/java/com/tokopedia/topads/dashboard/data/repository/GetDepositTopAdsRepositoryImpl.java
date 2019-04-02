package com.tokopedia.topads.dashboard.data.repository;

import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.topads.dashboard.data.source.GetDepositTopadsDataSource;
import com.tokopedia.topads.dashboard.domain.GetDepositTopAdsRepository;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/18/17.
 */

public class GetDepositTopAdsRepositoryImpl implements GetDepositTopAdsRepository {

    private GetDepositTopadsDataSource getDepositTopadsDataSource;

    public GetDepositTopAdsRepositoryImpl(GetDepositTopadsDataSource getDepositTopadsDataSource) {
        this.getDepositTopadsDataSource = getDepositTopadsDataSource;
    }

    @Override
    public Observable<DataDeposit> getDeposit(HashMap<String, String> params) {
        return getDepositTopadsDataSource.getDeposit(params);
    }
}
