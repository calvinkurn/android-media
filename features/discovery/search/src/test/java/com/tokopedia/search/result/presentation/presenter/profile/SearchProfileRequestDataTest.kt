package com.tokopedia.search.result.presentation.presenter.profile

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.domain.model.SearchProfileModel
import com.tokopedia.search.result.presentation.presenter.profile.testinstance.searchProfileModelEmptyResult
import com.tokopedia.search.result.presentation.presenter.profile.testinstance.searchProfileModelEmptyResultWithRecommendation
import com.tokopedia.search.shouldBe
import io.mockk.*
import org.junit.Test
import rx.Subscriber

class SearchProfileRequestDataTest: ProfileListPresenterTestFixtures() {

    private val query = "dariand"
    private val slotVisitableList = slot<List<Visitable<*>>>()

    @Test
    fun `No Result Empty Profile`() {
        `Given Search Profile API will return SearchProfileModel`(searchProfileModelEmptyResult)

        `When Request Profile List Data`()

        `Then verify total profile count`(searchProfileModelEmptyResult.aceSearchProfile.count)
        `Then verify has next page`(searchProfileModelEmptyResult.aceSearchProfile.hasNext)
        `Then verify view interaction for empty search`()
        `Then verify Visitable List should contain empty search model`()
    }

    private fun `Given Search Profile API will return SearchProfileModel`(searchProfileModel: SearchProfileModel) {
        every { searchProfileUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProfileModel>>().onNext(searchProfileModel)
        }
    }

    private fun `When Request Profile List Data`() {
        profileListPresenter.requestProfileListData(query, 1)
    }

    private fun `Then verify total profile count`(expectedTotalProfileCount: Int) {
        profileListPresenter.getTotalProfileCount() shouldBe expectedTotalProfileCount
    }

    private fun `Then verify has next page`(expectedHasNextPage: Boolean) {
        profileListPresenter.getHasNextPage() shouldBe expectedHasNextPage
    }

    private fun `Then verify view interaction for empty search`() {
        verifyOrder {
            profileListView.trackEmptySearchProfile()
            profileListView.hideLoading()
            profileListView.clearVisitableList()
            profileListView.renderVisitableList(capture(slotVisitableList))
        }

        confirmVerified(profileListView)
    }

    private fun `Then verify Visitable List should contain empty search model`() {
        val visitableList = slotVisitableList.captured

        visitableList.shouldOnlyHaveEmptyResult()
    }

    @Test
    fun `No Result Empty Profile with Recommendation`() {
        val slotRecommendationProfileTrackingObjectList = slot<List<Any>>()

        `Given Search Profile API will return SearchProfileModel`(searchProfileModelEmptyResultWithRecommendation)

        `When Request Profile List Data`()

        `Then verify total profile count`(searchProfileModelEmptyResultWithRecommendation.aceSearchProfile.count)
        `Then verify has next page`(searchProfileModelEmptyResult.aceSearchProfile.hasNext)
        `Then verify view interaction for empty search with recommendation`(slotRecommendationProfileTrackingObjectList)
        `Then Visitable List should contain empty search model and recommendation profiles`()
        `Then Recommendation profile tracking object list size should be equal to recommendation profile size`(slotRecommendationProfileTrackingObjectList)
    }

    private fun `Then verify view interaction for empty search with recommendation`(slotRecommendationProfileTrackingObjectList: CapturingSlot<List<Any>>) {
        verifyOrder {
            profileListView.trackEmptySearchProfile()
            profileListView.trackImpressionRecommendationProfile(capture(slotRecommendationProfileTrackingObjectList))
            profileListView.hideLoading()
            profileListView.clearVisitableList()
            profileListView.renderVisitableList(capture(slotVisitableList))
        }

        confirmVerified(profileListView)
    }

    private fun `Then Visitable List should contain empty search model and recommendation profiles`() {
        val visitableList = slotVisitableList.captured

        visitableList.shouldHaveEmptyResultAndRecommendationProfiles()
    }

    private fun `Then Recommendation profile tracking object list size should be equal to recommendation profile size`(slotRecommendationProfileTrackingObjectList: CapturingSlot<List<Any>>) {
        val recommendationProfileTrackingObjectList = slotRecommendationProfileTrackingObjectList.captured

        recommendationProfileTrackingObjectList.size shouldBe
                searchProfileModelEmptyResultWithRecommendation.aceSearchProfile.topProfile.size
    }
}