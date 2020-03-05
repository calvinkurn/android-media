package com.tokopedia.discovery.find.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.discovery.find.di.module.FindNavModule
import com.tokopedia.discovery.find.di.module.FindNavViewModelModule
import com.tokopedia.discovery.find.di.scope.FindNavScope
import com.tokopedia.discovery.find.view.fragment.FindNavFragment
import dagger.Component

@FindNavScope
@Component(modules = [FindNavModule::class,
    FindNavViewModelModule::class],
    dependencies = [BaseAppComponent::class])
interface FindNavComponent {
    fun inject(findNavFragment: FindNavFragment)

}