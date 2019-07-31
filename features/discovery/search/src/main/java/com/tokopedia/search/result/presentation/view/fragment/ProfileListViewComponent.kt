package com.tokopedia.search.result.presentation.view.fragment

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope
import com.tokopedia.search.di.module.UserSessionModule
import com.tokopedia.search.result.presentation.presenter.profile.ProfileListPresenterModule
import dagger.Component

@SearchScope
@Component(modules = [
    ProfileListPresenterModule::class,
    UserSessionModule::class
], dependencies = [
    BaseAppComponent::class
])
interface ProfileListViewComponent {

    fun inject(profileListFragment : ProfileListFragment)
}