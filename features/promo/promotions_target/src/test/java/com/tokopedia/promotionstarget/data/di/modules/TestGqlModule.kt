package com.tokopedia.promotionstarget.data.di.modules

import com.tokopedia.promotionstarget.data.gql.GqlUseCaseWrapper
import dagger.Module
import dagger.Provides
import io.mockk.mockk

@Module
class TestGqlModule {
    @Provides
    fun provideGqlWrapper(): GqlUseCaseWrapper = mockk()
}