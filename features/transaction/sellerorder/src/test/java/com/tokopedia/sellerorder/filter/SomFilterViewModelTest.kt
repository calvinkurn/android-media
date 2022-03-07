package com.tokopedia.sellerorder.filter

import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.filter.presentation.model.BaseSomFilter
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterDateUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.sellerorder.util.observeAwaitValue
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test

@ExperimentalCoroutinesApi
class SomFilterViewModelTest : SomFilterViewModelTestFixture() {

    @Test
    fun `when get Som List Order Param should return equal value`() =
        coroutineTestRule.runBlockingTest {
            val mockSomListOrderParam = SomListGetOrderListParam(
                statusList = listOf(450, 500, 501, 520, 530, 540, 550, 600, 601),
                orderTypeList = mutableSetOf(1, 2, 3, 4),
                shippingList = mutableSetOf(17, 8, 9, 14)
            )

            somFilterViewModel.setSomListGetOrderListParam(mockSomListOrderParam)
            assertEquals(
                mockSomListOrderParam.statusList,
                somFilterViewModel.getSomListGetOrderListParam().statusList
            )
            assertEquals(
                mockSomListOrderParam.orderTypeList,
                somFilterViewModel.getSomListGetOrderListParam().orderTypeList
            )
            assertEquals(
                mockSomListOrderParam.shippingList,
                somFilterViewModel.getSomListGetOrderListParam().shippingList
            )
        }

    @Test
    fun `when get Som Filter List should return equal value`() = coroutineTestRule.runBlockingTest {
        val mockSomFilterList = getMockSomFilterList()
        val expectedSomFilterUiModels = mockSomFilterList.filterIsInstance<SomFilterUiModel>()
        somFilterViewModel.setSomFilterUiModel(mockSomFilterList.filterIsInstance<SomFilterUiModel>())
        somFilterViewModel.getSomFilterUiModel().forEachIndexed { index, item ->
            val mockSomFilterUiModel = (mockSomFilterList[index] as SomFilterUiModel)
            assertEquals(item.nameFilter, mockSomFilterUiModel.nameFilter)
            assertEquals(item.isDividerVisible, mockSomFilterUiModel.isDividerVisible)
            assertEquals(item.canSelectMany, mockSomFilterUiModel.canSelectMany)
            assertEquals(item.somFilterData.size, mockSomFilterUiModel.somFilterData.size)
        }
        assertEquals(expectedSomFilterUiModels, somFilterViewModel.getSomFilterUiModel())
    }

