package com.tokopedia.shareexperience.data.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.shareexperience.data.usecase.ShareExGetAffiliateEligibilityUseCaseImpl
import com.tokopedia.shareexperience.data.usecase.ShareExGetGeneratedImageUseCaseImpl
import com.tokopedia.shareexperience.data.usecase.ShareExGetSharePropertiesUseCaseImpl
import com.tokopedia.shareexperience.data.usecase.ShareExGetShortLinkUseCaseImpl
import com.tokopedia.shareexperience.domain.usecase.ShareExGetAffiliateEligibilityUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetGeneratedImageUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetShortLinkUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class ShareExUseCaseModule {
    @Binds
    @ActivityScope
    abstract fun provideGetSharePropertiesUseCase(
        useCase: ShareExGetSharePropertiesUseCaseImpl
    ): ShareExGetSharePropertiesUseCase

    @Binds
    @ActivityScope
    abstract fun provideGetAffiliateEligibilityUseCase(
        useCase: ShareExGetAffiliateEligibilityUseCaseImpl
    ): ShareExGetAffiliateEligibilityUseCase

    @Binds
    @ActivityScope
    abstract fun provideGetGeneratedImageUseCase(
        useCase: ShareExGetGeneratedImageUseCaseImpl
    ): ShareExGetGeneratedImageUseCase

    @Binds
    @ActivityScope
    abstract fun provideGetShortLinkUseCase(
        useCase: ShareExGetShortLinkUseCaseImpl
    ): ShareExGetShortLinkUseCase
}
