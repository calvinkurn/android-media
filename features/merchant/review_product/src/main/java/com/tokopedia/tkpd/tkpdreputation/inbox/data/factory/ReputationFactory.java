package com.tokopedia.tkpd.tkpdreputation.inbox.data.factory;

import com.tokopedia.tkpd.tkpdreputation.data.mapper.DeleteReviewResponseMapper;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.GetLikeDislikeMapper;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.LikeDislikeMapper;
import com.tokopedia.tkpd.tkpdreputation.data.source.CloudGetLikeDislikeDataSource;
import com.tokopedia.tkpd.tkpdreputation.data.source.CloudGetLikeDislikeDataSourceV2;
import com.tokopedia.tkpd.tkpdreputation.data.source.CloudLikeDislikeDataSource;
import com.tokopedia.tkpd.tkpdreputation.data.source.CloudLikeDislikeDataSourceV2;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudDeleteReviewResponseDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudDeleteReviewResponseDataSourceV2;
import com.tokopedia.tkpd.tkpdreputation.network.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.network.ReputationServiceV2;
import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewProductService;
import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewProductServiceV2;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductGetHelpfulReviewCloud;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductGetHelpfulReviewCloudV2;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductGetListProductCloud;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductGetListProductCloudV2;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductGetStarCountCloud;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductGetStarCountCloudV2;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * @author by nisie on 8/14/17.
 */

public class ReputationFactory {

    private final ReputationService reputationService;
    private final ReputationServiceV2 reputationServiceV2;
    private final DeleteReviewResponseMapper deleteReviewResponseMapper;
    private final GetLikeDislikeMapper getLikeDislikeMapper;
    private final LikeDislikeMapper likeDislikeMapper;
    private final ReviewProductService reputationReviewApi;
    private final ReviewProductServiceV2 reviewProductServiceV2;
    private final UserSessionInterface userSession;

    public ReputationFactory(ReputationService reputationService,
                             ReputationServiceV2 reputationServiceV2,
                             DeleteReviewResponseMapper deleteReviewResponseMapper,
                             GetLikeDislikeMapper getLikeDislikeMapper,
                             LikeDislikeMapper likeDislikeMapper,
                             ReviewProductService reputationReviewApi,
                             ReviewProductServiceV2 reviewProductServiceV2,
                             UserSessionInterface userSession) {
        this.reputationService = reputationService;
        this.reputationServiceV2 = reputationServiceV2;
        this.deleteReviewResponseMapper = deleteReviewResponseMapper;
        this.getLikeDislikeMapper = getLikeDislikeMapper;
        this.likeDislikeMapper = likeDislikeMapper;
        this.reputationReviewApi = reputationReviewApi;
        this.userSession = userSession;
        this.reviewProductServiceV2 = reviewProductServiceV2;
    }

    public CloudDeleteReviewResponseDataSource createCloudDeleteReviewResponseDataSource() {
        return new CloudDeleteReviewResponseDataSource(reputationService,
                deleteReviewResponseMapper, userSession);
    }

    public CloudGetLikeDislikeDataSource createCloudGetLikeDislikeDataSource() {
        return new CloudGetLikeDislikeDataSource(
                reputationService,
                getLikeDislikeMapper
        );
    }

    public CloudLikeDislikeDataSource createCloudLikeDislikeDataSource() {
        return new CloudLikeDislikeDataSource(
                reputationService,
                likeDislikeMapper,
                userSession
        );
    }

    public ReviewProductGetListProductCloud createCloudGetReviewProductList() {
        return new ReviewProductGetListProductCloud(
                reputationReviewApi
        );
    }

    public ReviewProductGetHelpfulReviewCloud createCloudGetReviewHelpful() {
        return new ReviewProductGetHelpfulReviewCloud(
                reputationReviewApi
        );
    }

    public ReviewProductGetStarCountCloud createCloudGetReviewStarCount() {
        return new ReviewProductGetStarCountCloud(
                reputationReviewApi
        );
    }

    public ReviewProductGetStarCountCloudV2 createCloudGetReviewStarCountV2() {
        return new ReviewProductGetStarCountCloudV2(
                reviewProductServiceV2
        );
    }

    public ReviewProductGetHelpfulReviewCloudV2 createCloudGetReviewHelpfulV2() {
        return new ReviewProductGetHelpfulReviewCloudV2(reviewProductServiceV2);
    }

    public CloudGetLikeDislikeDataSourceV2 createCloudGetLikeDislikeDataSourceV2() {
        return new CloudGetLikeDislikeDataSourceV2(reputationServiceV2);
    }

    public ReviewProductGetListProductCloudV2 createCloudGetReviewProductListV2() {
        return new ReviewProductGetListProductCloudV2(reviewProductServiceV2);
    }

    public CloudDeleteReviewResponseDataSourceV2 createCloudDeleteReviewResponseDataSourceV2() {
        return new CloudDeleteReviewResponseDataSourceV2(reputationServiceV2, userSession);
    }

    public CloudLikeDislikeDataSourceV2 createCloudLikeDislikeDataSourceV2() {
        return new CloudLikeDislikeDataSourceV2(reputationServiceV2, userSession);
    }
}
