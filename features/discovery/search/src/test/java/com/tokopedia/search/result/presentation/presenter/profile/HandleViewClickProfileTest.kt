package com.tokopedia.search.result.presentation.presenter.profile

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.search.InstantTaskExecutorRuleSpek
import com.tokopedia.search.result.presentation.ProfileListSectionContract
import com.tokopedia.search.result.presentation.model.ProfileViewModel
import com.tokopedia.search.result.presentation.presenter.profile.ProfileListPresenter.Companion.PARAM_USER_ID
import io.mockk.confirmVerified
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class HandleViewClickProfileTest: Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Handle View Click Profile") {
        createTestInstance()

        Scenario("Click Profile") {
            val profileListView by memoized<ProfileListSectionContract.View>()

            val profileViewModel = ProfileViewModel(
                    id = "58377741",
                    name = "Duma Riris"
            ).also {
                it.position = 1
            }

            lateinit var profileListPresenter: ProfileListPresenter

            Given("Profile List Presenter") {
                profileListPresenter = createProfileListPresenter()
            }

            When("Click Profile") {
                profileListPresenter.onViewClickProfile(profileViewModel)
            }

            Then("Verify view interaction for click profile") {
                verifyOrder {
                    profileListView.trackClickProfile(profileViewModel)
                    profileListView.route(ApplinkConst.PROFILE.replace(PARAM_USER_ID, profileViewModel.id))
                }

                confirmVerified(profileListView)
            }
        }

        Scenario("Click Recommendation Profile") {
            val profileListView by memoized<ProfileListSectionContract.View>()

            val profileViewModel = ProfileViewModel(
                    id = "58377741",
                    name = "Duma Riris",
                    isRecommendation = true
            ).also {
                it.position = 1
            }

            lateinit var profileListPresenter: ProfileListPresenter

            Given("Profile List Presenter") {
                profileListPresenter = createProfileListPresenter()
            }

            When("Click Profile") {
                profileListPresenter.onViewClickProfile(profileViewModel)
            }

            Then("Verify view interaction for click recommendation profile") {
                verifyOrder {
                    profileListView.trackClickRecommendationProfile(profileViewModel)
                    profileListView.route(ApplinkConst.PROFILE.replace(PARAM_USER_ID, profileViewModel.id))
                }

                confirmVerified(profileListView)
            }
        }
    }
})