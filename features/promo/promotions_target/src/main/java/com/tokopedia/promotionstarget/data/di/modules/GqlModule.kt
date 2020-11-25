package com.tokopedia.promotionstarget.data.di.modules

import com.tokopedia.promotionstarget.data.di.scopes.PromoTargetScope
import com.tokopedia.promotionstarget.data.gql.GqlUseCaseWrapper
import dagger.Module
import dagger.Provides

@PromoTargetScope
@Module
class GqlModule {

    @Provides
    @PromoTargetScope
    fun provideGqlWrapper(): GqlUseCaseWrapper = GqlUseCaseWrapper()

}