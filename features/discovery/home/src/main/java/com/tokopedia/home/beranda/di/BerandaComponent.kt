package com.tokopedia.home.beranda.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home.beranda.di.module.HomeModule
import com.tokopedia.home.beranda.di.module.ShopModule
import com.tokopedia.home.beranda.di.module.ViewModelModule
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedPresenter
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter
import com.tokopedia.home.beranda.presentation.view.fragment.BusinessUnitItemFragment
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFeedFragment
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment
import com.tokopedia.home.beranda.presentation.view.fragment.TabBusinessFragment
import com.tokopedia.home.common.ApiModule
import dagger.Component


@HomeScope
@Component(modules = [ApiModule::class, HomeModule::class, ShopModule::class, ViewModelModule::class], dependencies = [BaseAppComponent::class])
interface BerandaComponent {
    fun inject(homeFragment: HomeFragment?)
    fun inject(homeFeedFragment: HomeFeedFragment?)
    fun inject(homePresenter: HomePresenter?)
    fun inject(homeFeedPresenter: HomeFeedPresenter?)
    fun inject(fragment: TabBusinessFragment?)
    fun inject(fragment: BusinessUnitItemFragment?)
}