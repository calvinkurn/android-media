package com.tokopedia.gamification.pdp.data.di.modules

import android.content.Context
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.pdp.data.di.scopes.GamificationPdpScope
import com.tokopedia.gamification.pdp.domain.Mapper
import com.tokopedia.gamification.pdp.domain.usecase.GamingRecommendationProductUseCase
import com.tokopedia.gamification.pdp.domain.usecase.KetupatBenefitCouponSlugUseCase
import com.tokopedia.gamification.pdp.domain.usecase.KetupatBenefitCouponUseCase
import com.tokopedia.gamification.pdp.domain.usecase.KetupatLandingUseCase
import com.tokopedia.gamification.pdp.domain.usecase.KetupatReferralEventTimeStampUseCase
import com.tokopedia.gamification.pdp.domain.usecase.KetupatReferralUserRegistrationUseCase
import com.tokopedia.gamification.pdp.repository.GamificationRepository
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import dagger.Module
import dagger.Provides

@Module(includes = [TopAdsWishlistModule::class,ActivityContextModule::class])
class PdpModule {

    @GamificationPdpScope
    @Provides
    fun provideKetupatReferralUserRegistrationUseCase(gamificationRepository: GamificationRepository): KetupatReferralUserRegistrationUseCase {
        return KetupatReferralUserRegistrationUseCase(gamificationRepository)
    }

    fun provideKetupatReferralEventTimeStampUseCase(gamificationRepository: GamificationRepository): KetupatReferralEventTimeStampUseCase {
        return KetupatReferralEventTimeStampUseCase(gamificationRepository)
    }

    @GamificationPdpScope
    @Provides
    fun provideKetupatLandingUseCase(gamificationRepository: GamificationRepository): KetupatLandingUseCase {
        return KetupatLandingUseCase(gamificationRepository)
    }

    @GamificationPdpScope
    @Provides
    fun provideKetupatBenefitCouponUseCase(gamificationRepository: GamificationRepository): KetupatBenefitCouponUseCase {
        return KetupatBenefitCouponUseCase(gamificationRepository)
    }

    @GamificationPdpScope
    @Provides
    fun provideKetupatBenefitCouponSlugUseCase(gamificationRepository: GamificationRepository): KetupatBenefitCouponSlugUseCase {
        return KetupatBenefitCouponSlugUseCase(gamificationRepository)
    }

    @GamificationPdpScope
    @Provides
    fun getGamingRecommendationProductUseCase(context: Context,
                                              graphqlUseCase: GraphqlUseCase,
                                              userSession: UserSessionInterface,
                                              mapper: Mapper): GamingRecommendationProductUseCase {
        return GamingRecommendationProductUseCase(context, graphqlUseCase, userSession, mapper)
    }

    @GamificationPdpScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @GamificationPdpScope
    @Provides
    fun provideAddToWishlistV2UseCase(graphqlRepository: GraphqlRepository): AddToWishlistV2UseCase {
        return AddToWishlistV2UseCase(graphqlRepository)
    }

    @GamificationPdpScope
    @Provides
    fun provideDeleteWishlistV2UseCase(graphqlRepository: GraphqlRepository): DeleteWishlistV2UseCase{
        return DeleteWishlistV2UseCase(graphqlRepository)
    }

    @GamificationPdpScope
    @Provides
    fun provideUserSession(context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @GamificationPdpScope
    @Provides
    fun provideMapper(): Mapper {
        return Mapper()
    }
}
