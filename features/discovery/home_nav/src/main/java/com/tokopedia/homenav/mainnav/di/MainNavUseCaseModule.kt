package com.tokopedia.homenav.mainnav.di

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.common_wallet.balance.data.entity.WalletBalanceResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.homenav.mainnav.domain.interactor.GetCoroutineWalletBalanceUseCase
import com.tokopedia.homenav.mainnav.domain.interactor.GetShopInfoUseCase
import com.tokopedia.homenav.mainnav.domain.interactor.GetUserInfoUseCase
import com.tokopedia.homenav.mainnav.domain.interactor.GetUserMembershipUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class MainNavUseCaseModule {

    private val walletBalanceQuery : String = "{\n" +
            "  wallet(isGetTopup:true) {\n" +
            "    linked\n" +
            "    balance\n" +
            "    rawBalance\n" +
            "    text\n" +
            "    total_balance\n" +
            "    raw_total_balance\n" +
            "    hold_balance\n" +
            "    raw_hold_balance\n" +
            "    redirect_url\n" +
            "    applinks\n" +
            "    ab_tags {\n" +
            "      tag\n" +
            "    }\n" +
            "    action {\n" +
            "      text\n" +
            "      redirect_url\n" +
            "      applinks\n" +
            "      visibility\n" +
            "    }\n" +
            "    point_balance\n" +
            "    raw_point_balance\n" +
            "    cash_balance\n" +
            "    raw_cash_balance\n" +
            "    wallet_type\n" +
            "    help_applink\n" +
            "    tnc_applink\n" +
            "    show_announcement\n" +
            "    is_show_topup\n" +
            "    topup_applink\n" +
            "    topup_limit\n" +
            "  }\n" +
            "}"


    @MainNavScope
    @Provides
    fun getCoroutineWalletBalanceUseCase(graphqlRepository: GraphqlRepository, userSession: UserSessionInterface, remoteConfig: RemoteConfig, localCacheHandler: LocalCacheHandler): GetCoroutineWalletBalanceUseCase {
        val usecase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<WalletBalanceResponse>(graphqlRepository)
        usecase.setGraphqlQuery(walletBalanceQuery)
        return GetCoroutineWalletBalanceUseCase(usecase, remoteConfig, userSession, localCacheHandler)
    }


    @MainNavScope
    @Provides
    fun provideUserMembershipUseCase(graphqlRepository: GraphqlRepository) = GetUserMembershipUseCase(graphqlRepository)


    @MainNavScope
    @Provides
    fun provideShopInfoUseCase(graphqlRepository: GraphqlRepository) = GetShopInfoUseCase(graphqlRepository)


    @MainNavScope
    @Provides
    fun provideGetUserInfoUseCase(graphqlRepository: GraphqlRepository) = GetUserInfoUseCase(graphqlRepository)
}