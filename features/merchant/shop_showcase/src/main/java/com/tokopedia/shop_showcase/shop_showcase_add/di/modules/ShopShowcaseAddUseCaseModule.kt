package com.tokopedia.shop_showcase.shop_showcase_add.di.modules

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop_showcase.shop_showcase_add.di.scope.ShopShowcaseAddScope
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.AppendShopShowcaseProductUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.CreateShopShowcaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.RemoveShopShowcaseProductUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.UpdateShopShowcaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.mapper.ProductMapper
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.usecase.GetProductListUseCase
import dagger.Module
import dagger.Provides

/**
 * @author by Rafli Syam on 2020-03-10
 */

@Module
class ShopShowcaseAddUseCaseModule {

    @ShopShowcaseAddScope
    @Provides
    fun provideCreateShopShowcaseUseCase(
            gqlRepository: GraphqlRepository
    ): CreateShopShowcaseUseCase {
        return CreateShopShowcaseUseCase(gqlRepository)
    }

    @ShopShowcaseAddScope
    @Provides
    fun provideGetProductListUseCase(
            gqlRepository: GraphqlRepository,
            productMapper: ProductMapper
    ): GetProductListUseCase {
        return GetProductListUseCase(gqlRepository, productMapper)
    }

    @ShopShowcaseAddScope
    @Provides
    fun provideUpdateShopShowcaseUseCase(
            gqlRepository: GraphqlRepository
    ): UpdateShopShowcaseUseCase {
        return UpdateShopShowcaseUseCase(gqlRepository)
    }

    @ShopShowcaseAddScope
    @Provides
    fun provideAppendShopShowcaseProductUseCase(
            gqlRepository: GraphqlRepository
    ): AppendShopShowcaseProductUseCase {
        return AppendShopShowcaseProductUseCase(gqlRepository)
    }

    @ShopShowcaseAddScope
    @Provides
    fun provideRemoveShopShowcaseProductUseCase(
            gqlRepository: GraphqlRepository
    ): RemoveShopShowcaseProductUseCase {
        return RemoveShopShowcaseProductUseCase(gqlRepository)
    }

}