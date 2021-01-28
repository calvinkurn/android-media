package com.tokopedia.productcard.options.di

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.productcard.options.ATC_MUTATION
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal class AddToCartUseCaseModule {

    @Provides
    @ProductCardOptionsScope
    @Named(ATC_MUTATION)
    fun provideAddToCartMutation(context: Context): String =
            GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart)
}