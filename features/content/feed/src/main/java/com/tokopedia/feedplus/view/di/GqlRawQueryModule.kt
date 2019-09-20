package com.tokopedia.feedplus.view.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.feedplus.R
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@FeedPlusScope
@Module
class GqlRawQueryModule {

    @FeedPlusScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_ONBOARDING_INTEREST)
    fun provideOnboardingQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_onboarding_interest)


    @FeedPlusScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.MUTATION_SUBMIT_INTEREST_ID)
    fun provideMutationSubmitInterestId(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_submit_interest_id)



}