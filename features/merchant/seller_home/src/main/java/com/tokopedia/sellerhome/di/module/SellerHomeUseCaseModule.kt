package com.tokopedia.sellerhome.di.module

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerhome.domain.usecase.GetLayoutUseCase
import dagger.Module
import dagger.Provides

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

@SellerHomeScope
@Module
class SellerHomeUseCaseModule {

    @SellerHomeScope
    @Provides
    fun provideGetSellerHomeLayoutUseCase(multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase): GetLayoutUseCase {
        return GetLayoutUseCase(multiRequestGraphqlUseCase)
    }
}