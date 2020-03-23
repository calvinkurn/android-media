package com.tokopedia.product.manage.feature.filter.di

import com.tokopedia.core.common.category.domain.interactor.GetCategoryListUseCase
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
    fun provideProductManageFilterCombinedUseCase(getProductListMetaUseCase: GetProductListMetaUseCase,
                                                  getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase,
                                                  getCategoryListUseCase: GetCategoryListUseCase) =
            GetProductManageFilterOptionsUseCase(getProductListMetaUseCase, getShopEtalaseByShopUseCase, getCategoryListUseCase)
}