package com.tokopedia.home.beranda.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.home.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class HomeRawModule {
    @Provides
    @Named("suggested_review")
    fun provideReviewForm(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_suggested_product_review)
    }

    @Provides
    @Named("dismiss_raw")
    fun provideDismissReviewRaw(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_dismiss_review)
    }
}