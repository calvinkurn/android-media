package com.tokopedia.universal_sharing.stub.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.universal_sharing.di.UniversalShareComponent
import com.tokopedia.universal_sharing.di.UniversalShareUseCaseModule
import com.tokopedia.universal_sharing.di.UniversalSharingViewModelModule
import com.tokopedia.universal_sharing.stub.di.base.UniversalSharingFakeBaseAppComponent
import com.tokopedia.universal_sharing.test.UniversalShareBottomSheetTest
import com.tokopedia.universal_sharing.test.base.UniversalSharingPostPurchaseBottomSheetTest
import dagger.Component

@ActivityScope
@Component(
    modules = [
        // Real modules
        UniversalShareUseCaseModule::class,
        UniversalSharingViewModelModule::class,

        // Stub modules
        UniversalSharingModuleStub::class
    ],
    dependencies = [UniversalSharingFakeBaseAppComponent::class]
)
interface UniversalSharingComponentStub: UniversalShareComponent {
    fun inject(test: UniversalShareBottomSheetTest)
    fun inject(base: UniversalSharingPostPurchaseBottomSheetTest)
}
