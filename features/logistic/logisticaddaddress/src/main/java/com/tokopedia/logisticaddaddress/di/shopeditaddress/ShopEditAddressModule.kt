package com.tokopedia.logisticaddaddress.di.shopeditaddress

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

@Module
@ShopEditAddressScope
class ShopEditAddressModule {

    @Provides
    @ShopEditAddressScope
    fun provideGraphQlRepository(): GraphqlRepository =
            GraphqlInteractor.getInstance().graphqlRepository
}