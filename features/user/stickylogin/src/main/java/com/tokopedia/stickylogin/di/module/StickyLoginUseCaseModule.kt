package com.tokopedia.stickylogin.di.module

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.stickylogin.domain.data.StickyLoginTickerDataModel
import com.tokopedia.stickylogin.di.StickyLoginScope
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import dagger.Module
import dagger.Provides

@Module
class StickyLoginUseCaseModule {

    @StickyLoginScope
    @Provides
    fun provideStickyLoginUseCase(useCase: GraphqlUseCase<StickyLoginTickerDataModel.TickerResponse>): StickyLoginUseCase {
        return StickyLoginUseCase(useCase)
    }
}