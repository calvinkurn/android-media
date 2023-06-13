package com.tokopedia.homenav.mainnav.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.homenav.mainnav.data.mapper.AccountHeaderMapper
import com.tokopedia.homenav.mainnav.data.mapper.BuListMapper
import com.tokopedia.homenav.mainnav.data.pojo.favoriteshop.FavoriteShopData
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.order.UohData
import com.tokopedia.homenav.mainnav.data.pojo.payment.Payment
import com.tokopedia.homenav.mainnav.data.pojo.review.ReviewProduct
import com.tokopedia.homenav.mainnav.data.pojo.tokopoint.TokopointsStatusFilteredPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.data.pojo.wishlist.GetWishlistCollection
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity
import com.tokopedia.homenav.mainnav.domain.usecases.*
import com.tokopedia.navigation_common.usecase.GetWalletAppBalanceUseCase
import com.tokopedia.navigation_common.usecase.GetWalletEligibilityUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusUseCase
import dagger.Module
import dagger.Provides

@Module
class MainNavUseCaseModule {
    @MainNavScope
    @Provides
    fun provideUserMembershipUseCase(graphqlRepository: GraphqlRepository) : GetUserMembershipUseCase{
        val useCase = GraphqlUseCase<MembershipPojo>(graphqlRepository)
        return GetUserMembershipUseCase(useCase)
    }

    @MainNavScope
    @Provides
    fun provideShopInfoUseCase(
        graphqlRepository: GraphqlRepository,
        userSession: UserSessionInterface
    ) : GetShopInfoUseCase {
        val useCase = GraphqlUseCase<ShopData>(graphqlRepository)
        return GetShopInfoUseCase(useCase, userSession)
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
            graphqlRepository: GraphqlRepository,
            remoteConfig: RemoteConfig): GetCategoryGroupUseCase {
        val useCase = GraphqlUseCase<DynamicHomeIconEntity>(graphqlRepository)
        val isUsingV2 = remoteConfig.getBoolean(RemoteConfigKey.HOME_USE_GQL_FED_QUERY, true)
        return GetCategoryGroupUseCase(buListMapper, useCase, isUsingV2)
    }

    @MainNavScope
    @Provides
    fun provideGetTokopointStatusFiltered(graphqlRepository: GraphqlRepository): GetTokopointStatusFiltered{
        val usecase = GraphqlUseCase<TokopointsStatusFilteredPojo>(graphqlRepository)
        return GetTokopointStatusFiltered(graphqlUseCase = usecase)
    }

    @MainNavScope
    @Provides
    fun provideGetWalletEligbilityUseCase(graphqlRepository: GraphqlRepository): GetWalletEligibilityUseCase{
        return GetWalletEligibilityUseCase(graphqlRepository)
    }

    @MainNavScope
    @Provides
    fun provideGetWalletAppUseCase(graphqlRepository: GraphqlRepository): GetWalletAppBalanceUseCase{
        return GetWalletAppBalanceUseCase(graphqlRepository)
    }

    @MainNavScope
    @Provides
    fun provideGetAffiliateUserUseCase(graphqlRepository: GraphqlRepository): GetAffiliateUserUseCase{
        return GetAffiliateUserUseCase(graphqlRepository)
    }

    @MainNavScope
    @Provides
    fun provideGetTokopediaPlusUseCase(graphqlRepository: GraphqlRepository, dispatcher: CoroutineDispatchers): TokopediaPlusUseCase{
        return TokopediaPlusUseCase(graphqlRepository, dispatcher)
    }

    @MainNavScope
    @Provides
    fun provideGetReviewProductUseCase(graphqlRepository: GraphqlRepository): GetReviewProductUseCase{
        val useCase = GraphqlUseCase<ReviewProduct>(graphqlRepository)
        return GetReviewProductUseCase(useCase)
    }

    @MainNavScope
    @Provides
    fun provideGetWishlistUseCase(graphqlRepository: GraphqlRepository): GetWishlistNavUseCase{
        val useCase = GraphqlUseCase<GetWishlistCollection>(graphqlRepository)
        return GetWishlistNavUseCase(useCase)
    }

    @MainNavScope
    @Provides
    fun provideGetFavoriteShopUseCase(graphqlRepository: GraphqlRepository, userSession: UserSessionInterface): GetFavoriteShopsNavUseCase{
        val useCase = GraphqlUseCase<FavoriteShopData>(graphqlRepository)
        return GetFavoriteShopsNavUseCase(useCase, userSession)
    }

    @MainNavScope
    @Provides
    fun provideGetProfileDataUseCase(
            accountHeaderMapper: AccountHeaderMapper,
            userInfoUseCase: GetUserInfoUseCase,
            getSaldoUseCase: GetSaldoUseCase,
            getUserMembershipUseCase: GetUserMembershipUseCase,
            getTokopointStatusFiltered: GetTokopointStatusFiltered,
            getShopInfoUseCase: GetShopInfoUseCase,
            getWalletEligibilityUseCase: GetWalletEligibilityUseCase,
            getWalletAppBalanceUseCase: GetWalletAppBalanceUseCase,
            getAffiliateUserUseCase: GetAffiliateUserUseCase,
            getTokopediaPlusUseCase: TokopediaPlusUseCase
    ): GetProfileDataUseCase {
        return GetProfileDataUseCase(
                accountHeaderMapper = accountHeaderMapper,
                getUserInfoUseCase = userInfoUseCase,
                getSaldoUseCase = getSaldoUseCase,
                getUserMembershipUseCase = getUserMembershipUseCase,
                getTokopointStatusFiltered = getTokopointStatusFiltered,
                getShopInfoUseCase = getShopInfoUseCase,
                getWalletEligibilityUseCase = getWalletEligibilityUseCase,
                getWalletAppBalanceUseCase = getWalletAppBalanceUseCase,
                getAffiliateUserUseCase = getAffiliateUserUseCase,
                getTokopediaPlusUseCase = getTokopediaPlusUseCase
        )
    }
    @MainNavScope
    @Provides
    fun provideGetAccountHeaderCachedUseCase(
            accountHeaderMapper: AccountHeaderMapper,
            userInfoUseCase: GetUserInfoUseCase,
            getUserMembershipUseCase: GetUserMembershipUseCase,
            getShopInfoUseCase: GetShopInfoUseCase,
            getAffiliateUserUseCase: GetAffiliateUserUseCase
    ): GetProfileDataCacheUseCase {
        return GetProfileDataCacheUseCase(
                accountHeaderMapper = accountHeaderMapper,
                getUserInfoUseCase = userInfoUseCase,
                getUserMembershipUseCase = getUserMembershipUseCase,
                getShopInfoUseCase = getShopInfoUseCase,
                getAffiliateUserUseCase = getAffiliateUserUseCase
        )
    }
}
