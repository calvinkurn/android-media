package com.tokopedia.shareexperience.data.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.shareexperience.data.usecase.ShareExGetAffiliateEligibilityUseCaseImpl
import com.tokopedia.shareexperience.data.usecase.ShareExGetGeneratedImageUseCaseImpl
import com.tokopedia.shareexperience.data.usecase.ShareExGetSharePropertiesUseCaseImpl
import com.tokopedia.shareexperience.data.usecase.shortlink.ShareExBranchLinkUseCaseImpl
import com.tokopedia.shareexperience.data.usecase.shortlink.ShareExGenerateShortLinkUseCaseImpl
import com.tokopedia.shareexperience.data.usecase.shortlink.ShareExGetAffiliateLinkUseCaseImpl
import com.tokopedia.shareexperience.domain.usecase.ShareExGetAffiliateEligibilityUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetGeneratedImageUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGenerateShortLinkUseCase
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetAffiliateLinkUseCase
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetBranchLinkUseCase
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
        useCase: ShareExBranchLinkUseCaseImpl
    ): ShareExGetBranchLinkUseCase

    @Binds
    @ActivityScope
    abstract fun provideGetAffiliateLinkUseCase(
        useCase: ShareExGetAffiliateLinkUseCaseImpl
    ): ShareExGetAffiliateLinkUseCase

    @Binds
    @ActivityScope
    abstract fun provideGetShortLinkUseCase(
        useCase: ShareExGenerateShortLinkUseCaseImpl
    ): ShareExGenerateShortLinkUseCase
}
