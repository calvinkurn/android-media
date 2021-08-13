package com.tokopedia.homenav.mainnav.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.common_wallet.balance.data.entity.WalletBalanceResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.homenav.mainnav.data.mapper.AccountHeaderMapper
import com.tokopedia.homenav.mainnav.data.mapper.BuListMapper
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.order.UohData
import com.tokopedia.homenav.mainnav.data.pojo.payment.Payment
import com.tokopedia.homenav.mainnav.data.pojo.tokopoint.TokopointsStatusFilteredPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity
import com.tokopedia.homenav.mainnav.domain.usecases.*
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
        val usecase = GraphqlUseCase<WalletBalanceResponse>(graphqlRepository)
        usecase.setGraphqlQuery(walletBalanceQuery)
        return GetCoroutineWalletBalanceUseCase(usecase, remoteConfig, userSession, localCacheHandler)
    }


    @MainNavScope
    @Provides
    fun provideUserMembershipUseCase(graphqlRepository: GraphqlRepository) : GetUserMembershipUseCase{
        val useCase = GraphqlUseCase<MembershipPojo>(graphqlRepository)
        return GetUserMembershipUseCase(useCase)
    }

    @MainNavScope
    @Provides
    fun provideShopInfoUseCase(graphqlRepository: GraphqlRepository) : GetShopInfoUseCase {
        val useCase = GraphqlUseCase<ShopData>(graphqlRepository)
        return GetShopInfoUseCase(useCase)
    }

    @MainNavScope
    @Provides
    fun provideGetSaldoUseCase(graphqlRepository: GraphqlRepository) = GetSaldoUseCase(graphqlRepository)

    @MainNavScope
    @Provides
    fun provideGetUserInfoUseCase(graphqlRepository: GraphqlRepository): GetUserInfoUseCase {
        val useCase = GraphqlUseCase<UserPojo>(graphqlRepository)
        return GetUserInfoUseCase(useCase)
    }

    @MainNavScope
    @Provides
    fun provideGetPaymentOrdersNavUseCase(graphqlRepository: GraphqlRepository): GetPaymentOrdersNavUseCase {
        val useCase = GraphqlUseCase<Payment>(graphqlRepository)
        return GetPaymentOrdersNavUseCase(useCase)
    }

    @MainNavScope
    @Provides
    fun provideGetUohOrdersNavUseCase(graphqlRepository: GraphqlRepository): GetUohOrdersNavUseCase {
        val useCase = GraphqlUseCase<UohData>(graphqlRepository)
        return GetUohOrdersNavUseCase(useCase)
    }

    @MainNavScope
    @Provides
    fun provideGetCategoryGroupUseCase(
            buListMapper: BuListMapper,
            graphqlRepository: GraphqlRepository): GetCategoryGroupUseCase {
        val useCase = GraphqlUseCase<DynamicHomeIconEntity>(graphqlRepository)
        return GetCategoryGroupUseCase(buListMapper, useCase)
    }

    @MainNavScope
    @Provides
    fun provideGetTokopointStatusFiltered(graphqlRepository: GraphqlRepository): GetTokopointStatusFiltered{
        val usecase = GraphqlUseCase<TokopointsStatusFilteredPojo>(graphqlRepository)
        return GetTokopointStatusFiltered(graphqlUseCase = usecase)
    }

    @MainNavScope
    @Provides
    fun provideGetProfileDataUseCase(
            accountHeaderMapper: AccountHeaderMapper,
            userInfoUseCase: GetUserInfoUseCase,
            getWalletBalanceUseCase: GetCoroutineWalletBalanceUseCase,
            getSaldoUseCase: GetSaldoUseCase,
            getUserMembershipUseCase: GetUserMembershipUseCase,
            getTokopointStatusFiltered: GetTokopointStatusFiltered,
            getShopInfoUseCase: GetShopInfoUseCase,
            userSession: UserSessionInterface,
            @ApplicationContext context: Context
    ): GetProfileDataUseCase {
        return GetProfileDataUseCase(
                accountHeaderMapper = accountHeaderMapper,
                getUserInfoUseCase = userInfoUseCase,
                getOvoUseCase = getWalletBalanceUseCase,
                getSaldoUseCase = getSaldoUseCase,
                getUserMembershipUseCase = getUserMembershipUseCase,
                getTokopointStatusFiltered = getTokopointStatusFiltered,
                getShopInfoUseCase = getShopInfoUseCase,
                userSession = userSession,
                context = context
        )
    }
    @MainNavScope
    @Provides
    fun provideGetAccountHeaderCachedUseCase(
            accountHeaderMapper: AccountHeaderMapper,
            userInfoUseCase: GetUserInfoUseCase,
            getUserMembershipUseCase: GetUserMembershipUseCase,
            getShopInfoUseCase: GetShopInfoUseCase,
            userSession: UserSessionInterface,
            @ApplicationContext context: Context
    ): GetProfileDataCacheUseCase {
        return GetProfileDataCacheUseCase(
                accountHeaderMapper = accountHeaderMapper,
                getUserInfoUseCase = userInfoUseCase,
                getUserMembershipUseCase = getUserMembershipUseCase,
                getShopInfoUseCase = getShopInfoUseCase,
                userSession = userSession,
                context = context
        )
    }
}