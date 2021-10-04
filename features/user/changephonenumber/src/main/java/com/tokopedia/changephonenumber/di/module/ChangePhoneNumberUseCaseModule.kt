package com.tokopedia.changephonenumber.di.module

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.changephonenumber.domain.usecase.GetWarningUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class ChangePhoneNumberUseCaseModule {

    @ActivityScope
    @Provides
    fun provideGetWarningUseCase(
        repository: RestRepository,
        userSession: UserSessionInterface
    ): GetWarningUseCase {
        return GetWarningUseCase(repository, userSession)
    }
}