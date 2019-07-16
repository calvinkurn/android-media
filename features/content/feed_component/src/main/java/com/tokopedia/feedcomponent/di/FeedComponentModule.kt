package com.tokopedia.feedcomponent.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.domain.usecase.GetProfileRelatedUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named


@Module
class FeedComponentModule {
    @Named(GetProfileRelatedUseCase.PROFILE_RELATED_KEY)
    @Provides
    fun provideQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(
            context.resources,
            R.raw.query_profile_related
        )
    }

}