package com.tokopedia.shareexperience.stub.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.shareexperience.data.di.ShareExUseCaseModule
import com.tokopedia.shareexperience.data.di.ShareExViewModelModule
import com.tokopedia.shareexperience.data.di.component.ShareExComponent
import com.tokopedia.shareexperience.stub.di.base.ShareExFakeBaseAppComponent
import com.tokopedia.shareexperience.test.base.ShareExBaseTest
import dagger.Component

@ActivityScope
@Component(
    modules = [
        // Stub modules
        ShareExModuleStub::class,
        ShareExRepositoryModuleStub::class,

        // Real modules
        ShareExViewModelModule::class,
        ShareExUseCaseModule::class
    ],
    dependencies = [ShareExFakeBaseAppComponent::class]
)
interface ShareExComponentStub : ShareExComponent {
    fun inject(test: ShareExBaseTest)
}
