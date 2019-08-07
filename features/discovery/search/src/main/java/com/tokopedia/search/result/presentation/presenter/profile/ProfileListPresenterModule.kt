package com.tokopedia.search.result.presentation.presenter.profile

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope
import com.tokopedia.search.result.presentation.ProfileListSectionContract
import dagger.Module
import dagger.Provides

@SearchScope
@Module
class ProfileListPresenterModule {

    @SearchScope
    @Provides
    fun provideProfileListSectionPresenter() : ProfileListSectionContract.Presenter {
        return ProfileListPresenter()
    }
}