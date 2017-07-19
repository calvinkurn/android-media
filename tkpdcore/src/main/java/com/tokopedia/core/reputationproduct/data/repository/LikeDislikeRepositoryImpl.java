package com.tokopedia.core.reputationproduct.data.repository;

import com.tokopedia.core.reputationproduct.data.factory.ReputationProductDataFactory;
import com.tokopedia.core.reputationproduct.domain.model.LikeDislikeDomain;

import java.util.Map;

/**
 * Created by yoasfs on 18/07/17.
 */

public class LikeDislikeRepositoryImpl implements LikeDislikeRepository {
    private final ReputationProductDataFactory reputationProductDataFactory;

    public LikeDislikeRepositoryImpl(ReputationProductDataFactory reputationProductDataFactory) {
        this.reputationProductDataFactory = reputationProductDataFactory;
    }

    @Override
    public rx.Observable<LikeDislikeDomain> getLikeDislikeRepository(Map<String, String> requestParams) {

        return reputationProductDataFactory.getCloudReputationProductDataSource()
                .getResCenterConversationMore(requestParams);
    }
}
