package com.tokopedia.tkpd.tkpdreputation.inbox.data.factory;

import com.tokopedia.tkpd.tkpdreputation.data.mapper.DeleteReviewResponseMapper;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.GetLikeDislikeMapper;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.LikeDislikeMapper;
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
    private final DeleteReviewResponseMapper deleteReviewResponseMapper;
    private final GetLikeDislikeMapper getLikeDislikeMapper;
    private final LikeDislikeMapper likeDislikeMapper;
    private final ReviewProductService reputationReviewApi;
    private final UserSessionInterface userSession;

    public ReputationFactory(ReputationService reputationService,
                             DeleteReviewResponseMapper deleteReviewResponseMapper,
                             GetLikeDislikeMapper getLikeDislikeMapper,
                             LikeDislikeMapper likeDislikeMapper,
                             ReviewProductService reputationReviewApi,
                             UserSessionInterface userSession) {
        this.reputationService = reputationService;
        this.deleteReviewResponseMapper = deleteReviewResponseMapper;
        this.getLikeDislikeMapper = getLikeDislikeMapper;
        this.likeDislikeMapper = likeDislikeMapper;
        this.reputationReviewApi = reputationReviewApi;
        this.userSession = userSession;
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
}
