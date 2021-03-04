package com.tokopedia.homenav.mainnav.di

import com.tokopedia.homenav.di.BaseNavComponent
import com.tokopedia.homenav.mainnav.view.fragment.MainNavFragment
import dagger.Component

@MainNavScope
@Component(modules = [MainNavModule::class, MainNavViewModelModule::class], dependencies = [BaseNavComponent::class])
interface MainNavComponent {

    fun inject(mainNavFragment: MainNavFragment)
}