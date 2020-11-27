package com.tokopedia.atc_variant.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.domain.GraphqlUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@NormalCheckoutScope
@Module(includes = [NormalCheckoutUserModule::class])
class NormalCheckoutModule {

    @Provides
    @NormalCheckoutScope
    @Named("atcMutation")
    fun provideAddToCartMutation(@ApplicationContext context: Context):
            String = GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart)

    @Provides
    @NormalCheckoutScope
    @Named("atcOcsMutation")
    fun provideAddToCartOcsMutation(@ApplicationContext context: Context):
            String = GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart_one_click_shipment)

    @NormalCheckoutScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @NormalCheckoutScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatchers = CoroutineDispatchersProvider
}