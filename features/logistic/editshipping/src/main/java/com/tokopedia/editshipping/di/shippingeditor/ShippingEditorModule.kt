package com.tokopedia.editshipping.di.shippingeditor

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

@Module
class ShippingEditorModule {
    @Provides
    @ShippingEditorScope
    fun provideGraphQlRepository(): GraphqlRepository =
            GraphqlInteractor.getInstance().graphqlRepository


}