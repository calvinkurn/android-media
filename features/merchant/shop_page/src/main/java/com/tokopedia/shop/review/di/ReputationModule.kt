package com.tokopedia.shop.review.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.network.NetworkRouter
import com.tokopedia.shop.review.analytic.ReputationTracking
import com.tokopedia.shop.review.shop.data.factory.ReputationFactory
import com.tokopedia.shop.review.shop.data.mapper.DeleteReviewResponseMapper
import com.tokopedia.shop.review.shop.data.mapper.GetLikeDislikeMapper
import com.tokopedia.shop.review.shop.data.mapper.LikeDislikeMapper
import com.tokopedia.shop.review.shop.data.network.ReputationService
import com.tokopedia.shop.review.shop.data.network.ReviewProductService
import com.tokopedia.shop.review.shop.domain.DeleteReviewResponseUseCase
import com.tokopedia.shop.review.shop.domain.GetLikeDislikeReviewUseCase
import com.tokopedia.shop.review.shop.domain.LikeDislikeReviewUseCase
import com.tokopedia.shop.review.shop.domain.repository.ReputationRepository
import com.tokopedia.shop.review.shop.domain.repository.ReputationRepositoryImpl
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
    fun providePersistentCacheManager(@ApplicationContext context: Context?): PersistentCacheManager {
        return PersistentCacheManager(context!!)
    }

    @ReputationScope
    @Provides
    fun provideReputationRepository(reputationFactory: ReputationFactory): ReputationRepository {
        return ReputationRepositoryImpl(reputationFactory)
    }

    @ReputationScope
    @Provides
    fun provideReputationFactory(
            reputationService: ReputationService?,
            deleteReviewResponseMapper: DeleteReviewResponseMapper?,
            getLikeDislikeMapper: GetLikeDislikeMapper?,
            likeDislikeMapper: LikeDislikeMapper?,
            reviewProductService: ReviewProductService?,
            userSession: UserSessionInterface?): ReputationFactory {
        return ReputationFactory(
                reputationService,
                deleteReviewResponseMapper,
                getLikeDislikeMapper,
                likeDislikeMapper,
                reviewProductService,
                userSession)
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
    fun provideReputationService(@ApplicationContext context: Context?, networkRouter: NetworkRouter?, userSession: UserSession?): ReputationService {
        return ReputationService(
                context,
                networkRouter,
                userSession
        )
    }

    @ReputationScope
    @Provides
    fun provideReviewProductService(@ApplicationContext context: Context?, networkRouter: NetworkRouter?, userSession: UserSession?): ReviewProductService {
        return ReviewProductService(
                context,
                networkRouter,
                userSession
        )
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
    fun provideLikeDislikeReviewUseCase(reputationRepository: ReputationRepository): LikeDislikeReviewUseCase {
        return LikeDislikeReviewUseCase(reputationRepository)
    }

    @ReputationScope
    @Provides
    fun provideGetLikeDislikeMapper(): GetLikeDislikeMapper {
        return GetLikeDislikeMapper()
    }

    @ReputationScope
    @Provides
    fun provideGetLikeDislikeReviewUseCase(reputationRepository: ReputationRepository): GetLikeDislikeReviewUseCase {
        return GetLikeDislikeReviewUseCase(reputationRepository)
    }

    @ReputationScope
    @Provides
    fun provideLikeDislikeMapper(): LikeDislikeMapper {
        return LikeDislikeMapper()
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