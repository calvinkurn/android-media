package com.tokopedia.review.feature.inbox.buyerreview.data.factory;

import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.DeleteReviewResponseMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.InboxReputationDetailMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.InboxReputationMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.ReplyReviewMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.ReportReviewMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.SendSmileyReputationMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.ShopFavoritedMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.source.CloudCheckShopFavoriteDataSource;
import com.tokopedia.review.feature.inbox.buyerreview.data.source.CloudDeleteReviewResponseDataSource;
import com.tokopedia.review.feature.inbox.buyerreview.data.source.CloudInboxReputationDataSource;
import com.tokopedia.review.feature.inbox.buyerreview.data.source.CloudInboxReputationDetailDataSource;
import com.tokopedia.review.feature.inbox.buyerreview.data.source.CloudReplyReviewDataSource;
import com.tokopedia.review.feature.inbox.buyerreview.data.source.CloudReportReviewDataSource;
import com.tokopedia.review.feature.inbox.buyerreview.data.source.CloudSendSmileyReputationDataSource;
import com.tokopedia.review.feature.inbox.buyerreview.data.source.LocalInboxReputationDataSource;
import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationService;
import com.tokopedia.review.feature.inbox.buyerreview.network.product.ReviewProductService;
import com.tokopedia.review.feature.inbox.buyerreview.network.shop.FaveShopActService;
import com.tokopedia.review.feature.inbox.buyerreview.network.tome.TomeService;
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
    private final ReportReviewMapper reportReviewMapper;
    private final ShopFavoritedMapper shopFavoritedMapper;
    private final FaveShopMapper faveShopMapper;
    private final ReplyReviewMapper replyReviewMapper;
    private final GetLikeDislikeMapper getLikeDislikeMapper;
    private final LikeDislikeMapper likeDislikeMapper;
    private final DeleteReviewResponseMapper deleteReviewResponseMapper;
    private final ReviewProductService reputationReviewApi;
    private final UserSessionInterface userSession;

    public ReputationFactory(TomeService tomeService,
                             ReputationService reputationService,
                             InboxReputationMapper inboxReputationMapper,
                             InboxReputationDetailMapper inboxReputationDetailMapper,
                             SendSmileyReputationMapper sendSmileyReputationMapper,
                             ReportReviewMapper reportReviewMapper,
                             ShopFavoritedMapper shopFavoritedMapper,
                             PersistentCacheManager persistentCacheManager,
                             FaveShopActService faveShopActService,
                             FaveShopMapper faveShopMapper,
                             ReplyReviewMapper replyReviewMapper,
                             GetLikeDislikeMapper getLikeDislikeMapper,
                             LikeDislikeMapper likeDislikeMapper,
                             ReviewProductService reputationReviewApi,
                             DeleteReviewResponseMapper deleteReviewResponseMapper,
                             UserSessionInterface userSession) {
        this.reputationService = reputationService;
        this.persistentCacheManager = persistentCacheManager;
        this.inboxReputationMapper = inboxReputationMapper;
        this.inboxReputationDetailMapper = inboxReputationDetailMapper;
        this.sendSmileyReputationMapper = sendSmileyReputationMapper;
        this.reportReviewMapper = reportReviewMapper;
        this.shopFavoritedMapper = shopFavoritedMapper;
        this.deleteReviewResponseMapper = deleteReviewResponseMapper;
        this.tomeService = tomeService;
        this.faveShopActService = faveShopActService;
        this.faveShopMapper = faveShopMapper;
        this.replyReviewMapper = replyReviewMapper;
        this.getLikeDislikeMapper = getLikeDislikeMapper;
        this.likeDislikeMapper = likeDislikeMapper;
        this.reputationReviewApi = reputationReviewApi;
        this.userSession = userSession;
    }

    public CloudDeleteReviewResponseDataSource createCloudDeleteReviewResponseDataSource() {
        return new CloudDeleteReviewResponseDataSource(reputationService,
                deleteReviewResponseMapper, userSession);
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

    public CloudReportReviewDataSource createCloudReportReviewDataSource() {
        return new CloudReportReviewDataSource(reputationService,
                reportReviewMapper, userSession);
    }

    public CloudCheckShopFavoriteDataSource createCloudCheckShopFavoriteDataSource() {
        return new CloudCheckShopFavoriteDataSource(tomeService, shopFavoritedMapper);
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
}
