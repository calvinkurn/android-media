package com.tokopedia.promotionstarget.data.di.modules

import com.tokopedia.promotionstarget.data.gql.GqlUseCaseWrapper
import dagger.Module
import dagger.Provides

@Module
class GqlModule {

    @Provides
    fun provideGqlWrapper(): GqlUseCaseWrapper = GqlUseCaseWrapper()

}