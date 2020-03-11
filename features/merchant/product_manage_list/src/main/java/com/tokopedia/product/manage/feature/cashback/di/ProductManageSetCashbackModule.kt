package com.tokopedia.product.manage.feature.cashback.di


import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.product.manage.feature.cashback.domain.SetCashbackUseCase
import dagger.Module
import dagger.Provides

@Module(includes = [ProductManageSetCashbackViewModelModule::class])
class ProductManageSetCashbackModule {

    @ProductManageSetCashbackScope
    @Provides
    fun provideSetCashbackUseCase(multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase) =
            SetCashbackUseCase(multiRequestGraphqlUseCase)
}