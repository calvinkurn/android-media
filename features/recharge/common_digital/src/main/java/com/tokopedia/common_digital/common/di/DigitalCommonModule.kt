package com.tokopedia.common_digital.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.usecase.RechargePushEventRecommendationUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by Rizky on 13/08/18.
 */
@Module
class DigitalCommonModule {

    @Provides
    @DigitalCommonScope
    fun provideRechargePushEventRecommendationUseCase(@ApplicationContext context: Context): RechargePushEventRecommendationUseCase {
        return RechargePushEventRecommendationUseCase(GraphqlUseCase(), context)
    }

    @Provides
    @DigitalCommonScope
    fun provideRechargeAnalytics(rechargePushEventRecommendationUseCase: RechargePushEventRecommendationUseCase): RechargeAnalytics {
        return RechargeAnalytics(rechargePushEventRecommendationUseCase)
    }

    @DigitalCommonScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}
