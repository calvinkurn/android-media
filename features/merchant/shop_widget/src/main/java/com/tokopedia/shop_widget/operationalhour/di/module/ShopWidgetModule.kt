package com.tokopedia.shop_widget.operationalhour.di.module

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor.Companion.getInstance
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.common.di.ShopCommonModule
import dagger.Module
import dagger.Provides

@Module(includes = [ShopCommonModule::class, ShopWidgetViewModelModule::class])
class ShopWidgetModule {

    @Provides
    fun provideGqlRepository(): GraphqlRepository {
        return getInstance().graphqlRepository
    }

}