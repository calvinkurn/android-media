package com.tokopedia.addongifting.addonbottomsheet.view.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.addongifting.addonbottomsheet.view.AddOnBottomSheet
import dagger.Component

@ActivityScope
@Component(modules = [AddOnViewModelModule::class, AddOnModule::class], dependencies = [BaseAppComponent::class])
interface AddOnComponent {
    fun inject(addOnBottomSheet: AddOnBottomSheet)
}