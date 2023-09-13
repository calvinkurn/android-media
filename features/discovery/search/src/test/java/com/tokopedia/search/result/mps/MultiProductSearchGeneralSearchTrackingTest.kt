package com.tokopedia.search.result.mps

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.search.TestException
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.mps.analytics.GeneralSearchTrackingMPS
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import io.mockk.slot
import io.mockk.verify
import org.junit.Test

internal class MultiProductSearchGeneralSearchTrackingTest : MultiProductSearchTestFixtures() {
    private val userId = "12345"
    private val keyword1 = "samsung"
    private val keyword2 = "xiaomi"
    private val keyword3 = "iphone"

    private var parameter: Map<String, String> = mapOf(
        SearchApiConst.ACTIVE_TAB to SearchConstant.ActiveTab.MPS,
        SearchApiConst.Q1 to keyword1,
        SearchApiConst.Q2 to keyword2,
        SearchApiConst.Q3 to keyword3,
        SearchApiConst.USER_ID to userId,
    )

    private fun `Test MPS General Search Tracking`(
        mpsModel: MPSModel,
        mpsViewModel: MPSViewModel,
        expectedGeneralSearchTracking: GeneralSearchTrackingMPS,
    ) {
        `Given MPS Use Case success`(mpsModel)

        `When view created`(mpsViewModel)

        `Then verify general search tracking called`(expectedGeneralSearchTracking)
    }

    private fun `Then verify general search tracking called`(expectedGeneralSearchTrackingMPS: GeneralSearchTrackingMPS) {
        val generalSearchTrackerSlot = slot<GeneralSearchTrackingMPS>()
        verify {
            mpsTracking.trackGeneralSearch(capture(generalSearchTrackerSlot))
        }
        generalSearchTrackerSlot.captured shouldBe expectedGeneralSearchTrackingMPS
    }

    private fun `When view created`(mpsViewModel: MPSViewModel) {
        mpsViewModel.onViewCreated()
    }

    @Test
    fun `General search tracking with result found`() {
        val mpsModel = MPSSuccessJSON.jsonToObject<MPSModel>()
        val mpsViewModel = mpsViewModel(MPSState(parameter))
        val expectedGeneralSearchTracking = GeneralSearchTrackingMPS(
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                "$keyword1^$keyword2^$keyword3",
                mpsModel.searchShopMPS.header.treatmentCode,
                "0",
                SearchEventTracking.PHYSICAL_GOODS,
                "none",
                "none",
                mpsModel.totalData,
            ),
            pageSource = Dimension90Utils.getDimension90(parameter),
            isResultFound = mpsModel.searchShopMPS.shopList.isNotEmpty().toString(),
            userId = "",
            relatedKeyword = "",
            searchFilter = "",
            externalReference = "none",
            componentId = "",
        )

        `Test MPS General Search Tracking`(mpsModel, mpsViewModel, expectedGeneralSearchTracking)
    }

    @Test
    fun `General search tracking with no result found`() {
        val mpsModel = "mps/mps-emptystate.json".jsonToObject<MPSModel>()
        val mpsViewModel = mpsViewModel(MPSState(parameter))

        val expectedGeneralSearchTracking = GeneralSearchTrackingMPS(
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                "$keyword1^$keyword2^$keyword3",
                mpsModel.searchShopMPS.header.treatmentCode,
                "1",
                SearchEventTracking.PHYSICAL_GOODS,
                "none",
                "none",
                mpsModel.totalData,
            ),
            pageSource = Dimension90Utils.getDimension90(parameter),
            isResultFound = mpsModel.searchShopMPS.shopList.isNotEmpty().toString(),
            userId = "",
            relatedKeyword = "",
            searchFilter = "",
            externalReference = "none",
            componentId = "",
        )

        `Test MPS General Search Tracking`(mpsModel, mpsViewModel, expectedGeneralSearchTracking)
    }

    @Test
    fun `General search tracking with no result found and active filter`() {
        val mpsModel = "mps/mps-emptystate.json".jsonToObject<MPSModel>()

        val activeFilterOption = mpsModel.quickFilterList.first().options.first()
        val activeFilterPair = activeFilterOption.run { key to value }
        val parameterWithFilter = parameter + activeFilterPair
        val mpsState = MPSState(parameterWithFilter)
        val mpsViewModel = mpsViewModel(mpsState)

        val expectedGeneralSearchTracking = GeneralSearchTrackingMPS(
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                "$keyword1^$keyword2^$keyword3",
                mpsModel.searchShopMPS.header.treatmentCode,
                "1",
                SearchEventTracking.PHYSICAL_GOODS,
                "none",
                "none",
                mpsModel.totalData,
            ),
            pageSource = Dimension90Utils.getDimension90(parameter),
            isResultFound = mpsModel.searchShopMPS.shopList.isNotEmpty().toString(),
            userId = "",
            relatedKeyword = "",
            searchFilter = "",
            externalReference = "none",
            componentId = "",
        )

        `Test MPS General Search Tracking`(mpsModel, mpsViewModel, expectedGeneralSearchTracking)
    }

    @Test
    fun `General search tracking should not sent on load more`() {
        val mpsModel = MPSSuccessJSON.jsonToObject<MPSModel>()
        val mpsStateAfterFirstPage = MPSState(parameter).success(mpsModel)
        val mpsModelLoadMore = MPSLoadMoreSuccessJSON.jsonToObject<MPSModel>()
        `Given MPS load more use case success`(mpsModelLoadMore, requestParamsSlot)
        val viewModel = mpsViewModel(mpsStateAfterFirstPage)

        `When load more`(viewModel)

        `Then verify general search tracking not called`()
    }

    private fun `When load more`(mpsViewModel: MPSViewModel) {
        mpsViewModel.onViewLoadMore()
    }

    @Test
    fun `General search tracking should not sent when error occurred`() {
        val exception = TestException("test exception")
        val viewModel = mpsViewModel(MPSState(parameter))
        `Given MPS Use Case failed`(exception)

        `When view created`(viewModel)

        `Then verify general search tracking not called`()
    }

    private fun `Given MPS Use Case failed`(throwable: Throwable) {
        mpsFirstPageUseCase.stubExecute() throws throwable
    }

    private fun `Then verify general search tracking not called`() {
        verify(exactly = 0) {
            mpsTracking.trackGeneralSearch(any())
        }
    }
}
