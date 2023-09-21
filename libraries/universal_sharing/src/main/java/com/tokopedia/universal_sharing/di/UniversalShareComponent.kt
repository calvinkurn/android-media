package com.tokopedia.universal_sharing.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalSharingPostPurchaseBottomSheet
import dagger.Component

@ActivityScope
@Component(
    modules = [
        UniversalShareModule::class,
        UniversalShareUseCaseModule::class,
        UniversalSharingViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface UniversalShareComponent {
    fun inject(universalShareBottomSheet: UniversalShareBottomSheet)
    fun inject(universalSharingPostPurchaseBottomSheet: UniversalSharingPostPurchaseBottomSheet)
}
