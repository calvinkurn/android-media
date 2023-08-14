package com.tokopedia.shop.score.penalty.presentation

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailResponse
import com.tokopedia.shop.score.penalty.model.PenaltyFilterNoneUiModel
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.BaseFilterPenaltyPage
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageType
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterDateUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.shop.score.util.observeAwaitValue
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ShopPenaltyViewModelTest : ShopPenaltyViewModelTestFixture() {

    @Test
    fun `when get penalty detail by date filter should return success`() {
        runBlocking {
            val shopScorePenaltyDetail = ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail()
            onGetShopPenaltyDetailUseCase_thenReturn(shopScorePenaltyDetail)

            val dateFilter = Pair("2021-01-04", "2021-04-15")

            penaltyViewModel.shopPenaltyDetailMediator.observe({ lifecycle }) {}

            penaltyViewModel.setDateFilterData(dateFilter)

            verifyGetShopPenaltyDetailUseCaseCaseCalled()
            val actualResult =
                (penaltyViewModel.shopPenaltyDetailData.observeAwaitValue() as Success).data
            assertTrue(penaltyViewModel.shopPenaltyDetailData.observeAwaitValue() is Success)
            assertNotNull(actualResult)
            assertEquals(dateFilter.first, penaltyViewModel.getStartDate())
            assertEquals(dateFilter.second, penaltyViewModel.getEndDate())
        }
    }

    @Test
    fun `when get penalty detail by sort and type filter should return success`() {
        runBlocking {
            val shopScorePenaltyDetail = ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail()
            onGetShopPenaltyDetailUseCase_thenReturn(shopScorePenaltyDetail)

            penaltyViewModel.shopPenaltyDetailMediator.observe({ lifecycle }) {}

            penaltyViewModel.setSortTypeFilterData(Pair(0, listOf(2)))

            verifyGetShopPenaltyDetailUseCaseCaseCalled()
            val actualResult = penaltyViewModel.shopPenaltyDetailData.observeAwaitValue(time = 60)
            assertTrue(actualResult is Success)
            assertNotNull((actualResult as Success).data)
        }
    }

    @Test
    fun `when get penalty detail by type filter should return success`() {
        runBlocking {
            val shopScorePenaltyDetail = ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail()
            onGetShopPenaltyDetailUseCase_thenReturn(shopScorePenaltyDetail)

            val typeFilter = listOf(4)

            penaltyViewModel.shopPenaltyDetailMediator.observe({ lifecycle }) {}

            penaltyViewModel.setTypeFilterData(typeFilter)

            verifyGetShopPenaltyDetailUseCaseCaseCalled()
            val actualResult =
                (penaltyViewModel.shopPenaltyDetailData.observeAwaitValue() as Success).data
            assertTrue(penaltyViewModel.shopPenaltyDetailData.observeAwaitValue() is Success)
            assertNotNull(actualResult)
        }
    }

    @Test
    fun `when get penalty detail by type filter should return fail`() {
        runBlocking {
            val messageErrorException = MessageErrorException("Internal Server Error")
            onGetShopPenaltyDetailUseCaseError_thenReturn(messageErrorException)

            val typeFilter = listOf(4)

            penaltyViewModel.shopPenaltyDetailMediator.observe({ lifecycle }) {}

            penaltyViewModel.setTypeFilterData(typeFilter)

            verifyGetShopPenaltyDetailUseCaseCaseCalled()
            val actualResult =
                (penaltyViewModel.shopPenaltyDetailData.observeAwaitValue() as Fail).throwable::class
            val expectedResult = messageErrorException::class
            assertTrue(penaltyViewModel.shopPenaltyDetailData.observeAwaitValue() is Fail)
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when getDataPenalty should return success`() {
        runBlocking {
            onGetShopPenaltyDetailMergeUseCase_thenReturn()

            onGetNotYetDeductedPenaltyUseCase_thenReturn()

            onGetShopPenaltyTickerUseCase_thenReturn(ShopPenaltyPageType.ONGOING)

            penaltyViewModel.getDataPenalty()

            verifyGetShopPenaltyDetailMergeUseCaseCalled()
            val actualResult =
                (penaltyViewModel.penaltyPageData.observeAwaitValue() as? Success)?.data
            assertTrue(penaltyViewModel.penaltyPageData.observeAwaitValue() is Success)
            assertNotNull(actualResult)
        }
    }

    @Test
    fun `when getDataPenalty should return Fail`() {
        runBlocking {
            val exception = MessageErrorException()
            onGetShopPenaltyDetailMergeUseCaseError_thenReturn(exception)

            penaltyViewModel.getDataPenalty()

            verifyGetShopPenaltyDetailMergeUseCaseCalled()
            val actualResult =
                (penaltyViewModel.penaltyPageData.observeAwaitValue() as Fail).throwable::class
            val expectedResult = exception::class
            assertTrue(penaltyViewModel.penaltyPageData.observeAwaitValue() is Fail)
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when get history data penalty should return Success`() {
        runBlocking {
            onGetShopPenaltyDetailMergeUseCase_thenReturn()

            onGetNotYetDeductedPenaltyUseCase_thenReturn()

            onGetShopPenaltyTickerUseCase_thenReturn(ShopPenaltyPageType.HISTORY)

            penaltyViewModel.getDataPenalty(ShopPenaltyPageType.HISTORY)

            verifyGetShopPenaltyDetailMergeUseCaseCalled()
            val actualResult =
                (penaltyViewModel.penaltyPageData.observeAwaitValue() as? Success)?.data
            assertTrue(penaltyViewModel.penaltyPageData.observeAwaitValue() is Success)
            assertNotNull(actualResult)
        }
    }

    @Test
    fun `when get not yet deducted data penalty should return Success`() {
        runBlocking {
            onGetShopPenaltyDetailMergeUseCase_thenReturn()

            onGetNotYetDeductedPenaltyUseCase_thenReturn()

            onGetShopPenaltyTickerUseCase_thenReturn(ShopPenaltyPageType.NOT_YET_DEDUCTED)

            penaltyViewModel.getDataPenalty(ShopPenaltyPageType.NOT_YET_DEDUCTED)

            verifyGetShopPenaltyDetailMergeUseCaseCalled()
            val actualResult =
                (penaltyViewModel.penaltyPageData.observeAwaitValue() as? Success)?.data
            assertTrue(penaltyViewModel.penaltyPageData.observeAwaitValue() is Success)
            assertNotNull(actualResult)
        }
    }

    @Test
    fun `when getFilterPenalty should return Success`() {
        runBlocking {
            val sortBy = 1
            val startDate = "20-04-2023"
            val endDate = "20-06-2023"
            val penaltyFilterList = mutableListOf<BaseFilterPenaltyPage>().apply {
                add(
                    PenaltyFilterUiModel(
                        title = ShopScoreConstant.TITLE_SORT,
                        isDividerVisible = true,
                        chipsFilterList = penaltyMapper.mapToChipsSortFilter(sortBy)
                    )
                )
                add(
                    PenaltyFilterDateUiModel(
                        startDate = startDate,
                        endDate = endDate,
                        defaultStartDate = "20-03-2023",
                        defaultEndDate = "20-07-2023",
                        initialStartDate = startDate,
                        initialEndDate = endDate,
                        completeDate = "20 April 2023 - 20 Juni 2023"
                    )
                )
                add(
                    PenaltyFilterUiModel(
                        title = ShopScoreConstant.TITLE_TYPE_PENALTY,
                        chipsFilterList = mapToChipsTypePenaltyFilterDummy()
                    )
                )
            }

            penaltyViewModel.getFilterPenalty(penaltyFilterList)
            val actualResult = penaltyViewModel.filterPenaltyData.observeAwaitValue()
            val initialStartDate = penaltyViewModel.getInitialStartDate()
            val initialEndDate = penaltyViewModel.getInitialEndDate()
            assertTrue(actualResult is Success)
            assertNotNull((actualResult as Success).data)
            assertEquals(penaltyFilterList, actualResult.data)
            assertEquals(startDate, initialStartDate)
            assertEquals(endDate, initialEndDate)
        }
    }

    @Test
    fun `when getFilterPenalty without date ui model should return Success`() {
        runBlocking {
            val sortBy = 1
            val penaltyFilterList = mutableListOf<BaseFilterPenaltyPage>().apply {
                add(
                    PenaltyFilterUiModel(
                        title = ShopScoreConstant.TITLE_SORT,
                        isDividerVisible = true,
                        chipsFilterList = penaltyMapper.mapToChipsSortFilter(sortBy)
                    )
                )
                add(
                    PenaltyFilterUiModel(
                        title = ShopScoreConstant.TITLE_TYPE_PENALTY,
                        chipsFilterList = mapToChipsTypePenaltyFilterDummy()
                    )
                )
            }

            penaltyViewModel.getFilterPenalty(penaltyFilterList)
            val actualResult = penaltyViewModel.filterPenaltyData.observeAwaitValue()
            assertTrue(actualResult is Success)
            assertNotNull((actualResult as Success).data)
            assertEquals(penaltyFilterList, actualResult.data)
        }
    }

    @Test
    fun `when updateSortFilterSelected with chip type selected should return Success`() {
        runBlocking {
            val titleFilter = "Bersalah di Pusat Resolusi"
            val chipType = ChipsUnify.TYPE_SELECTED

            val sortBy = 1

            val penaltyFilterList = mutableListOf<PenaltyFilterUiModel>().apply {
                add(
                    PenaltyFilterUiModel(
                        title = ShopScoreConstant.TITLE_SORT,
                        isDividerVisible = true,
                        chipsFilterList = penaltyMapper.mapToChipsSortFilter(sortBy)
                    )
                )
                add(
                    PenaltyFilterUiModel(
                        title = ShopScoreConstant.TITLE_TYPE_PENALTY,
                        chipsFilterList = mapToChipsTypePenaltyFilterDummy()
                    )
                )
            }

            val sortFilterItemWrapperList = penaltyMapper.mapToSortFilterItemFromPenaltyList(penaltyFilterList)

            penaltyViewModel.setItemSortFilterWrapperList(
                penaltyFilterList,
                sortFilterItemWrapperList
            )

            penaltyViewModel.updateSortFilterSelected(titleFilter, chipType)
            val actualResult = penaltyViewModel.updateSortSelectedPeriod.observeAwaitValue()
            assertTrue(actualResult is Success)
            val actualResultData = (actualResult as Success).data
            assertNotNull(actualResultData)
        }
    }

    @Test
    fun `when updateSortFilterSelected wiith chip type normal should return Success`() {
        runBlocking {
            val titleFilter = "Bersalah di Pusat Resolusi"
            val chipType = ChipsUnify.TYPE_NORMAL

            val sortBy = 1

            val penaltyFilterList = mutableListOf<PenaltyFilterUiModel>().apply {
                add(
                    PenaltyFilterUiModel(
                        title = ShopScoreConstant.TITLE_SORT,
                        isDividerVisible = true,
                        chipsFilterList = penaltyMapper.mapToChipsSortFilter(sortBy)
                    )
                )
                add(
                    PenaltyFilterUiModel(
                        title = ShopScoreConstant.TITLE_TYPE_PENALTY,
                        chipsFilterList = mapToChipsTypePenaltyFilterDummy()
                    )
                )
            }

            val sortFilterItemWrapperList = penaltyMapper.mapToSortFilterItemFromPenaltyList(penaltyFilterList)

            penaltyViewModel.setItemSortFilterWrapperList(
                penaltyFilterList,
                sortFilterItemWrapperList
            )

            penaltyViewModel.updateSortFilterSelected(titleFilter, chipType)
            val actualResult = penaltyViewModel.updateSortSelectedPeriod.observeAwaitValue()
            assertTrue(actualResult is Success)
            val actualResultData = (actualResult as Success).data
            assertNotNull(actualResultData)
        }
    }

    @Test
    fun `when updateFilterSelected with chip type selected should return Success`() {
        runBlocking {
            val titleFilter = ShopScoreConstant.TITLE_SORT
            val chipType = ChipsUnify.TYPE_SELECTED
            val position = 1
            val sortBy = 1

            val penaltyFilterList = mutableListOf<PenaltyFilterUiModel>().apply {
                add(
                    PenaltyFilterUiModel(
                        title = ShopScoreConstant.TITLE_SORT, isDividerVisible = true,
                        chipsFilterList = penaltyMapper.mapToChipsSortFilter(sortBy)
                    )
                )
                add(
                    PenaltyFilterUiModel(
                        title = ShopScoreConstant.TITLE_TYPE_PENALTY,
                        chipsFilterList = mapToChipsTypePenaltyFilterDummy()
                    )
                )
            }

            val sortFilterItemWrapperList = penaltyMapper.mapToSortFilterItemFromPenaltyList(penaltyFilterList)

            penaltyViewModel.setItemSortFilterWrapperList(
                penaltyFilterList,
                sortFilterItemWrapperList
            )

            penaltyViewModel.updateFilterSelected(titleFilter, chipType, position, true)
            val actualResult = penaltyViewModel.updateFilterSelected.observeAwaitValue()
            assertTrue(actualResult is Success)
            val actualResultData = (actualResult as Success).data
            assertEquals(titleFilter, actualResultData.second)
        }
    }

    @Test
    fun `when updateFilterSelected with chip type normal should return Success`() {
        runBlocking {
            val titleFilter = ShopScoreConstant.TITLE_SORT
            val chipType = ChipsUnify.TYPE_NORMAL
            val position = 1
            val sortBy = 1

            val penaltyFilterList = mutableListOf<PenaltyFilterUiModel>().apply {
                add(
                    PenaltyFilterUiModel(
                        title = ShopScoreConstant.TITLE_SORT, isDividerVisible = true,
                        chipsFilterList = penaltyMapper.mapToChipsSortFilter(sortBy)
                    )
                )
                add(
                    PenaltyFilterUiModel(
                        title = ShopScoreConstant.TITLE_TYPE_PENALTY,
                        chipsFilterList = mapToChipsTypePenaltyFilterDummy()
                    )
                )
            }

            val sortFilterItemWrapperList = penaltyMapper.mapToSortFilterItemFromPenaltyList(penaltyFilterList)

            penaltyViewModel.setItemSortFilterWrapperList(
                penaltyFilterList,
                sortFilterItemWrapperList
            )

            penaltyViewModel.updateFilterSelected(titleFilter, chipType, position, true)
            val actualResult = penaltyViewModel.updateFilterSelected.observeAwaitValue()
            assertTrue(actualResult is Success)
            val actualResultData = (actualResult as Success).data
            assertEquals(titleFilter, actualResultData.second)
        }
    }

    @Test
    fun `when resetFilterSelected should return Success`() {
        runBlocking {
            onGetShopPenaltyDetailMergeUseCase_thenReturn(
                ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail(
                    startDate = "2023-04-20",
                    endDate = "2023-07-20"
                )
            )

            onGetNotYetDeductedPenaltyUseCase_thenReturn()

            onGetShopPenaltyTickerUseCase_thenReturn(ShopPenaltyPageType.ONGOING)

            penaltyViewModel.getDataPenalty()

            val sortBy = 1

            val penaltyFilterList = mutableListOf<BaseFilterPenaltyPage>().apply {
                add(
                    PenaltyFilterUiModel(
                        title = ShopScoreConstant.TITLE_SORT, isDividerVisible = true,
                        chipsFilterList = penaltyMapper.mapToChipsSortFilter(sortBy),
                        shownFilterList = penaltyMapper.mapToChipsSortFilter(sortBy)
                    )
                )
                add(
                    PenaltyFilterDateUiModel(
                        startDate = "2023-04-20",
                        endDate = "2023-07-20",
                        defaultStartDate = "2023-04-20",
                        defaultEndDate = "2023-07-20",
                        initialStartDate = "2023-04-20",
                        initialEndDate = "2023-07-20",
                        completeDate = "20 April 2023 - 20 Juni 2023"
                    )
                )
                add(
                    PenaltyFilterUiModel(
                        title = ShopScoreConstant.TITLE_TYPE_PENALTY,
                        chipsFilterList = mapToChipsTypePenaltyFilterDummy(),
                        shownFilterList = mapToChipsTypePenaltyFilterDummy()
                    )
                )
                add(PenaltyFilterNoneUiModel())
            }

            val sortFilterItemWrapperList = penaltyMapper.mapToSortFilterItemFromPenaltyList(penaltyFilterList)

            penaltyViewModel.setItemSortFilterWrapperList(
                penaltyFilterList,
                sortFilterItemWrapperList
            )

            penaltyViewModel.resetFilterSelected()
            val actualResult = penaltyViewModel.resetFilterResult.observeAwaitValue()
            assertTrue(actualResult is Success)
        }
    }

    @Test
    fun `when resetFilterSelected but date format incorrect should return empty complete date`() {
        runBlocking {
            onGetShopPenaltyDetailMergeUseCase_thenReturn(
                ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail(
                    startDate = "20 Juli 2023",
                    endDate = "20 Agustus 2023"
                )
            )

            onGetNotYetDeductedPenaltyUseCase_thenReturn()

            onGetShopPenaltyTickerUseCase_thenReturn(ShopPenaltyPageType.ONGOING)

            penaltyViewModel.getDataPenalty()

            val sortBy = 1

            val penaltyFilterList = mutableListOf<BaseFilterPenaltyPage>().apply {
                add(
                    PenaltyFilterUiModel(
                        title = ShopScoreConstant.TITLE_SORT, isDividerVisible = true,
                        chipsFilterList = penaltyMapper.mapToChipsSortFilter(sortBy),
                        shownFilterList = penaltyMapper.mapToChipsSortFilter(sortBy)
                    )
                )
                add(
                    PenaltyFilterDateUiModel(
                        startDate = "20-04-2023",
                        endDate = "20-07-2023",
                        defaultStartDate = "20-03-2023",
                        defaultEndDate = "20-07-2023",
                        initialStartDate = "20-04-2023",
                        initialEndDate = "20-07-2023",
                        completeDate = "20 April 2023 - 20 Juni 2023"
                    )
                )
                add(
                    PenaltyFilterUiModel(
                        title = ShopScoreConstant.TITLE_TYPE_PENALTY,
                        chipsFilterList = mapToChipsTypePenaltyFilterDummy(),
                        shownFilterList = mapToChipsTypePenaltyFilterDummy()
                    )
                )
            }

            val sortFilterItemWrapperList = penaltyMapper.mapToSortFilterItemFromPenaltyList(penaltyFilterList)

            penaltyViewModel.setItemSortFilterWrapperList(
                penaltyFilterList,
                sortFilterItemWrapperList
            )

            penaltyViewModel.resetFilterSelected()
            val actualResult = penaltyViewModel.resetFilterResult.observeAwaitValue()
            assertEquals(" - ",
                (actualResult as? Success)?.data?.filterIsInstance<PenaltyFilterDateUiModel>()
                    ?.firstOrNull()?.completeDate
            )
        }
    }

    @Test
    fun `when setItemSortFilterWrapperList should return success`() {
        val sortBy = 1

        val penaltyFilterList = mutableListOf<PenaltyFilterUiModel>().apply {
            add(
                PenaltyFilterUiModel(
                    title = ShopScoreConstant.TITLE_SORT,
                    isDividerVisible = true,
                    chipsFilterList = penaltyMapper.mapToChipsSortFilter(sortBy)
                )
            )
            add(
                PenaltyFilterUiModel(
                    title = ShopScoreConstant.TITLE_TYPE_PENALTY,
                    chipsFilterList = mapToChipsTypePenaltyFilterDummy()
                )
            )
        }

        val sortFilterItemWrapperList =
            penaltyMapper.mapToSortFilterItemFromPenaltyList(penaltyFilterList)

        penaltyViewModel.setItemSortFilterWrapperList(penaltyFilterList, sortFilterItemWrapperList)
        assertEquals(penaltyFilterList, penaltyViewModel.getPenaltyFilterUiModelList())
    }

    @Test
    fun `when setDateFilterData should set start and end date`() {
        val expectedStartDate = "2023-06-06"
        val expectedEndDate = "2023-06-07"

        penaltyViewModel.setDateFilterData(
            expectedStartDate,
            expectedEndDate,
            "$expectedStartDate - $expectedEndDate"
        )

        assertEquals(expectedStartDate, penaltyViewModel.getStartDate())
        assertEquals(expectedEndDate, penaltyViewModel.getEndDate())
    }

    @Test
    fun `when setMaxDateFilterData should set max start and end date`() {
        val expectedMaxStartDate = "2023-06-06"
        val expectedMaxEndDate = "2023-06-07"

        penaltyViewModel.setMaxDateFilterData(expectedMaxStartDate to expectedMaxEndDate)

        assertEquals(expectedMaxStartDate, penaltyViewModel.getMaxStartDate())
        assertEquals(expectedMaxEndDate, penaltyViewModel.getMaxEndDate())
    }

    @Test
    fun `when updateSortFilterSelected should success`() {
        penaltyViewModel.updateSortFilterSelected(listOf())

        assert(penaltyViewModel.updateSortSelectedPeriod.value is Success)
    }

    @Test
    fun `when getCurrentPageType with setting pageType should success`() {
        onGetShopPenaltyDetailMergeUseCase_thenReturn()

        onGetNotYetDeductedPenaltyUseCase_thenReturn()

        onGetShopPenaltyTickerUseCase_thenReturn(ShopPenaltyPageType.ONGOING)
        val shopScorePenaltyDetail = ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail()
        onGetShopPenaltyDetailUseCase_thenReturn(shopScorePenaltyDetail)

        penaltyViewModel.getDataPenalty()
        penaltyViewModel.getPenaltyDetailListNext()

        verifyGetShopPenaltyDetailUseCaseCaseCalled()
        val actualResult =
            (penaltyViewModel.shopPenaltyDetailData.observeAwaitValue() as Success).data
        assertTrue(penaltyViewModel.shopPenaltyDetailData.observeAwaitValue() is Success)
        assertNotNull(actualResult)
    }

    @Test
    fun `when getCurrentPageType without setting pageType should success`() {
        val shopScorePenaltyDetail = ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail()
        onGetShopPenaltyDetailUseCase_thenReturn(shopScorePenaltyDetail)

        penaltyViewModel.getPenaltyDetailListNext()

        verifyGetShopPenaltyDetailUseCaseCaseCalled()
        val actualResult =
            (penaltyViewModel.shopPenaltyDetailData.observeAwaitValue() as Success).data
        assertTrue(penaltyViewModel.shopPenaltyDetailData.observeAwaitValue() is Success)
        assertNotNull(actualResult)
    }

    @Test
    fun `when setDateFilterData should update date filter result value to true`() {
        penaltyViewModel.setDateFilterData(String.EMPTY, String.EMPTY, String.EMPTY)

        assertEquals(true, penaltyViewModel.dateFilterResult.value)
    }

}
