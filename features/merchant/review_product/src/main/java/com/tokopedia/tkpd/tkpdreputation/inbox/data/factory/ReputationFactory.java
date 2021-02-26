package com.tokopedia.tkpd.tkpdreputation.inbox.data.factory;

import com.tokopedia.tkpd.tkpdreputation.data.source.CloudGetLikeDislikeDataSourceV2;
import com.tokopedia.tkpd.tkpdreputation.data.source.CloudLikeDislikeDataSourceV2;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudDeleteReviewResponseDataSourceV2;
import com.tokopedia.tkpd.tkpdreputation.network.ReputationServiceV2;
import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewProductServiceV2;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductGetHelpfulReviewCloudV2;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductGetListProductCloudV2;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductGetStarCountCloudV2;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * @author by nisie on 8/14/17.
 */

public class ReputationFactory {

    private final ReputationServiceV2 reputationService;
    private final ReviewProductServiceV2 reviewProductService;
    private final UserSessionInterface userSession;

    public ReputationFactory(ReputationServiceV2 reputationService,
                             ReviewProductServiceV2 reviewProductService,
                             UserSessionInterface userSession) {
        this.reputationService = reputationService;
        this.userSession = userSession;
        this.reviewProductService = reviewProductService;
    }

    public ReviewProductGetStarCountCloudV2 createCloudGetReviewStarCount() {
        return new ReviewProductGetStarCountCloudV2(
                reviewProductService
        );
    }

    public ReviewProductGetHelpfulReviewCloudV2 createCloudGetReviewHelpful() {
        return new ReviewProductGetHelpfulReviewCloudV2(reviewProductService);
    }

    public CloudGetLikeDislikeDataSourceV2 createCloudGetLikeDislikeDataSource() {
        return new CloudGetLikeDislikeDataSourceV2(reputationService);
    }

    public ReviewProductGetListProductCloudV2 createCloudGetReviewProductList() {
        return new ReviewProductGetListProductCloudV2(reviewProductService);
    }

    public CloudDeleteReviewResponseDataSourceV2 createCloudDeleteReviewResponseDataSource() {
        return new CloudDeleteReviewResponseDataSourceV2(reputationService, userSession);
    }

    public CloudLikeDislikeDataSourceV2 createCloudLikeDislikeDataSource() {
        return new CloudLikeDislikeDataSourceV2(reputationService, userSession);
    }
}
