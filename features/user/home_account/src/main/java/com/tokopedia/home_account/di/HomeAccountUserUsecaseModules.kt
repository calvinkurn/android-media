package com.tokopedia.home_account.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common_wallet.balance.domain.GetWalletBalanceUseCase
import com.tokopedia.common_wallet.pendingcashback.domain.GetPendingCasbackUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home_account.domain.usecase.HomeAccountWalletBalanceUseCase
import com.tokopedia.home_account.domain.usecase.SafeSettingProfileUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class HomeAccountUserUsecaseModules {

    @HomeAccountUserScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @HomeAccountUserScope
    @Provides
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase


    @Provides
    fun provideGetWalletBalanceUseCase(
            @HomeAccountUserContext context: Context,
            graphqlUseCase: GraphqlUseCase,
                                        remoteConfig: RemoteConfig,
                                        userSession: UserSessionInterface): GetWalletBalanceUseCase {
        return GetWalletBalanceUseCase(context, graphqlUseCase, remoteConfig, userSession)
    }


    @Provides
    fun provideGetPendingCasbackUseCase(@HomeAccountUserContext context: Context,
                                        graphqlUseCase: GraphqlUseCase): GetPendingCasbackUseCase {
        return GetPendingCasbackUseCase(context, graphqlUseCase)
    }

    @Provides
    fun provideBuyerWalletBalance(getWalletBalanceUseCase: GetWalletBalanceUseCase,
                                  getPendingCasbackUseCase: GetPendingCasbackUseCase): HomeAccountWalletBalanceUseCase {
        return HomeAccountWalletBalanceUseCase(getWalletBalanceUseCase, getPendingCasbackUseCase)
    }

    @Provides
    fun provideSafeSettingUseCase(@HomeAccountUserContext context: Context,
                                  graphqlRepository: GraphqlRepository): SafeSettingProfileUseCase {
        return SafeSettingProfileUseCase(context, graphqlRepository)
    }
}