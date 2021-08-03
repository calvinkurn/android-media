package com.tokopedia.editshipping.di.customproductlogistic

import com.tokopedia.editshipping.di.shippingeditor.ShippingEditorScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

@Module
class CustomProductLogisticModule {

    @Provides
    @ShippingEditorScope
    fun provideGraphQlRepository(): GraphqlRepository =
        GraphqlInteractor.getInstance().graphqlRepository

}