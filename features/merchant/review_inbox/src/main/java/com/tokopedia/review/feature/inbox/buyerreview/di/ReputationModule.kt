package com.tokopedia.review.feature.inbox.buyerreview.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTracking
import com.tokopedia.review.feature.inbox.buyerreview.data.factory.ReputationFactory
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.*
import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository
import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepositoryImpl
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetCacheInboxReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetInboxReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.*
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.report.ReportReviewUseCase
import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationService
import com.tokopedia.review.feature.inbox.buyerreview.network.tome.TomeService
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * @author by nisie on 8/11/17.
 */
@Module
class ReputationModule {
    @ReputationScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @ReputationScope
    @Provides
    fun providePersistentCacheManager(@ApplicationContext context: Context?): PersistentCacheManager {
        return PersistentCacheManager(context!!)
    }

    @ReputationScope
    @Provides
    fun provideGetFirstTimeInboxReputationUseCase(
        getInboxReputationUseCase: GetInboxReputationUseCase,
        getCacheInboxReputationUseCase: GetCacheInboxReputationUseCase?,
        reputationRepository: ReputationRepository
    ): GetFirstTimeInboxReputationUseCase {
        return GetFirstTimeInboxReputationUseCase(
            getInboxReputationUseCase,
            getCacheInboxReputationUseCase,
            reputationRepository
        )
    }

    @ReputationScope
    @Provides
    fun provideGetInboxReputationUseCase(reputationRepository: ReputationRepository): GetInboxReputationUseCase {
        return GetInboxReputationUseCase(reputationRepository)
    }

    @ReputationScope
    @Provides
    fun provideReputationRepository(reputationFactory: ReputationFactory): ReputationRepository {
        return ReputationRepositoryImpl(reputationFactory)
    }

    @ReputationScope
    @Provides
    fun provideReputationFactory(
        tomeService: TomeService,
        reputationService: ReputationService,
        inboxReputationMapper: InboxReputationMapper?,
        inboxReputationDetailMapper: InboxReputationDetailMapper?,
        sendSmileyReputationMapper: SendSmileyReputationMapper?,
        shopFavoritedMapper: ShopFavoritedMapper?,
        reportReviewMapper: ReportReviewMapper?,
        persistentCacheManager: PersistentCacheManager,
        deleteReviewResponseMapper: DeleteReviewResponseMapper?,
        replyReviewMapper: ReplyReviewMapper?,
        userSession: UserSessionInterface
    ): ReputationFactory {
        return ReputationFactory(
            tomeService, reputationService, inboxReputationMapper,
            inboxReputationDetailMapper, sendSmileyReputationMapper, reportReviewMapper,
            shopFavoritedMapper,
            persistentCacheManager,
            replyReviewMapper,
            deleteReviewResponseMapper,
            userSession
        )
    }

    @ReputationScope
    @Provides
    fun provideInboxReputationMapper(): InboxReputationMapper {
        return InboxReputationMapper()
    }

    @ReputationScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context?): NetworkRouter {
        if (context is NetworkRouter) {
            return context
        }
        throw IllegalStateException("Application must implement NetworkRouter")
    }

    @ReputationScope
    @Provides
    fun provideReputationService(
        @ApplicationContext context: Context?,
        networkRouter: NetworkRouter?,
        userSession: UserSession?
    ): ReputationService {
        return ReputationService(
            context,
            networkRouter,
            userSession
        )
    }

    @ReputationScope
    @Provides
    fun provideTomeService(
        @ApplicationContext context: Context?,
        networkRouter: NetworkRouter?,
        userSession: UserSession?
    ): TomeService {
        return TomeService(
            context,
            networkRouter,
            userSession
        )
    }

    @ReputationScope
    @Provides
    fun provideInboxReputationDetailMapper(): InboxReputationDetailMapper {
        return InboxReputationDetailMapper()
    }

    @ReputationScope
    @Provides
    fun provideGetReviewUseCase(reputationRepository: ReputationRepository): GetReviewUseCase {
        return GetReviewUseCase(reputationRepository)
    }

    @ReputationScope
    @Provides
    fun provideGetInboxReputationDetailUseCase(
        getInboxReputationUseCase: GetInboxReputationUseCase,
        getReviewUseCase: GetReviewUseCase,
        checkShopFavoritedUseCase: CheckShopFavoritedUseCase
    ): GetInboxReputationDetailUseCase {
        return GetInboxReputationDetailUseCase(
            getInboxReputationUseCase,
            getReviewUseCase,
            checkShopFavoritedUseCase
        )
    }

    @ReputationScope
    @Provides
    fun provideSendSmileyReputationMapper(): SendSmileyReputationMapper {
        return SendSmileyReputationMapper()
    }

    @ReputationScope
    @Provides
    fun provideSendSmileyReputationUseCase(reputationRepository: ReputationRepository): SendSmileyReputationUseCase {
        return SendSmileyReputationUseCase(reputationRepository)
    }

    @ReputationScope
    @Provides
    fun provideReportReviewMapper(): ReportReviewMapper {
        return ReportReviewMapper()
    }

    @ReputationScope
    @Provides
    fun provideReportReviewUseCase(reputationRepository: ReputationRepository): ReportReviewUseCase {
        return ReportReviewUseCase(reputationRepository)
    }

    @ReputationScope
    @Provides
    fun provideGetCacheInboxReputationUseCase(reputationRepository: ReputationRepository): GetCacheInboxReputationUseCase {
        return GetCacheInboxReputationUseCase(reputationRepository)
    }

    @ReputationScope
    @Provides
    fun provideShopFavoritedMapper(): ShopFavoritedMapper {
        return ShopFavoritedMapper()
    }

    @ReputationScope
    @Provides
    fun provideCheckShopFavoritedUseCase(
        reputationRepository: ReputationRepository
    ): CheckShopFavoritedUseCase {
        return CheckShopFavoritedUseCase(reputationRepository)
    }

    @ReputationScope
    @Provides
    fun provideDeleteReviewResponseUseCase(reputationRepository: ReputationRepository): DeleteReviewResponseUseCase {
        return DeleteReviewResponseUseCase(reputationRepository)
    }

    @ReputationScope
    @Provides
    fun provideDeleteReviewResponseMapper(): DeleteReviewResponseMapper {
        return DeleteReviewResponseMapper()
    }

    @ReputationScope
    @Provides
    fun provideSendReplyReviewUseCase(reputationRepository: ReputationRepository): SendReplyReviewUseCase {
        return SendReplyReviewUseCase(reputationRepository)
    }

    @ReputationScope
    @Provides
    fun provideReplyReviewMapper(): ReplyReviewMapper {
        return ReplyReviewMapper()
    }

    @ReputationScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @ReputationScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context?): UserSession {
        return UserSession(context)
    }

    @ReputationScope
    @Provides
    fun reputationTracking(): ReputationTracking {
        return ReputationTracking()
    }
}