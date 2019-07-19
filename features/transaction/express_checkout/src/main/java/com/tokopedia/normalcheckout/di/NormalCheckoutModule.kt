package com.tokopedia.normalcheckout.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.expresscheckout.R
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.domain.GraphqlUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@NormalCheckoutScope
@Module(includes = [NormalCheckoutUserModule::class])
class NormalCheckoutModule {

    @Provides
    @NormalCheckoutScope
    @Named("atcMutation")
    fun provideAddToCartMutation(@ApplicationContext context: Context):
            String = GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_to_cart)

    @Provides
    @NormalCheckoutScope
    @Named("atcOcsMutation")
    fun provideAddToCartOcsMutation(@ApplicationContext context: Context):
            String = GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_to_cart_one_click_shipment)

    @NormalCheckoutScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @NormalCheckoutScope
    @Provides
    @Named("Main")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}