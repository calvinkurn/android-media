package com.tokopedia.addongifting.view.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.addongifting.view.AddOnBottomSheet
import dagger.Component

@ActivityScope
@Component(modules = [AddOnViewModelModule::class, AddOnModule::class], dependencies = [BaseAppComponent::class])
interface AddOnComponent {
    fun inject(addOnBottomSheet: AddOnBottomSheet)
}