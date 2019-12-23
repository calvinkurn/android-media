package com.tokopedia.tkpd.tkpdreputation.inbox.data.factory;

import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.core.network.apiservices.tome.TomeService;
import com.tokopedia.core.network.apiservices.user.FaveShopActService;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.GetLikeDislikeMapper;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.LikeDislikeMapper;
import com.tokopedia.tkpd.tkpdreputation.data.source.CloudGetLikeDislikeDataSource;
import com.tokopedia.tkpd.tkpdreputation.data.source.CloudLikeDislikeDataSource;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.DeleteReviewResponseMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.FaveShopMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationDetailMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.ReplyReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.ReportReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewSubmitMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewValidateMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendSmileyReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.ShopFavoritedMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SkipReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudCheckShopFavoriteDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudDeleteReviewResponseDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudFaveShopDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudInboxReputationDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudInboxReputationDetailDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudReplyReviewDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudReportReviewDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudSendReviewDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudSendReviewSubmitDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudSendSmileyReputationDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudSkipReviewDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.LocalInboxReputationDataSource;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductApi;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductGetHelpfulReviewCloud;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductGetListProductCloud;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductGetStarCountCloud;
import com.tokopedia.tkpd.tkpdreputation.review.shop.data.source.ReviewShopGetListReviewCloud;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * @author by nisie on 8/14/17.
 */

public class ReputationFactory {

    private final ReputationService reputationService;
    private final TomeService tomeService;
    private final FaveShopActService faveShopActService;
    private final InboxReputationMapper inboxReputationMapper;
    private final PersistentCacheManager persistentCacheManager;
    private final InboxReputationDetailMapper inboxReputationDetailMapper;
    private final SendSmileyReputationMapper sendSmileyReputationMapper;
    private final SendReviewValidateMapper sendReviewValidateMapper;
    private final SendReviewSubmitMapper sendReviewSubmitMapper;
    private final SkipReviewMapper skipReviewMapper;
    private final ReportReviewMapper reportReviewMapper;
    private final ShopFavoritedMapper shopFavoritedMapper;
    private final FaveShopMapper faveShopMapper;
    private final DeleteReviewResponseMapper deleteReviewResponseMapper;
    private final ReplyReviewMapper replyReviewMapper;
    private final GetLikeDislikeMapper getLikeDislikeMapper;
    private final LikeDislikeMapper likeDislikeMapper;
    private final ReviewProductApi reputationReviewApi;
    private final UserSessionInterface userSession;

    public ReputationFactory(TomeService tomeService,
                             ReputationService reputationService,
                             InboxReputationMapper inboxReputationMapper,
                             InboxReputationDetailMapper inboxReputationDetailMapper,
                             SendSmileyReputationMapper sendSmileyReputationMapper,
                             SendReviewValidateMapper sendReviewValidateMapper,
                             SendReviewSubmitMapper sendReviewSubmitMapper,
                             SkipReviewMapper skipReviewMapper,
                             ReportReviewMapper reportReviewMapper,
                             ShopFavoritedMapper shopFavoritedMapper,
                             PersistentCacheManager persistentCacheManager,
                             FaveShopActService faveShopActService,
                             FaveShopMapper faveShopMapper,
                             DeleteReviewResponseMapper deleteReviewResponseMapper,
                             ReplyReviewMapper replyReviewMapper,
                             GetLikeDislikeMapper getLikeDislikeMapper,
                             LikeDislikeMapper likeDislikeMapper, ReviewProductApi reputationReviewApi,
                             UserSessionInterface userSession) {
        this.reputationService = reputationService;
        this.persistentCacheManager = persistentCacheManager;
        this.inboxReputationMapper = inboxReputationMapper;
        this.inboxReputationDetailMapper = inboxReputationDetailMapper;
        this.sendSmileyReputationMapper = sendSmileyReputationMapper;
        this.sendReviewValidateMapper = sendReviewValidateMapper;
        this.sendReviewSubmitMapper = sendReviewSubmitMapper;
        this.skipReviewMapper = skipReviewMapper;
        this.reportReviewMapper = reportReviewMapper;
        this.shopFavoritedMapper = shopFavoritedMapper;
        this.tomeService = tomeService;
        this.faveShopActService = faveShopActService;
        this.faveShopMapper = faveShopMapper;
        this.deleteReviewResponseMapper = deleteReviewResponseMapper;
        this.replyReviewMapper = replyReviewMapper;
        this.getLikeDislikeMapper = getLikeDislikeMapper;
        this.likeDislikeMapper = likeDislikeMapper;
        this.reputationReviewApi = reputationReviewApi;
        this.userSession = userSession;
    }

    public CloudInboxReputationDataSource createCloudInboxReputationDataSource() {
        return new CloudInboxReputationDataSource(reputationService, inboxReputationMapper, persistentCacheManager, userSession);
    }

    public LocalInboxReputationDataSource createLocalInboxReputationDataSource() {
        return new LocalInboxReputationDataSource(persistentCacheManager);
    }

    public CloudInboxReputationDetailDataSource createCloudInboxReputationDetailDataSource() {
        return new CloudInboxReputationDetailDataSource(
                reputationService,
                inboxReputationDetailMapper,
                userSession
        );
    }

    public CloudSendSmileyReputationDataSource createCloudSendSmileyReputationDataSource() {
        return new CloudSendSmileyReputationDataSource(reputationService,
                sendSmileyReputationMapper, userSession);
    }

    public CloudSendReviewDataSource createCloudSendReviewValidationDataSource() {
        return new CloudSendReviewDataSource(reputationService,
                sendReviewValidateMapper, userSession);
    }

    public CloudSendReviewSubmitDataSource createCloudSendReviewSubmitDataSource() {
        return new CloudSendReviewSubmitDataSource(reputationService,
                sendReviewSubmitMapper, userSession);
    }

    public CloudSkipReviewDataSource createCloudSkipReviewDataSource() {
        return new CloudSkipReviewDataSource(reputationService,
                skipReviewMapper, userSession);
    }

    public CloudReportReviewDataSource createCloudReportReviewDataSource() {
        return new CloudReportReviewDataSource(reputationService,
                reportReviewMapper, userSession);
    }

    public CloudCheckShopFavoriteDataSource createCloudCheckShopFavoriteDataSource() {
        return new CloudCheckShopFavoriteDataSource(tomeService, shopFavoritedMapper);
    }

    public CloudFaveShopDataSource createCloudFaveShopDataSource() {
        return new CloudFaveShopDataSource(faveShopActService, faveShopMapper, userSession);
    }

    public CloudDeleteReviewResponseDataSource createCloudDeleteReviewResponseDataSource() {
        return new CloudDeleteReviewResponseDataSource(reputationService,
                deleteReviewResponseMapper, userSession);
    }

    public CloudReplyReviewDataSource createCloudReplyReviewDataSource() {
        return new CloudReplyReviewDataSource(reputationService,
                replyReviewMapper, userSession);
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

    public ReviewShopGetListReviewCloud createCloudGetReviewShopList() {
        return new ReviewShopGetListReviewCloud(
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
