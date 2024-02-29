package com.tokopedia.shareexperience.data.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.shareexperience.data.repository.ShareExBranchRepositoryImpl
import com.tokopedia.shareexperience.domain.repository.ShareExBranchRepository
import dagger.Binds
import dagger.Module

@Module
abstract class ShareExRepository {

    @Binds
    @ActivityScope
    abstract fun provideBranchRepository(
        repository: ShareExBranchRepositoryImpl
    ): ShareExBranchRepository
}