    @Test
    fun `when given empty date then get som filter data should not replacing current date filter date value`() {
        val mockSomFilterUiModel = getMockSomFilterList()
        val expected = mockSomFilterUiModel.filterIsInstance<SomFilterDateUiModel>().first().date
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModel
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData(mockIdFilter, "")
        }
        val filterResult = somFilterViewModel.filterResult.observeAwaitValue() as Success
        val actual = filterResult.data.filterIsInstance<SomFilterDateUiModel>().first().date
        assertEquals(expected, actual)
    }

    @Test
    fun `when given non empty date then get som filter data should replacing current date filter date value`() {
        val mockSomFilterUiModel = getMockSomFilterList()
        val expected = "14 Okt 2021 - 24 Okt 2021"
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModel
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData(mockIdFilter, expected)
        }
        val filterResult = somFilterViewModel.filterResult.observeAwaitValue() as Success
        val actual = filterResult.data.filterIsInstance<SomFilterDateUiModel>().first().date
        assertEquals(expected, actual)
    }

    @Test
    fun `when current filter data is empty then get som filter data should replacing current filter data`() {
        val mockSomFilterUiModel = getMockSomFilterList()
        val currentFilterData = somFilterViewModel.getSomFilterUiModel().toMutableList()
        val expectedSomFilterUiModels = mockSomFilterUiModel.filterIsInstance<SomFilterUiModel>()
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModel
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData(mockIdFilter, mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        assertNotEquals(currentFilterData, somFilterViewModel.getSomFilterUiModel())
        assertEquals(expectedSomFilterUiModels, somFilterViewModel.getSomFilterUiModel())
    }

    @Test
    fun `when current filter data is not empty then get som filter data should not replacing current filter data`() {
        val mockSomFilterUiModel = getMockSomFilterList()
        val currentFilterData = mutableListOf<SomFilterUiModel>(mockk(), mockk(), mockk())
        somFilterViewModel.setSomFilterUiModel(currentFilterData)
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModel
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData(mockIdFilter, mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        assertEquals(currentFilterData, somFilterViewModel.getSomFilterUiModel())
        assertNotEquals(mockSomFilterUiModel, somFilterViewModel.getSomFilterUiModel())
    }

    @Test
    fun `when given valid order status then get som filter data should select corresponding filter`() {
        val expected = true
        val mockSomFilterUiModel = getMockSomFilterList()
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModel
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData(mockIdFilter, mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        val actual = somFilterViewModel.getSomFilterUiModel().find {
            it.nameFilter == SomConsts.FILTER_STATUS_ORDER
        }?.somFilterData?.find {
            it.name == mockIdFilter
        }?.isSelected.orFalse()
        assertEquals(expected, actual)
    }

    @Test
    fun `when given empty order status then get som filter data should not elect any filter`() {
        val mockSomFilterUiModel = getMockSomFilterList()
        val expected = mockSomFilterUiModel.filterIsInstance<SomFilterUiModel>()
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModel
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData("", mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        val actual = somFilterViewModel.getSomFilterUiModel()
        assertEquals(expected, actual)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `when get som filter data should return success and select request cancel filter`() {
        val mockCancelFilterApplied = true
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns getMockSomFilterList()
        somFilterViewModel.setIsRequestCancelFilterApplied(mockCancelFilterApplied)
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData(mockIdFilter, mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        coVerify {
            getSomOrderFilterUseCase.execute()
        }
        val expectedRequestCancelFilterSelectedStatus = somFilterViewModel.getSomFilterUiModel()
            .getRequestCancelFilter()?.isSelected
        assertEquals(mockCancelFilterApplied, somFilterViewModel.isRequestCancelFilterApplied())
        assertEquals(true, expectedRequestCancelFilterSelectedStatus)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `when get som filter data should return success and should not select request cancel filter`() {
        val mockCancelFilterApplied = false
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns getMockSomFilterList()
        somFilterViewModel.setIsRequestCancelFilterApplied(mockCancelFilterApplied)
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData(mockIdFilter, mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        coVerify {
            getSomOrderFilterUseCase.execute()
        }
        val expectedRequestCancelFilterSelectedStatus = somFilterViewModel.getSomFilterUiModel()
            .getRequestCancelFilter()?.isSelected
        assertEquals(mockCancelFilterApplied, somFilterViewModel.isRequestCancelFilterApplied())
        assertEquals(false, expectedRequestCancelFilterSelectedStatus)
    }

    @Test
    fun `when get som filter data should return fail`() = coroutineTestRule.runBlockingTest {
        val exception = NullPointerException()
        coEvery { getSomOrderFilterUseCase.execute() } coAnswers { throw exception }
        somFilterViewModel.getSomFilterData(mockIdFilter, mockDate)
        val expectedFail = Fail(exception)
        somFilterViewModel.filterResult.verifyErrorEquals(expectedFail)
    }

    @Test
    fun `when given valid idFilter, valid position and chip type normal, update filter selected should deselect other filter and select corresponding filter and then success`() {
        val idFilter = SomConsts.FILTER_SORT
        val mockSomFilterUiModels = getMockSomFilterList()
        val previouslySelectedFilterItem =
            mockSomFilterUiModels.filterIsInstance<SomFilterUiModel>().find {
                it.nameFilter == idFilter
            }!!.somFilterData.last()
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModels
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData(previouslySelectedFilterItem.name, mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.updateFilterSelected(idFilter, 0, ChipsUnify.TYPE_NORMAL)
        }
        val somFilterSuccess = (somFilterViewModel.updateFilterSelected.observeAwaitValue() as Success).data
        assertNotNull(somFilterSuccess.first)
        assertTrue(somFilterSuccess.first.isNotEmpty())
        assertTrue(somFilterSuccess.first.first().isSelected)
        assertFalse(somFilterSuccess.first.last().isSelected)
    }

    @Test
    fun `when given valid idFilter, valid position and chip type selected, update filter selected should deselect other filter and not select corresponding filter and then success`() {
        val idFilter = SomConsts.FILTER_SORT
        val mockSomFilterUiModels = getMockSomFilterList()
        val previouslySelectedFilterItem =
            mockSomFilterUiModels.filterIsInstance<SomFilterUiModel>().find {
                it.nameFilter == idFilter
            }!!.somFilterData.last()
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModels
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData(previouslySelectedFilterItem.name, mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.updateFilterSelected(idFilter, 0, ChipsUnify.TYPE_SELECTED)
        }
        val somFilterSuccess = (somFilterViewModel.updateFilterSelected.observeAwaitValue() as Success).data
        assertNotNull(somFilterSuccess.first)
        assertTrue(somFilterSuccess.first.isNotEmpty())
        assertFalse(somFilterSuccess.first.first().isSelected)
        assertFalse(somFilterSuccess.first.last().isSelected)
    }

    @Test
    fun `when given valid idFilter and invalid position, update filter selected should deselect other filter items and not select any filter items and then success`() {
        val idFilter = SomConsts.FILTER_SORT
        val mockSomFilterUiModels = getMockSomFilterList()
        val previouslySelectedFilterItem =
            mockSomFilterUiModels.filterIsInstance<SomFilterUiModel>().find {
                it.nameFilter == idFilter
            }!!.somFilterData.last()
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModels
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData(previouslySelectedFilterItem.name, mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.updateFilterSelected(idFilter, 2, ChipsUnify.TYPE_NORMAL)
        }
        val somFilterSuccess = (somFilterViewModel.updateFilterSelected.observeAwaitValue() as Success).data
        assertNotNull(somFilterSuccess.first)
        assertTrue(somFilterSuccess.first.isNotEmpty())
        assertFalse(somFilterSuccess.first.first().isSelected)
        assertFalse(somFilterSuccess.first.last().isSelected)
    }

    @Test
    fun `when given invalid idFilter, update filter selected should not change any filters and then success`() {
        val mockSomFilterUiModels = getMockSomFilterList()
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModels
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData("", mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.updateFilterSelected("", 0, ChipsUnify.TYPE_NORMAL)
        }
        val somFilterSuccess = (somFilterViewModel.updateFilterSelected.observeAwaitValue() as Success).data
        assertNotNull(somFilterSuccess.first)
        assertTrue(somFilterSuccess.first.isEmpty())
        assertTrue(somFilterViewModel.getSomFilterUiModel().all {
            it.somFilterData.all {
                !it.isSelected
            }
        })
    }

    @Test
    fun `when given valid idFilter, valid position and chip type normal, update filter many selected should not deselect any other filter and select corresponding filter and then success`() {
        val idFilter = SomConsts.FILTER_STATUS_ORDER
        val mockSomFilterUiModels = getMockSomFilterList()
        val previouslySelectedFilterItem =
            mockSomFilterUiModels.filterIsInstance<SomFilterUiModel>()
                .find {
                    it.nameFilter == idFilter
                }!!.somFilterData.last()
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModels
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData(previouslySelectedFilterItem.name, mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.updateFilterManySelected(idFilter, ChipsUnify.TYPE_NORMAL, 0)
        }
        val somFilterSuccess = (somFilterViewModel.updateFilterSelected.observeAwaitValue() as Success).data
        assertNotNull(somFilterSuccess.first)
        assertTrue(somFilterSuccess.first.isNotEmpty())
        assertTrue(somFilterSuccess.first.first().isSelected)
        assertTrue(somFilterSuccess.first.last().isSelected)
    }

    @Test
    fun `when given valid idFilter, valid position and chip type selected, update filter many selected should not deselect any other filter except corresponding filter and then success`() {
        val idFilter = SomConsts.FILTER_STATUS_ORDER
        val mockSomFilterUiModels = getMockSomFilterList()
        val previouslySelectedFilterItem =
            mockSomFilterUiModels.filterIsInstance<SomFilterUiModel>()
                .find {
                    it.nameFilter == idFilter
                }!!.somFilterData.last()
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModels
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData(previouslySelectedFilterItem.name, mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.updateFilterManySelected(idFilter, ChipsUnify.TYPE_NORMAL, 0)
            somFilterViewModel.updateFilterManySelected(idFilter, ChipsUnify.TYPE_SELECTED, 6)
        }
        val somFilterSuccess = (somFilterViewModel.updateFilterSelected.observeAwaitValue() as Success).data
        assertNotNull(somFilterSuccess.first)
        assertTrue(somFilterSuccess.first.isNotEmpty())
        assertTrue(somFilterSuccess.first.first().isSelected)
        assertFalse(somFilterSuccess.first.last().isSelected)
    }

    @Test
    fun `when given valid idFilter and invalid position, update filter many selected should should not change any filters and then success`() {
        val idFilter = SomConsts.FILTER_STATUS_ORDER
        val mockSomFilterUiModels = getMockSomFilterList()
        val previouslySelectedFilterItem =
            mockSomFilterUiModels.filterIsInstance<SomFilterUiModel>()
                .find {
                    it.nameFilter == idFilter
                }!!.somFilterData.last()
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModels
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData(previouslySelectedFilterItem.name, mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.updateFilterManySelected(idFilter, ChipsUnify.TYPE_NORMAL, -1)
        }
        val somFilterSuccess = (somFilterViewModel.updateFilterSelected.observeAwaitValue() as Success).data
        assertNotNull(somFilterSuccess.first)
        assertTrue(somFilterSuccess.first.isNotEmpty())
        assertTrue(somFilterSuccess.first.subList(Int.ZERO, somFilterSuccess.first.size - 1).all { !it.isSelected })
        assertTrue(somFilterSuccess.first.last().isSelected)
    }

    @Test
    fun `when given invalid idFilter, update filter many selected should should not change any filters and then success`() {
        val mockSomFilterUiModels = getMockSomFilterList()
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModels
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData("", mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.updateFilterManySelected("", ChipsUnify.TYPE_NORMAL, 0)
        }
        val somFilterSuccess = (somFilterViewModel.updateFilterSelected.observeAwaitValue() as Success).data
        assertNotNull(somFilterSuccess.first)
        assertTrue(somFilterSuccess.first.isEmpty())
        assertTrue(somFilterViewModel.getSomFilterUiModel().all {
            it.somFilterData.all {
                !it.isSelected
            }
        })
    }

    @Test
    fun `when given valid idFilter, update som filter see all should update corresponding filter items and then return success`() = coroutineTestRule.runBlockingTest {
        val idFilter = SomConsts.FILTER_STATUS_ORDER
        val mockSomFilterUiModels = getMockSomFilterList()
        val mockSomSubFilter = mockSomFilterUiModels.filterIsInstance<SomFilterUiModel>().find {
            it.nameFilter == idFilter
        }!!.somFilterData
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModels
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData("", mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        mockSomSubFilter.forEach {
            it.isSelected = true
        }
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.updateSomFilterSeeAll(idFilter, mockSomSubFilter)
        }
        val somFilterSuccess = (somFilterViewModel.updateFilterSelected.observeAwaitValue() as Success).data
        assertNotNull(somFilterSuccess.first)
        assertTrue(somFilterSuccess.first.isNotEmpty())
        assertEquals(mockSomFilterUiModels.filterIsInstance<SomFilterUiModel>(), somFilterViewModel.getSomFilterUiModel())
    }

    @Test
    fun `when given invalid idFilter, update som filter see all should not make any changes and then return success`() = coroutineTestRule.runBlockingTest {
        val idFilter = SomConsts.FILTER_STATUS_ORDER
        val mockSomFilterUiModels = getMockSomFilterList()
        val mockSomSubFilter = mockSomFilterUiModels.filterIsInstance<SomFilterUiModel>().find {
            it.nameFilter == idFilter
        }!!.somFilterData
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModels
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData("", mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        mockSomSubFilter.forEach {
            it.isSelected = true
        }
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.updateSomFilterSeeAll("", mockSomSubFilter)
        }
        val somFilterSuccess = (somFilterViewModel.updateFilterSelected.observeAwaitValue() as Success).data
        assertNotNull(somFilterSuccess.first)
        assertTrue(somFilterSuccess.first.isEmpty())
        assertEquals(mockSomFilterUiModels.filterIsInstance<SomFilterUiModel>(), somFilterViewModel.getSomFilterUiModel())
    }

    @Test
    fun `when given filter sort idFilter should update somFilterOrderListParam sortBy`() {
        val idFilter = SomConsts.FILTER_SORT
        val mockSomFilterUiModels = getMockSomFilterList()
        val indexOfSelectedSortFilterItem = mockSomFilterUiModels.filterIsInstance<SomFilterUiModel>()
                .find {
                    it.nameFilter == idFilter
                }!!.somFilterData.indexOfFirst { it.id == SomConsts.SORT_ASCENDING }
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModels
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData("", mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.updateFilterManySelected(idFilter, ChipsUnify.TYPE_NORMAL, indexOfSelectedSortFilterItem)
            somFilterViewModel.updateParamSom(idFilter)
        }
        val somFilterSuccess = (somFilterViewModel.updateFilterSelected.observeAwaitValue() as Success).data
        val somFilterOrderListParam = (somFilterViewModel.somFilterOrderListParam.observeAwaitValue() as Success).data
        val expectedSortByValue = getSelectedSortFilterId(somFilterViewModel.getSomFilterUiModel())
        assertNotNull(somFilterSuccess.first)
        assertTrue(somFilterSuccess.first.isNotEmpty())
        assertTrue(somFilterSuccess.first[indexOfSelectedSortFilterItem].isSelected)
        assertEquals(expectedSortByValue, somFilterOrderListParam.sortBy)
    }

    @Test
    fun `when given filter status order idFilter should update somFilterOrderListParam statusList`() {
        val idFilter = SomConsts.FILTER_STATUS_ORDER
        val selectedStatusOrderFilterItemKey = SomConsts.STATUS_NEW_ORDER
        val mockSomFilterUiModels = getMockSomFilterList()
        val indexOfSelectedStatusOrderFilterItem =
            mockSomFilterUiModels.filterIsInstance<SomFilterUiModel>()
                .find {
                    it.nameFilter == idFilter
                }!!.somFilterData
                .indexOfFirst {
                    it.key == selectedStatusOrderFilterItemKey
                }
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModels
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData("", mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.updateFilterManySelected(idFilter, ChipsUnify.TYPE_NORMAL, indexOfSelectedStatusOrderFilterItem)
            somFilterViewModel.updateParamSom(idFilter)
        }
        val somFilterSuccess = (somFilterViewModel.updateFilterSelected.observeAwaitValue() as Success).data
        val somFilterOrderListParam = (somFilterViewModel.somFilterOrderListParam.observeAwaitValue() as Success).data
        val expectedStatusListValue = getSelectedStatusOrderFilterIds(somFilterViewModel.getSomFilterUiModel())
        assertNotNull(somFilterSuccess.first)
        assertTrue(somFilterSuccess.first.isNotEmpty())
        assertTrue(somFilterSuccess.first[indexOfSelectedStatusOrderFilterItem].isSelected)
        assertEquals(expectedStatusListValue, somFilterOrderListParam.statusList)
    }

    @Test
    fun `when given filter order type idFilter should update somFilterOrderListParam orderTypeList`() {
        val idFilter = SomConsts.FILTER_TYPE_ORDER
        val selectedOrderTypeFilterItemId = DeeplinkMapperOrder.FILTER_CANCELLATION_REQUEST
        val mockSomFilterUiModels = getMockSomFilterList()
        val indexOfSelectedOrderTypeFilterItem =
            mockSomFilterUiModels.filterIsInstance<SomFilterUiModel>()
                .find {
                    it.nameFilter == idFilter
                }!!.somFilterData
                .indexOfFirst {
                    it.id == selectedOrderTypeFilterItemId
                }
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModels
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData("", mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.updateFilterManySelected(idFilter, ChipsUnify.TYPE_NORMAL, indexOfSelectedOrderTypeFilterItem)
            somFilterViewModel.updateParamSom(idFilter)
        }
        val somFilterSuccess = (somFilterViewModel.updateFilterSelected.observeAwaitValue() as Success).data
        val somFilterOrderListParam = (somFilterViewModel.somFilterOrderListParam.observeAwaitValue() as Success).data
        val expectedOrderTypeListValue = getSelectedOrderTypeFilterIds(somFilterViewModel.getSomFilterUiModel())
        assertNotNull(somFilterSuccess.first)
        assertTrue(somFilterSuccess.first.isNotEmpty())
        assertTrue(somFilterSuccess.first[indexOfSelectedOrderTypeFilterItem].isSelected)
        assertEquals(expectedOrderTypeListValue, somFilterOrderListParam.orderTypeList)
    }

    @Test
    fun `when given filter courier idFilter should update somFilterOrderListParam shippingList`() {
        val idFilter = SomConsts.FILTER_COURIER
        val selectedCourierFilterItemId = 14
        val mockSomFilterUiModels = getMockSomFilterList()
        val indexOfSelectedCourierFilterItem =
            mockSomFilterUiModels.filterIsInstance<SomFilterUiModel>()
                .find {
                    it.nameFilter == idFilter
                }!!.somFilterData
                .indexOfFirst {
                    it.id == selectedCourierFilterItemId
                }
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModels
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData("", mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.updateFilterManySelected(idFilter, ChipsUnify.TYPE_NORMAL, indexOfSelectedCourierFilterItem)
            somFilterViewModel.updateParamSom(idFilter)
        }
        val somFilterSuccess = (somFilterViewModel.updateFilterSelected.observeAwaitValue() as Success).data
        val somFilterOrderListParam = (somFilterViewModel.somFilterOrderListParam.observeAwaitValue() as Success).data
        val expectedShippingListValue = getSelectedShippingListFilterIds(somFilterViewModel.getSomFilterUiModel())
        assertNotNull(somFilterSuccess.first)
        assertTrue(somFilterSuccess.first.isNotEmpty())
        assertTrue(somFilterSuccess.first[indexOfSelectedCourierFilterItem].isSelected)
        assertEquals(expectedShippingListValue, somFilterOrderListParam.shippingList)
    }

    @Test
    fun `when given filter label idFilter should update somFilterOrderListParam isShippingPrinted`() {
        val idFilter = SomConsts.FILTER_LABEL
        val selectedCourierFilterItemId = SomConsts.NOT_YET_PRINTED
        val mockSomFilterUiModels = getMockSomFilterList()
        val indexOfSelectedLabelFilterItem =
            mockSomFilterUiModels.filterIsInstance<SomFilterUiModel>()
                .find {
                    it.nameFilter == idFilter
                }!!.somFilterData
                .indexOfFirst {
                    it.id == selectedCourierFilterItemId
                }
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModels
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData("", mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.updateFilterManySelected(idFilter, ChipsUnify.TYPE_NORMAL, indexOfSelectedLabelFilterItem)
            somFilterViewModel.updateParamSom(idFilter)
        }
        val somFilterSuccess = (somFilterViewModel.updateFilterSelected.observeAwaitValue() as Success).data
        val somFilterOrderListParam = (somFilterViewModel.somFilterOrderListParam.observeAwaitValue() as Success).data
        val expectedIsShippingPrintedValue = getSelectedIsShippingPrintedFilterId(somFilterViewModel.getSomFilterUiModel())
        assertNotNull(somFilterSuccess.first)
        assertTrue(somFilterSuccess.first.isNotEmpty())
        assertTrue(somFilterSuccess.first[indexOfSelectedLabelFilterItem].isSelected)
        assertEquals(expectedIsShippingPrintedValue, somFilterOrderListParam.isShippingPrinted)
    }

    @Test
    fun `when given invalid idFilter should not update any somFilterOrderListParam`() {
        val idFilter = ""
        val mockSomFilterUiModels = getMockSomFilterList()
        val expectedSortByValue = getSelectedSortFilterId(mockSomFilterUiModels)
        val expectedStatusListValue = getSelectedStatusOrderFilterIds(mockSomFilterUiModels)
        val expectedOrderTypeListValue = getSelectedOrderTypeFilterIds(mockSomFilterUiModels)
        val expectedShippingListValue = getSelectedShippingListFilterIds(mockSomFilterUiModels)
        val expectedIsShippingPrintedValue = getSelectedIsShippingPrintedFilterId(mockSomFilterUiModels)
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModels
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData("", mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.updateFilterManySelected(idFilter, ChipsUnify.TYPE_NORMAL, 0)
            somFilterViewModel.updateParamSom(idFilter)
        }
        val somFilterSuccess = (somFilterViewModel.updateFilterSelected.observeAwaitValue() as Success).data
        val somFilterOrderListParam = (somFilterViewModel.somFilterOrderListParam.observeAwaitValue() as Success).data
        assertNotNull(somFilterSuccess.first)
        assertTrue(somFilterSuccess.first.isEmpty())
        assertEquals(expectedSortByValue, somFilterOrderListParam.sortBy)
        assertEquals(expectedStatusListValue, somFilterOrderListParam.statusList)
        assertEquals(expectedOrderTypeListValue, somFilterOrderListParam.orderTypeList)
        assertEquals(expectedShippingListValue, somFilterOrderListParam.shippingList)
        assertEquals(expectedIsShippingPrintedValue, somFilterOrderListParam.isShippingPrinted)
    }

    @Test
    fun `when reset all of som filter should return success`() = coroutineTestRule.runBlockingTest {
        val mockSomFilterUiModels = getMockSomFilterList()
        val expectedSortByValue = getSelectedSortFilterId(mockSomFilterUiModels)
        val expectedStatusListValue = getSelectedStatusOrderFilterIds(mockSomFilterUiModels)
        val expectedOrderTypeListValue = getSelectedOrderTypeFilterIds(mockSomFilterUiModels)
        val expectedShippingListValue = getSelectedShippingListFilterIds(mockSomFilterUiModels)
        val expectedIsShippingPrintedValue = getSelectedIsShippingPrintedFilterId(mockSomFilterUiModels)
        coEvery {
            getSomOrderFilterUseCase.execute()
        } returns mockSomFilterUiModels
        coroutineTestRule.runBlockingTest {
            somFilterViewModel.getSomFilterData("", mockDate)
        }
        somFilterViewModel.filterResult.observeAwaitValue()
        somFilterViewModel.resetFilterSelected()
        val somFilterSuccess = (somFilterViewModel.resetFilterResult.observeAwaitValue() as Success).data
        val somFilterOrderListParam = (somFilterViewModel.somFilterOrderListParam.observeAwaitValue() as Success).data
        assertNotNull(somFilterSuccess)
        assertEquals(expectedSortByValue, somFilterOrderListParam.sortBy)
        assertEquals(expectedStatusListValue, somFilterOrderListParam.statusList)
        assertEquals(expectedOrderTypeListValue, somFilterOrderListParam.orderTypeList)
        assertEquals(expectedShippingListValue, somFilterOrderListParam.shippingList)
        assertEquals(expectedIsShippingPrintedValue, somFilterOrderListParam.isShippingPrinted)
    }

    @Test
    fun `when reset all of som filter should return fail`() = coroutineTestRule.runBlockingTest {
        mockkObject(Utils) {
            val mockSomFilterUiModels = getMockSomFilterList()
            every {
                Utils.getNPastMonthTimeText(any())
            } throws Throwable()
            coEvery {
                getSomOrderFilterUseCase.execute()
            } returns mockSomFilterUiModels
            coroutineTestRule.runBlockingTest {
                somFilterViewModel.getSomFilterData("", mockDate)
            }
            somFilterViewModel.filterResult.observeAwaitValue()
            somFilterViewModel.resetFilterSelected()
            val resetFilterResult = somFilterViewModel.resetFilterResult.observeAwaitValue()
            assert(resetFilterResult is Fail)
        }
    }

    private fun getSelectedSortFilterId(somFilterUiModels: List<BaseSomFilter>): Int {
        return somFilterUiModels.filterIsInstance<SomFilterUiModel>()
            .find {
                it.nameFilter == SomConsts.FILTER_SORT
            }!!.somFilterData
            .find {
                it.isSelected
            }?.id ?: SomConsts.SORT_BY_PAYMENT_DATE_DESCENDING
    }

    private fun getSelectedStatusOrderFilterIds(somFilterUiModels: List<BaseSomFilter>): List<Int> {
        return somFilterUiModels.filterIsInstance<SomFilterUiModel>()
            .find {
                it.nameFilter == SomConsts.FILTER_STATUS_ORDER
            }!!.somFilterData
            .filter {
                it.isSelected
            }
            .flatMap {
                it.idList
            }
    }

    private fun getSelectedOrderTypeFilterIds(somFilterUiModels: List<BaseSomFilter>): MutableSet<Int> {
        return somFilterUiModels.filterIsInstance<SomFilterUiModel>()
            .find {
                it.nameFilter == SomConsts.FILTER_TYPE_ORDER
            }!!.somFilterData
            .filter {
                it.isSelected
            }
            .map {
                it.id
            }
            .toMutableSet()
    }

    private fun getSelectedShippingListFilterIds(somFilterUiModels: List<BaseSomFilter>): MutableSet<Int> {
        return somFilterUiModels.filterIsInstance<SomFilterUiModel>()
            .find {
                it.nameFilter == SomConsts.FILTER_COURIER
            }!!.somFilterData
            .filter {
                it.isSelected
            }
            .map {
                it.id
            }
            .toMutableSet()
    }

    private fun getSelectedIsShippingPrintedFilterId(somFilterUiModels: List<BaseSomFilter>): Int {
        return somFilterUiModels.filterIsInstance<SomFilterUiModel>()
            .find {
                it.nameFilter == SomConsts.FILTER_LABEL
            }!!.somFilterData
            .find {
                it.isSelected
            }?.id.orZero()
    }
}