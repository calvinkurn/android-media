package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.repository;

import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.factory.ReputationProductDataFactory;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.LikeDislikeDomain;

import java.util.Map;

/**
 * Created by yoasfs on 18/07/17.
 */

public class GetLikeDislikeRepositoryImpl implements GetLikeDislikeRepository {
    private final ReputationProductDataFactory reputationProductDataFactory;

    public GetLikeDislikeRepositoryImpl(ReputationProductDataFactory reputationProductDataFactory) {
        this.reputationProductDataFactory = reputationProductDataFactory;
    }

    @Override
    public rx.Observable<LikeDislikeDomain> getGetLikeDislikeRepository(Map<String, String> requestParams) {

        return reputationProductDataFactory.getCloudReputationProductDataSource()
                .getLikeDislikeReviewCloudSource(requestParams);
    }
}
