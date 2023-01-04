package com.tokopedia.find_native.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.find_native.di.module.FindNavModule
import com.tokopedia.find_native.di.module.FindNavViewModelModule
import com.tokopedia.find_native.di.scope.FindNavScope
import com.tokopedia.find_native.view.fragment.FindNavFragment
import dagger.Component

@FindNavScope
@Component(modules = [FindNavModule::class,
    FindNavViewModelModule::class],
    dependencies = [BaseAppComponent::class])
interface FindNavComponent {
    fun inject(findNavFragment: FindNavFragment)

}