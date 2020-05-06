package com.tokopedia.tkpd.tkpdreputation.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.tkpd.tkpdreputation.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ReputationRawModule {

    @Provides
    @Named("review_form")
    fun provideReviewForm(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_product_rev_form)
    }
}