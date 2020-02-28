package com.tokopedia.product.manage.feature.filter.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.core.common.category.domain.interactor.GetCategoryListUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.product.manage.feature.filter.domain.GetProductListMetaUseCase
import com.tokopedia.product.manage.feature.filter.domain.ProductManageFilterCombinedUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
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
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface =
            UserSession(context)

    @ProductManageFilterScope
    @Provides
    fun provideProductManageFilterCombinedUseCase(getProductListMetaUseCase: GetProductListMetaUseCase,
                                                  getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase,
                                                  getCategoryListUseCase: GetCategoryListUseCase) =
            ProductManageFilterCombinedUseCase(getProductListMetaUseCase, getShopEtalaseByShopUseCase, getCategoryListUseCase)
}