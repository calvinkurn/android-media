package com.tokopedia.shareexperience.data.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.shareexperience.data.usecase.ShareExGetAffiliateEligibilityUseCaseImpl
import com.tokopedia.shareexperience.data.usecase.ShareExGetDownloadedImageUseCaseImpl
import com.tokopedia.shareexperience.data.usecase.ShareExGetGeneratedImageUseCaseImpl
import com.tokopedia.shareexperience.data.usecase.ShareExGetSharePropertiesUseCaseImpl
import com.tokopedia.shareexperience.data.usecase.shortlink.ShareExGetAffiliateLinkUseCaseImpl
import com.tokopedia.shareexperience.data.usecase.shortlink.ShareExGetBranchLinkUseCaseImpl
import com.tokopedia.shareexperience.data.usecase.shortlink.ShareExGetShortLinkUseCaseImpl
import com.tokopedia.shareexperience.domain.usecase.ShareExGetAffiliateEligibilityUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetDownloadedImageUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetGeneratedImageUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetAffiliateLinkUseCase
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetBranchLinkUseCase
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetShortLinkUseCase
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
    abstract fun provideGetBranchLinkUseCase(
        useCase: ShareExGetBranchLinkUseCaseImpl
    ): ShareExGetBranchLinkUseCase

    @Binds
    @ActivityScope
    abstract fun provideGetAffiliateLinkUseCase(
        useCase: ShareExGetAffiliateLinkUseCaseImpl
    ): ShareExGetAffiliateLinkUseCase

    @Binds
    @ActivityScope
    abstract fun provideGetShortLinkUseCase(
        useCase: ShareExGetShortLinkUseCaseImpl
    ): ShareExGetShortLinkUseCase

    @Binds
    @ActivityScope
    abstract fun provideGetDownloadedImageUseCase(
        useCase: ShareExGetDownloadedImageUseCaseImpl
    ): ShareExGetDownloadedImageUseCase
}
