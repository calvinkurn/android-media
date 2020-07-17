package com.tokopedia.oneclickcheckout.preference.list.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.oneclickcheckout.preference.list.view.PreferenceListFragment
import dagger.Component

@PreferenceListScope
@Component(modules = [PreferenceListModule::class, PreferenceListViewModelModule::class], dependencies = [BaseAppComponent::class])
interface PreferenceListComponent {

    fun inject(preferenceListFragment: PreferenceListFragment)
}