package com.tokopedia.similarsearch.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.similarsearch.ATC_MUTATION
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal class AddToCartUseCaseModule {

    @Provides
    @SimilarSearchModuleScope
    @Named(ATC_MUTATION)
    fun provideAddToCartMutation(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart)
}