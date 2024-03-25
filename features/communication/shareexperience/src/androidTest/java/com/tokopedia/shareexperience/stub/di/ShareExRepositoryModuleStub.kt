package com.tokopedia.shareexperience.stub.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.shareexperience.domain.repository.ShareExBranchRepository
import com.tokopedia.shareexperience.stub.domain.ShareExBranchRepositoryImplStub
import dagger.Binds
import dagger.Module

@Module
abstract class ShareExRepositoryModuleStub {

    @Binds
    @ActivityScope
    abstract fun provideBranchRepositoryStub(
        repository: ShareExBranchRepositoryImplStub
    ): ShareExBranchRepository
}
