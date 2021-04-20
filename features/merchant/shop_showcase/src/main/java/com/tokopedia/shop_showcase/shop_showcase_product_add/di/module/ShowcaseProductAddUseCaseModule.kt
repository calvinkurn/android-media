package com.tokopedia.shop_showcase.shop_showcase_product_add.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop_showcase.shop_showcase_product_add.di.scope.ShowcaseProductAddScope
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.mapper.ProductMapper
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.usecase.GetProductListUseCase
import dagger.Module
import dagger.Provides

/**
 * @author by Rafli Syam on 2020-03-09
 */

@Module
class ShowcaseProductAddUseCaseModule {

    @ShowcaseProductAddScope
    @Provides
    fun provideGetProductListUseCase(
        gqlRepository: GraphqlRepository,
        productMapper: ProductMapper
    ): GetProductListUseCase {
        return GetProductListUseCase(gqlRepository, productMapper)
    }

}