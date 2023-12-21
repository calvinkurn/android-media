package com.tokopedia.shareexperience.data.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.shareexperience.data.usecase.ShareExGetSharePropertiesUseCaseImpl
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class ShareExUseCaseModule {
    @Binds
    @ActivityScope
    abstract fun provideGetSharePropertiesUseCase(
        useCase: ShareExGetSharePropertiesUseCaseImpl
    ): ShareExGetSharePropertiesUseCase
}
