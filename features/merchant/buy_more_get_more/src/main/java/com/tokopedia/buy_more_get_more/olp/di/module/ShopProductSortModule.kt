package com.tokopedia.buy_more_get_more.di.module

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.buy_more_get_more.di.scope.ShopProductSortScope
import com.tokopedia.buy_more_get_more.sort.mapper.ShopProductSortMapper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import dagger.Module
import dagger.Provides

@Module(includes = [ShopProductSortViewModelModule::class])
class ShopProductSortModule {

    @ShopProductSortScope
    @Provides
    fun provideShopProductSortMapper(): ShopProductSortMapper {
        return ShopProductSortMapper()
    }

    @ShopProductSortScope
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchersProvider

    @ShopProductSortScope
    @Provides
    fun provideGraphqlRepository() = GraphqlInteractor.getInstance().graphqlRepository

}
