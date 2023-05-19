package com.tokopedia.home.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.di.module.ViewModelModule
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment
import dagger.Component

@HomeScope
@Component(
    modules = [
        HomeTestModule::class,
        ViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface HomeTestComponent {
    fun inject(homeRevampFragment: HomeRevampFragment?)
}
