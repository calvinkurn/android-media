package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.product.ReviewActService;
import com.tokopedia.core.network.apiservices.shop.ReputationActService;
import com.tokopedia.core.network.apiservices.shop.ShopService;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.mapper.ActResultMapper;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.mapper.LikeDislikeDomainMapper;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.source.CloudActResultDataSource;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.source.CloudDeleteCommentDataSource;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.source.CloudLikeDislikeDataSource;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.source.CloudPostReportDataSource;

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

    public ReputationProductDataFactory(Context context,
                                        ShopService shopService,
                                        ReviewActService reviewActService,
                                        ReputationActService reputationActService,
                                        LikeDislikeDomainMapper likeDislikeDomainMapper,
                                        ActResultMapper actResultMapper) {
        this.context = context;
        this.reviewActService = reviewActService;
        this.reputationActService = reputationActService;
        this.shopService = shopService;
        this.likeDislikeDomainMapper = likeDislikeDomainMapper;
        this.actResultMapper = actResultMapper;
    }
    public CloudLikeDislikeDataSource getCloudReputationProductDataSource() {
        return new CloudLikeDislikeDataSource(context,
                shopService,
                likeDislikeDomainMapper);
    }

    public CloudActResultDataSource getLikeDislikeReviewDataSource() {
        return new CloudActResultDataSource(context,
                reviewActService,
                actResultMapper);
    }

    public CloudPostReportDataSource getPostReportDataSource() {
        return new CloudPostReportDataSource(context,
                reviewActService,
                actResultMapper);

    }

    public CloudDeleteCommentDataSource getDeleteCommentDataSource() {
        return new CloudDeleteCommentDataSource(context,
                reputationActService,
                actResultMapper);

    }
}
