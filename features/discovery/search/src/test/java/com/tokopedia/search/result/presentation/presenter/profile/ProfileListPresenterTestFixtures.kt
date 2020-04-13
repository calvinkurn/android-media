package com.tokopedia.search.result.presentation.presenter.profile

import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.search.result.domain.model.SearchProfileModel
import com.tokopedia.search.result.presentation.ProfileListSectionContract
import com.tokopedia.search.result.presentation.mapper.ProfileListViewModelMapperModule
import com.tokopedia.usecase.UseCase
import io.mockk.mockk
import org.junit.Before

open class ProfileListPresenterTestFixtures {

    protected val profileListView = mockk<ProfileListSectionContract.View>(relaxed = true)
    protected val searchProfileUseCase = mockk<UseCase<SearchProfileModel>>(relaxed = true)
    protected val followKolPostUseCase = mockk<FollowKolPostGqlUseCase>(relaxed = true)
    protected val profileListPresenter = ProfileListPresenter().also {
        it.searchProfileListUseCase = searchProfileUseCase
        it.followKolPostGqlUseCase = followKolPostUseCase
        it.profileListViewModelMapper = ProfileListViewModelMapperModule().provideProfileListViewModelMapper()
    }

    @Before
    open fun setUp() {
        profileListPresenter.attachView(profileListView)
    }
}