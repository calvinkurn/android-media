package com.tokopedia.tkpd.tkpdreputation.inbox.data.factory;

import com.tokopedia.tkpd.tkpdreputation.data.source.CloudGetLikeDislikeDataSource;
import com.tokopedia.tkpd.tkpdreputation.data.source.CloudLikeDislikeDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudDeleteReviewResponseDataSource;
import com.tokopedia.tkpd.tkpdreputation.network.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewProductService;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductGetHelpfulReviewCloud;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductGetListProductCloud;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductGetStarCountCloud;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * @author by nisie on 8/14/17.
 */

public class ReputationFactory {

    private final ReputationService reputationService;
    private final ReviewProductService reviewProductService;
    private final UserSessionInterface userSession;

    public ReputationFactory(ReputationService reputationService,
                             ReviewProductService reviewProductService,
                             UserSessionInterface userSession) {
        this.reputationService = reputationService;
        this.userSession = userSession;
        this.reviewProductService = reviewProductService;
    }

    public ReviewProductGetStarCountCloud createCloudGetReviewStarCount() {
        return new ReviewProductGetStarCountCloud(
                reviewProductService
        );
    }

    public ReviewProductGetHelpfulReviewCloud createCloudGetReviewHelpful() {
        return new ReviewProductGetHelpfulReviewCloud(reviewProductService);
    }

    public CloudGetLikeDislikeDataSource createCloudGetLikeDislikeDataSource() {
        return new CloudGetLikeDislikeDataSource(reputationService);
    }

    public ReviewProductGetListProductCloud createCloudGetReviewProductList() {
        return new ReviewProductGetListProductCloud(reviewProductService);
    }

    public CloudDeleteReviewResponseDataSource createCloudDeleteReviewResponseDataSource() {
        return new CloudDeleteReviewResponseDataSource(reputationService, userSession);
    }

    public CloudLikeDislikeDataSource createCloudLikeDislikeDataSource() {
        return new CloudLikeDislikeDataSource(reputationService, userSession);
    }
}
