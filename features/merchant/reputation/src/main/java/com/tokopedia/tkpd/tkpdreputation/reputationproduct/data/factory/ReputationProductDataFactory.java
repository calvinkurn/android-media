package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.factory;

import android.content.Context;

import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewActService;
import com.tokopedia.tkpd.tkpdreputation.network.shop.ReputationActService;
import com.tokopedia.tkpd.tkpdreputation.network.shop.ShopService;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.mapper.ActResultMapper;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.mapper.LikeDislikeDomainMapper;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.source.CloudActResultDataSource;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.source.CloudDeleteCommentDataSource;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.source.CloudLikeDislikeDataSource;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.source.CloudPostReportDataSource;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * Created by yoasfs on 18/07/17.
 */

public class ReputationProductDataFactory {

    private Context context;
    private ShopService shopService;
    private ReviewActService reviewActService;
    private ReputationActService reputationActService;
    private LikeDislikeDomainMapper likeDislikeDomainMapper;
    private ActResultMapper actResultMapper;
    private UserSessionInterface userSessionInterface;

    public ReputationProductDataFactory(Context context,
                                        ShopService shopService,
                                        ReviewActService reviewActService,
                                        ReputationActService reputationActService,
                                        LikeDislikeDomainMapper likeDislikeDomainMapper,
                                        ActResultMapper actResultMapper,
                                        UserSessionInterface userSessionInterface) {
        this.context = context;
        this.reviewActService = reviewActService;
        this.reputationActService = reputationActService;
        this.shopService = shopService;
        this.likeDislikeDomainMapper = likeDislikeDomainMapper;
        this.actResultMapper = actResultMapper;
        this.userSessionInterface = userSessionInterface;
    }
    public CloudLikeDislikeDataSource getCloudReputationProductDataSource() {
        return new CloudLikeDislikeDataSource(
                shopService,
                likeDislikeDomainMapper,
                userSessionInterface
        );
    }

    public CloudActResultDataSource getLikeDislikeReviewDataSource() {
        return new CloudActResultDataSource(
                reviewActService,
                actResultMapper,
                userSessionInterface
        );
    }

    public CloudPostReportDataSource getPostReportDataSource() {
        return new CloudPostReportDataSource(
                reviewActService,
                actResultMapper,
                userSessionInterface
        );
    }

    public CloudDeleteCommentDataSource getDeleteCommentDataSource() {
        return new CloudDeleteCommentDataSource(
                reputationActService,
                actResultMapper,
                userSessionInterface
        );
    }
}
