package com.tokopedia.search.result.presentation.presenter.profile

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.InstantTaskExecutorRuleSpek
import com.tokopedia.search.result.domain.model.SearchProfileModel
import com.tokopedia.search.result.presentation.ProfileListSectionContract
import com.tokopedia.search.result.presentation.model.EmptySearchProfileViewModel
import com.tokopedia.search.result.presentation.model.ProfileRecommendationTitleViewModel
import com.tokopedia.search.result.presentation.model.ProfileViewModel
import com.tokopedia.search.result.presentation.presenter.profile.testinstance.searchProfileModelEmptyResult
import com.tokopedia.search.result.presentation.presenter.profile.testinstance.searchProfileModelEmptyResultWithRecommendation
import com.tokopedia.search.result.shop.presentation.viewmodel.shouldBeInstanceOf
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.UseCase
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.slot
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber

class RequestProfileListDataTest: Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Request Profile List Data Empty Result") {
        createTestInstance()

        Scenario("No Result Empty Profile") {
            val query = "dariand"
            val profileListView by memoized<ProfileListSectionContract.View>()
            val searchProfileUseCase by memoized<UseCase<SearchProfileModel>>()
            val slotVisitableList = slot<List<Visitable<*>>>()

            lateinit var profileListPresenter: ProfileListPresenter

            Given("Profile list presenter") {
                profileListPresenter = createProfileListPresenter()
            }

            Given("Search Profile API will return empty result") {
                every { searchProfileUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProfileModel>>().onNext(searchProfileModelEmptyResult)
                }
            }

            When("Request Profile List Data") {
                profileListPresenter.requestProfileListData(query, 1)
            }

            Then("Total profile count should be ${searchProfileModelEmptyResult.aceSearchProfile.count}") {
                profileListPresenter.getTotalProfileCount() shouldBe searchProfileModelEmptyResult.aceSearchProfile.count
            }

            Then("Total has next page should be ${searchProfileModelEmptyResult.aceSearchProfile.hasNext}") {
                profileListPresenter.getHasNextPage() shouldBe searchProfileModelEmptyResult.aceSearchProfile.hasNext
            }

            Then("Verify view interaction for empty search") {
                verifyOrder {
                    profileListView.trackEmptySearchProfile()
                    profileListView.hideLoading()
                    profileListView.clearVisitableList()
                    profileListView.renderVisitableList(capture(slotVisitableList))
                }

                confirmVerified(profileListView)
            }

            Then("Visitable List should contain empty search model") {
                val visitableList = slotVisitableList.captured

                visitableList.shouldOnlyHaveEmptyResult()
            }
        }

        Scenario("No Result Empty Profile with Recommendation") {
            val query = "dariand"
            val profileListView by memoized<ProfileListSectionContract.View>()
            val searchProfileUseCase by memoized<UseCase<SearchProfileModel>>()
            val slotVisitableList = slot<List<Visitable<*>>>()
            val slotRecommendationProfileTrackingObjectList = slot<List<Any>>()

            lateinit var profileListPresenter: ProfileListPresenter

            Given("Profile list presenter") {
                profileListPresenter = createProfileListPresenter()
            }

            Given("Search Profile API will return empty result with recommendation") {
                every { searchProfileUseCase.execute(any(), any()) }.answers {
                    secondArg<Subscriber<SearchProfileModel>>().onNext(searchProfileModelEmptyResultWithRecommendation)
                }
            }

            When("Request Profile List Data") {
                profileListPresenter.requestProfileListData(query, 1)
            }

            Then("Total profile count should be ${searchProfileModelEmptyResultWithRecommendation.aceSearchProfile.count}") {
                profileListPresenter.getTotalProfileCount() shouldBe searchProfileModelEmptyResultWithRecommendation.aceSearchProfile.count
            }

            Then("Total has next page should be ${searchProfileModelEmptyResult.aceSearchProfile.hasNext}") {
                profileListPresenter.getHasNextPage() shouldBe searchProfileModelEmptyResult.aceSearchProfile.hasNext
            }

            Then("Verify view interaction for empty search") {
                verifyOrder {
                    profileListView.trackEmptySearchProfile()
                    profileListView.trackImpressionRecommendationProfile(capture(slotRecommendationProfileTrackingObjectList))
                    profileListView.hideLoading()
                    profileListView.clearVisitableList()
                    profileListView.renderVisitableList(capture(slotVisitableList))
                }

                confirmVerified(profileListView)
            }

            Then("Visitable List should contain empty search model and recommendation profiles") {
                val visitableList = slotVisitableList.captured

                visitableList.shouldHaveEmptyResultAndRecommendationProfiles()
            }

            Then("Recommendation profile tracking object list size should be equal to recommendation profile size") {
                val recommendationProfileTrackingObjectList = slotRecommendationProfileTrackingObjectList.captured

                recommendationProfileTrackingObjectList.size shouldBe
                        searchProfileModelEmptyResultWithRecommendation.aceSearchProfile.topProfile.size
            }
        }
    }
})