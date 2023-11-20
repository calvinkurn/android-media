package com.tokopedia.shop.product.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.network.NetworkRouter
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipStampProgress
import com.tokopedia.shop.product.data.model.ShopFeaturedProduct
import com.tokopedia.shop.product.di.scope.ShopProductScope
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.universal_sharing.view.usecase.AffiliateEligibilityCheckUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [ShopProductViewModelModule::class])
class ShopProductModule {
    @ShopProductScope
    @Provides
    fun getNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @ShopProductScope
    @Provides
    fun provideGraphqlGetMembershipUseCaseNew(gqlRepository: GraphqlRepository): GraphqlUseCase<MembershipStampProgress> {
        return GraphqlUseCase(gqlRepository)
    }

    @ShopProductScope
    @Provides
    fun provideGraphqlGetShopFeaturedProductUseCaseNew(gqlRepository: GraphqlRepository): GraphqlUseCase<ShopFeaturedProduct.Response> {
        return GraphqlUseCase(gqlRepository)
    }

    @ShopProductScope
    @Provides
    fun provideUserSessionInterface(@ShopPageContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ShopProductScope
    @Provides
    fun provideShopProductSortMapper(): ShopProductSortMapper {
        return ShopProductSortMapper()
    }

    @ShopProductScope
    @Provides
    fun provideAddToCart(
        graphqlRepository: GraphqlRepository,
        addToCartDataMapper: AddToCartDataMapper,
        chosenAddressRequestHelper: ChosenAddressRequestHelper
    ): AddToCartUseCase {
        return AddToCartUseCase(graphqlRepository, addToCartDataMapper, chosenAddressRequestHelper)
    }

    @ShopProductScope
    @Provides
    fun provideUpdateCart(
        graphqlRepository: GraphqlRepository,
        chosenAddressRequestHelper: ChosenAddressRequestHelper
    ): UpdateCartUseCase {
        return UpdateCartUseCase(graphqlRepository, chosenAddressRequestHelper)
    }

    @ShopProductScope
    @Provides
    fun provideDeleteCart(graphqlRepository: GraphqlRepository): DeleteCartUseCase {
        return DeleteCartUseCase(graphqlRepository)
    }

    @ShopProductScope
    @Provides
    fun provideAffiliateUseCase(graphqlRepository: GraphqlRepository): AffiliateEligibilityCheckUseCase {
        return AffiliateEligibilityCheckUseCase(graphqlRepository)
    }
}
