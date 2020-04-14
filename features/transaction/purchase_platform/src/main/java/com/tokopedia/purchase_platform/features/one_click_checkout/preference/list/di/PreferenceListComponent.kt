package com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.view.PreferenceListFragment
import dagger.Component

@PreferenceListScope
@Component(modules = [PreferenceListModule::class, PreferenceListViewModelModule::class], dependencies = [BaseAppComponent::class])
interface PreferenceListComponent {

    fun inject(preferenceListFragment: PreferenceListFragment)
}