package com.tokopedia.gm.common.data.repository;

import com.tokopedia.gm.common.data.source.GMCommonDataSource;
import com.tokopedia.gm.common.domain.repository.GMCommonRepository;

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
    public Observable<Boolean> setCashback(String productId, int cashback) {
        return gmCommonDataSource.setCashback(productId, cashback);
    }

}
