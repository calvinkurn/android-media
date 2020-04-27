package com.tokopedia.search.result.presentation.presenter.profile

import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.search.result.domain.model.SearchProfileModel
import com.tokopedia.search.result.presentation.ProfileListSectionContract
import com.tokopedia.search.result.presentation.mapper.ProfileListViewModelMapperModule
import com.tokopedia.usecase.UseCase
import io.mockk.mockk
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.gherkin.FeatureBody

@Suppress("UNUSED_VARIABLE")
internal fun FeatureBody.createTestInstance() {
    val profileListView by memoized {
        mockk<ProfileListSectionContract.View>(relaxed = true)
    }

    val searchProfileUseCase by memoized {
        mockk<UseCase<SearchProfileModel>>(relaxed = true)
    }

    val followKolPostUseCase by memoized {
        mockk<FollowKolPostGqlUseCase>(relaxed = true)
    }
}

internal fun TestBody.createProfileListPresenter(): ProfileListPresenter {
    val profileListView by memoized<ProfileListSectionContract.View>()
    val searchProfileUseCase by memoized<UseCase<SearchProfileModel>>()
    val followKolPostUseCase by memoized<FollowKolPostGqlUseCase>()

    return ProfileListPresenter().also {
        it.attachView(profileListView)

        it.searchProfileListUseCase = searchProfileUseCase
        it.followKolPostGqlUseCase = followKolPostUseCase
        it.profileListViewModelMapper = ProfileListViewModelMapperModule().provideProfileListViewModelMapper()
    }
}