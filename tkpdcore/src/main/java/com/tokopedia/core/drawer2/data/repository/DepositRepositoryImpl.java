package com.tokopedia.core.drawer2.data.repository;

import com.tokopedia.core.drawer2.data.factory.DepositSourceFactory;
import com.tokopedia.core.drawer2.data.pojo.deposit.DepositModel;
import com.tokopedia.core.drawer2.domain.DepositRepository;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by nisie on 5/4/17.
 */

public class DepositRepositoryImpl implements DepositRepository {

    private final DepositSourceFactory depositSourceFactory;

    public DepositRepositoryImpl(DepositSourceFactory depositSourceFactory) {
        this.depositSourceFactory = depositSourceFactory;
    }

    @Override
    public Observable<DepositModel> getDeposit(TKPDMapParam<String, Object> params) {
        return depositSourceFactory.createCloudDepositSource().getDeposit(params);
    }
}
