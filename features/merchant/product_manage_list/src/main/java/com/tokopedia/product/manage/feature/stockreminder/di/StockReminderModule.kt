package com.tokopedia.product.manage.feature.stockreminder.di

import com.tokopedia.graphql.domain.GraphqlUseCase
import dagger.Module
import dagger.Provides

@Module
class StockReminderModule {

    @StockReminderScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()
}