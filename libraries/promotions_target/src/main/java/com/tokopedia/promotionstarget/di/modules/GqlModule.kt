package com.tokopedia.promotionstarget.di.modules

import com.tokopedia.promotionstarget.di.scopes.PromoTargetScope
import com.tokopedia.promotionstarget.gql.GqlUseCaseWrapper
import dagger.Module
import dagger.Provides

@PromoTargetScope
@Module
class GqlModule {

    @Provides
    @PromoTargetScope
    fun provideGqlWrapper(): GqlUseCaseWrapper = GqlUseCaseWrapper()

}