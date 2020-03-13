package com.tokopedia.product.manage.feature.etalase.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import dagger.Module
import dagger.Provides

@Module(includes = [ProductManageEtalaseViewModelModule::class])
class ProductManageEtalaseModule {

    @ProductManageEtalaseScope
    @Provides
    fun provideGetShopeEtalaseByShopUseCase(
        @ApplicationContext context: Context
    ): GetShopEtalaseByShopUseCase {
        return GetShopEtalaseByShopUseCase(context)
    }
}