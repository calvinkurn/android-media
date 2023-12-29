package com.tokopedia.universal_sharing.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.universal_sharing.view.activity.UniversalSharingPostPurchaseSharingActivity
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalSharingPostPurchaseBottomSheet
import com.tokopedia.universal_sharing.view.customview.UniversalShareWidget
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
    fun inject(universalSharingPostPurchaseSharingActivity: UniversalSharingPostPurchaseSharingActivity)
    fun inject(universalShareWidget: UniversalShareWidget)
}
