package com.tokopedia.search.result.presentation.presenter.profile

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.search.result.presentation.model.ProfileViewModel
import io.mockk.confirmVerified
import io.mockk.verifyOrder
import org.junit.Test

class SearchProfileHandleClickProfileTest: ProfileListPresenterTestFixtures() {

    @Test
    fun `Handle Click Profile`() {
        val profileViewModel = ProfileViewModel(
                id = "58377741",
                name = "Duma Riris"
        ).also {
            it.position = 1
        }

        `When Handle Click Profile`(profileViewModel)

        `Then verify view interaction for click profile`(profileViewModel)
    }

    private fun `When Handle Click Profile`(profileViewModel: ProfileViewModel) {
        profileListPresenter.onViewClickProfile(profileViewModel)
    }

    private fun `Then verify view interaction for click profile`(profileViewModel: ProfileViewModel) {
        verifyOrder {
            profileListView.trackClickProfile(profileViewModel)
            profileListView.route(ApplinkConst.PROFILE.replace(ProfileListPresenter.PARAM_USER_ID, profileViewModel.id))
        }

        confirmVerified(profileListView)
    }

    @Test
    fun `Handle Click Recommendation Profile`() {
        val profileViewModel = ProfileViewModel(
                id = "58377741",
                name = "Duma Riris",
                isRecommendation = true
        ).also {
            it.position = 1
        }

        `When Handle Click Profile`(profileViewModel)

        `Then Verify view interaction for click recommendation profile`(profileViewModel)
    }

    private fun `Then Verify view interaction for click recommendation profile`(profileViewModel: ProfileViewModel) {
        verifyOrder {
            profileListView.trackClickRecommendationProfile(profileViewModel)
            profileListView.route(ApplinkConst.PROFILE.replace(ProfileListPresenter.PARAM_USER_ID, profileViewModel.id))
        }

        confirmVerified(profileListView)
    }
}