package com.tokopedia.shop.score.penalty.presentation

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailResponse
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.shop.score.util.observeAwaitValue
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
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

            penaltyViewModel.setSortTypeFilterData(Pair(0, 2))

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

            val typeFilter = 4

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

            val typeFilter = 4

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

            onGetShopPenaltyDetailMergeUseCase_thenReturn()

            penaltyViewModel.getDataPenalty()

            verifyGetShopPenaltyDetailMergeUseCaseCalled()
            val actualResult =
                (penaltyViewModel.penaltyPageData.observeAwaitValue() as Success).data
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
    fun `when getFilterPenalty should return Success`() {
        runBlocking {
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

            penaltyViewModel.updateFilterSelected(titleFilter, chipType, position)
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

            penaltyViewModel.updateFilterSelected(titleFilter, chipType, position)
            val actualResult = penaltyViewModel.updateFilterSelected.observeAwaitValue()
            assertTrue(actualResult is Success)
            val actualResultData = (actualResult as Success).data
            assertEquals(titleFilter, actualResultData.second)
        }
    }

    @Test
    fun `when resetFilterSelected should return Success`() {
        runBlocking {
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

            penaltyViewModel.resetFilterSelected()
            val actualResult = penaltyViewModel.resetFilterResult.observeAwaitValue()
            assertTrue(actualResult is Success)
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
}