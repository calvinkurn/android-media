package com.tokopedia.universal_sharing.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import dagger.Component

@ActivityScope
@Component(modules = [UniversalShareModule::class], dependencies = [BaseAppComponent::class])
interface UniversalShareComponent {
    fun inject(universalShareBottomSheet: UniversalShareBottomSheet)
}