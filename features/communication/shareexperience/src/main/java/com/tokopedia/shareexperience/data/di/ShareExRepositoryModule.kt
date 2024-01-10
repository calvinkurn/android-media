package com.tokopedia.shareexperience.data.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.shareexperience.data.repository.ShareExBranchLinkRepositoryImpl
import com.tokopedia.shareexperience.domain.repository.ShareExShortLinkRepository
import dagger.Binds
import dagger.Module

@Module
abstract class ShareExRepositoryModule {
    @Binds
    @ActivityScope
    abstract fun provideShortLinkRepository(
        repository: ShareExBranchLinkRepositoryImpl
    ): ShareExShortLinkRepository
}
