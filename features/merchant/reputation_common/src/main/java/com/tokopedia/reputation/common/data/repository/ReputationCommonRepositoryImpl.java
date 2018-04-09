package com.tokopedia.reputation.common.data.repository;

import com.tokopedia.reputation.common.data.source.ReputationCommonDataSource;
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed;
import com.tokopedia.reputation.common.domain.repository.ReputationCommonRepository;

import rx.Observable;

/**
 * @author hendry on 4/20/17.
 */

public class ReputationCommonRepositoryImpl implements ReputationCommonRepository {
    private final ReputationCommonDataSource reputationCommonDataSource;

    public ReputationCommonRepositoryImpl(ReputationCommonDataSource reputationCommonDataSource) {
        this.reputationCommonDataSource = reputationCommonDataSource;
    }

    @Override
    public Observable<ReputationSpeed> getStatisticSpeed(String shopId) {
        return reputationCommonDataSource.getStatisticSpeed(shopId);
    }

}
