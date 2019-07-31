package com.tokopedia.search.result.presentation.presenter.profile

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope
import com.tokopedia.search.di.module.FollowKolPostUseCaseModule
import com.tokopedia.search.result.domain.usecase.searchprofile.SearchProfileUseCaseModule
import com.tokopedia.search.result.presentation.mapper.ProfileListViewModelMapperModule
import dagger.Component

@SearchScope
@Component(modules = [
    SearchProfileUseCaseModule::class,
    ProfileListViewModelMapperModule::class,
    FollowKolPostUseCaseModule::class
], dependencies = [
    BaseAppComponent::class
])
interface ProfileListPresenterComponent {

    fun inject(profileListPresenter: ProfileListPresenter)
}