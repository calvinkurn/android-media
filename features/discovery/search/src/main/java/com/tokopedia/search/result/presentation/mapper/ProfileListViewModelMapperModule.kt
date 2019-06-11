package com.tokopedia.search.result.presentation.mapper

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope
import com.tokopedia.search.result.domain.model.SearchProfileModel
import com.tokopedia.search.result.presentation.model.ProfileListViewModel
import dagger.Module
import dagger.Provides

@SearchScope
@Module
class ProfileListViewModelMapperModule {

    @SearchScope
    @Provides
    fun provideProfileListViewModelMapper() : Mapper<SearchProfileModel, ProfileListViewModel> {
        return ProfileListViewModelMapper()
    }
}