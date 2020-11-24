package com.tokopedia.manageaddress.di.shoplocation

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

@Module
@ShopLocationScope
class ShopLocationModule {

    @Provides
    @ShopLocationScope
    fun provideGraphQlRepository(): GraphqlRepository =
            GraphqlInteractor.getInstance().graphqlRepository

}