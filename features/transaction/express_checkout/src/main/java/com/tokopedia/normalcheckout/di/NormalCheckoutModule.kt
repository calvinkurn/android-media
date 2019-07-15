package com.tokopedia.normalcheckout.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
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
    fun providesGson(): Gson = Gson()

    @Provides
    @NormalCheckoutScope
    @Named("atcMutation")
    fun provideAddToCartMutation(@ApplicationContext context: Context):
            String = GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_to_cart)

    @Provides
    @NormalCheckoutScope
    fun provideAddToCartUseCase(@Named("atcMutation") mutation: String, graphqlUseCase: GraphqlUseCase):
            AddToCartUseCase = AddToCartUseCase(mutation, graphqlUseCase)

    @Provides
    @NormalCheckoutScope
    @Named("atcOcsMutation")
    fun provideAddToCartOcsMutation(@ApplicationContext context: Context):
            String = GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_to_cart_one_click_shipment)

    @Provides
    @NormalCheckoutScope
    fun provideAddToCartOcsUseCase(@Named("atcOcsMutation") mutation: String,
                                   graphqlUseCase: GraphqlUseCase,
                                   gson: Gson):
            AddToCartOcsUseCase = AddToCartOcsUseCase(mutation, gson, graphqlUseCase)

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