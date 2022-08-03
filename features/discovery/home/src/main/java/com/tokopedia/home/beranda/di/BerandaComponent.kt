package com.tokopedia.home.beranda.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home.beranda.di.module.HomeModule
import com.tokopedia.home.beranda.di.module.ViewModelModule
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRecommendationFragment
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment
import dagger.Component
import com.tokopedia.usercomponents.explicit.di.ExplicitViewModule

@HomeScope
@Component(
    modules = [
        HomeModule::class,
        ViewModelModule::class,
        ExplicitViewModule::class
    ], dependencies = [
        BaseAppComponent::class
    ]
)
interface BerandaComponent {
    fun inject(homeRevampFragment: HomeRevampFragment?)
    fun inject(homeRecommendationFragment: HomeRecommendationFragment)
}