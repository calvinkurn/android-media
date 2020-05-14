package com.tokopedia.search.result.presentation.presenter.profile

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.di.module.SearchContextModule
import com.tokopedia.search.result.domain.usecase.searchprofile.SearchProfileUseCaseModule
import com.tokopedia.search.result.presentation.mapper.ProfileListViewModelMapperModule
import dagger.Component

@SearchScope
@Component(modules = [
    SearchContextModule::class,
    SearchProfileUseCaseModule::class,
    ProfileListViewModelMapperModule::class
], dependencies = [
    BaseAppComponent::class
])
interface ProfileListPresenterComponent {

    fun inject(profileListPresenter: ProfileListPresenter)
}