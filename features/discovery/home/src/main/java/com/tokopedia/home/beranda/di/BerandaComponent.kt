package com.tokopedia.home.beranda.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home.beranda.di.module.HomeModule
import com.tokopedia.home.beranda.di.module.ViewModelModule
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRecommendationFragment
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment
import com.tokopedia.home.common.ApiModule
import dagger.Component


@HomeScope
@Component(modules = [ApiModule::class, HomeModule::class, ViewModelModule::class], dependencies = [BaseAppComponent::class])
interface BerandaComponent {
    fun inject(homeFragment: HomeFragment?)
    fun inject(homeRevampFragment: HomeRevampFragment?)
    fun inject(homeRecommendationFragment: HomeRecommendationFragment)
}