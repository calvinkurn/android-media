package com.tokopedia.logout.di.module

import android.content.Context
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logout.di.LogoutContext
import com.tokopedia.logout.di.LogoutScope
import com.tokopedia.logout.domain.model.LogoutDataModel
import com.tokopedia.logout.domain.usecase.LogoutUseCase
import dagger.Module
import dagger.Provides

@Module
class LogoutUseCaseModule {

    @LogoutScope
    @Provides
    fun provideLogoutUseCase(@LogoutContext context: Context, useCase: GraphqlUseCase<LogoutDataModel>): LogoutUseCase {
        return LogoutUseCase(context, useCase)
    }
}