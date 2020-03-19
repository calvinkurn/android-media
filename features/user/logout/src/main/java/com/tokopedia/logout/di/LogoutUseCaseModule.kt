package com.tokopedia.logout.di

import com.tokopedia.logout.data.LogoutApi
import com.tokopedia.logout.domain.mapper.LogoutMapper
import com.tokopedia.logout.domain.usecase.LogoutUseCase
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides

@LogoutScope
@Module
class LogoutUseCaseModule {

    @Provides
    fun provideLogoutUseCase(api: LogoutApi, logoutMapper: LogoutMapper, userSession: UserSession): LogoutUseCase {
        return LogoutUseCase(api, logoutMapper, userSession)
    }
}