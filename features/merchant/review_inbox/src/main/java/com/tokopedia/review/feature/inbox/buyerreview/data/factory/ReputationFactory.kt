package com.tokopedia.review.feature.inbox.buyerreview.data.factory

import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.InboxReputationDetailMapper
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.InboxReputationMapper
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.SendSmileyReputationMapper
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.ShopFavoritedMapper
import com.tokopedia.review.feature.inbox.buyerreview.data.source.CloudCheckShopFavoriteDataSource
import com.tokopedia.review.feature.inbox.buyerreview.data.source.CloudInboxReputationDataSource
import com.tokopedia.review.feature.inbox.buyerreview.data.source.CloudInboxReputationDetailDataSource
import com.tokopedia.review.feature.inbox.buyerreview.data.source.CloudSendSmileyReputationDataSource
import com.tokopedia.review.feature.inbox.buyerreview.data.source.LocalInboxReputationDataSource
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
    private val shopFavoritedMapper: ShopFavoritedMapper,
    private val persistentCacheManager: PersistentCacheManager,
    private val userSession: UserSessionInterface
) {

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

    fun createCloudCheckShopFavoriteDataSource(): CloudCheckShopFavoriteDataSource {
        return CloudCheckShopFavoriteDataSource(tomeService, shopFavoritedMapper)
    }
}