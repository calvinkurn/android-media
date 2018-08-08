package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.repository;

import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.ActResultDomain;

import java.util.Map;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public interface PostReportRepository {
    Observable<ActResultDomain> getPostReportRepository(Map<String, String> requestParams);
}
