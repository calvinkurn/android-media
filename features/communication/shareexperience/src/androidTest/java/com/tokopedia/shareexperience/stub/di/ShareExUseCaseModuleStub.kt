package com.tokopedia.shareexperience.stub.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.shareexperience.domain.usecase.ShareExGetAffiliateEligibilityUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetDownloadedImageUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetGeneratedImageUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetAffiliateLinkUseCase
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetBranchLinkUseCase
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetShortLinkUseCase
import com.tokopedia.shareexperience.stub.domain.ShareExGetAffiliateEligibilityUseCaseStub
import com.tokopedia.shareexperience.stub.domain.ShareExGetDownloadedImageUseCaseStub
import com.tokopedia.shareexperience.stub.domain.ShareExGetGeneratedImageUseCaseStub
import com.tokopedia.shareexperience.stub.domain.ShareExGetSharePropertiesUseCaseStub
import com.tokopedia.shareexperience.stub.domain.shortlink.ShareExGetAffiliateLinkUseCaseStub
import com.tokopedia.shareexperience.stub.domain.shortlink.ShareExGetBranchLinkUseCaseStub
import com.tokopedia.shareexperience.stub.domain.shortlink.ShareExGetShortLinkUseCaseStub
import dagger.Binds
import dagger.Module

@Module
abstract class ShareExUseCaseModuleStub {

    @Binds
    @ActivityScope
    abstract fun provideGetSharePropertiesUseCase(
        useCase: ShareExGetSharePropertiesUseCaseStub
    ): ShareExGetSharePropertiesUseCase

    @Binds
    @ActivityScope
    abstract fun provideGetAffiliateEligibilityUseCase(
        useCase: ShareExGetAffiliateEligibilityUseCaseStub
    ): ShareExGetAffiliateEligibilityUseCase

    @Binds
    @ActivityScope
    abstract fun provideGetGeneratedImageUseCase(
        useCase: ShareExGetGeneratedImageUseCaseStub
    ): ShareExGetGeneratedImageUseCase

    @Binds
    @ActivityScope
    abstract fun provideGetBranchLinkUseCase(
        useCase: ShareExGetBranchLinkUseCaseStub
    ): ShareExGetBranchLinkUseCase

    @Binds
    @ActivityScope
    abstract fun provideGetAffiliateLinkUseCase(
        useCase: ShareExGetAffiliateLinkUseCaseStub
    ): ShareExGetAffiliateLinkUseCase

    @Binds
    @ActivityScope
    abstract fun provideGetShortLinkUseCase(
        useCase: ShareExGetShortLinkUseCaseStub
    ): ShareExGetShortLinkUseCase

    @Binds
    @ActivityScope
    abstract fun provideGetDownloadedImageUseCase(
        useCase: ShareExGetDownloadedImageUseCaseStub
    ): ShareExGetDownloadedImageUseCase
}
