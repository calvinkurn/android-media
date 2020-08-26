package com.tokopedia.review.feature.reputationhistory.data.repository;

import com.tokopedia.review.feature.reputationhistory.data.source.cloud.CloudReputationReviewDataSource;
import com.tokopedia.review.feature.reputationhistory.domain.ReputationRepository;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 2/13/18.
 */

public class ReputationRepositoryImpl implements ReputationRepository {

    private CloudReputationReviewDataSource cloudReputationReviewDataSource;

    @Inject
    public ReputationRepositoryImpl(
            CloudReputationReviewDataSource cloudReputationReviewDataSource) {
        this.cloudReputationReviewDataSource = cloudReputationReviewDataSource;
    }
}
