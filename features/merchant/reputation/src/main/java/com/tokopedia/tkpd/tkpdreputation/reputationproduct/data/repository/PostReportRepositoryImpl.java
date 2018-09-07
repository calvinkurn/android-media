package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.repository;

import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.factory.ReputationProductDataFactory;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.ActResultDomain;

import java.util.Map;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public class PostReportRepositoryImpl implements PostReportRepository {
    private final ReputationProductDataFactory reputationProductDataFactory;

    public PostReportRepositoryImpl(ReputationProductDataFactory reputationProductDataFactory) {
        this.reputationProductDataFactory = reputationProductDataFactory;
    }

    @Override
    public Observable<ActResultDomain> getPostReportRepository(Map<String, String> requestParams) {

        return reputationProductDataFactory.getPostReportDataSource()
                .getPostReportDataSource(requestParams);
    }
}
