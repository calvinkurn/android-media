package com.tokopedia.product.manage.feature.filter.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.core.common.category.domain.interactor.GetCategoryListUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.product.manage.feature.filter.domain.GetProductListMetaUseCase
import com.tokopedia.product.manage.feature.filter.domain.GetProductManageFilterOptionsUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import dagger.Module
import dagger.Provides

@Module(includes = [ProductManageFilterViewModelModule::class])
@ProductManageFilterScope
class ProductManageFilterModule {

    @ProductManageFilterScope
    @Provides
    fun provideGetProductListMetaUseCase(multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase) =
            GetProductListMetaUseCase(multiRequestGraphqlUseCase)

    @ProductManageFilterScope
    @Provides
    fun provideGetCategoryListUseCase(multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase) =
            GetCategoryListUseCase(multiRequestGraphqlUseCase)

    @ProductManageFilterScope
    @Provides
    fun provideGetShopeEtalaseByShopUseCase(@ApplicationContext context: Context) =
            GetShopEtalaseByShopUseCase(context)

    @ProductManageFilterScope
    @Provides
    fun provideProductManageFilterCombinedUseCase(getProductListMetaUseCase: GetProductListMetaUseCase,
                                                  getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase,
                                                  getCategoryListUseCase: GetCategoryListUseCase) =
            GetProductManageFilterOptionsUseCase(getProductListMetaUseCase, getShopEtalaseByShopUseCase, getCategoryListUseCase)
}