package com.tokopedia.review.feature.inbox.buyerreview.data.factory

import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.*
import com.tokopedia.review.feature.inbox.buyerreview.data.source.*
import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationService
import com.tokopedia.review.feature.inbox.buyerreview.network.tome.TomeService
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by nisie on 8/14/17.
 */
class ReputationFactory @Inject constructor(
    private val tomeService: TomeService,
    private val reputationService: ReputationService,
    private val inboxReputationMapper: InboxReputationMapper,
    private val inboxReputationDetailMapper: InboxReputationDetailMapper,
    private val sendSmileyReputationMapper: SendSmileyReputationMapper,
    private val reportReviewMapper: ReportReviewMapper,
    private val shopFavoritedMapper: ShopFavoritedMapper,
    private val persistentCacheManager: PersistentCacheManager,
    private val replyReviewMapper: ReplyReviewMapper,
    private val deleteReviewResponseMapper: DeleteReviewResponseMapper,
    private val userSession: UserSessionInterface
) {

    fun createCloudDeleteReviewResponseDataSource(): CloudDeleteReviewResponseDataSource {
        return CloudDeleteReviewResponseDataSource(
            reputationService,
            deleteReviewResponseMapper, userSession
        )
    }

    fun createCloudInboxReputationDataSource(): CloudInboxReputationDataSource {
        return CloudInboxReputationDataSource(
            reputationService,
            inboxReputationMapper,
            persistentCacheManager,
            userSession
        )
    }

    fun createLocalInboxReputationDataSource(): LocalInboxReputationDataSource {
        return LocalInboxReputationDataSource(persistentCacheManager)
    }

    fun createCloudInboxReputationDetailDataSource(): CloudInboxReputationDetailDataSource {
        return CloudInboxReputationDetailDataSource(
            reputationService,
            inboxReputationDetailMapper,
            userSession
        )
    }

    fun createCloudSendSmileyReputationDataSource(): CloudSendSmileyReputationDataSource {
        return CloudSendSmileyReputationDataSource(
            reputationService,
            sendSmileyReputationMapper, userSession
        )
    }

    fun createCloudReportReviewDataSource(): CloudReportReviewDataSource {
        return CloudReportReviewDataSource(
            reputationService,
            reportReviewMapper, userSession
        )
    }

    fun createCloudCheckShopFavoriteDataSource(): CloudCheckShopFavoriteDataSource {
        return CloudCheckShopFavoriteDataSource(tomeService, shopFavoritedMapper)
    }

    fun createCloudReplyReviewDataSource(): CloudReplyReviewDataSource {
        return CloudReplyReviewDataSource(
            reputationService,
            replyReviewMapper, userSession
        )
    }
}