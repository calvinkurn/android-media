package com.tokopedia.sellerorder.filter

import com.tokopedia.sellerorder.filter.domain.SomFilterResponse
import com.tokopedia.sellerorder.filter.domain.mapper.GetSomFilterMapper
import com.tokopedia.sellerorder.filter.presentation.model.BaseSomFilter
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterDateUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.sellerorder.util.TestHelper
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class SomFilterViewModelTest : SomFilterViewModelTestFixture() {

    @Test
    fun `when get Som List Order Param should return equal value`() {
        val mockSomListOrderParam = SomListGetOrderListParam(
                statusList = listOf(450, 500, 501, 520, 530, 540, 550, 600, 601),
                orderTypeList = listOf(1, 2, 3, 4),
                shippingList = listOf(17, 8, 9, 14)
        )

        somFilterViewModel.setSomListGetOrderListParam(mockSomListOrderParam)
        assertEquals(somFilterViewModel.getSomListGetOrderListParam().statusList, mockSomListOrderParam.statusList)
        assertEquals(somFilterViewModel.getSomListGetOrderListParam().orderTypeList, mockSomListOrderParam.orderTypeList)
        assertEquals(somFilterViewModel.getSomListGetOrderListParam().shippingList, mockSomListOrderParam.shippingList)
    }

    @Test
    fun `when get Som Filter List should return equal value`() {
        val mockResponseSomFilterList = TestHelper.createSuccessResponse<SomFilterResponse>(SOM_FILTER_SUCCESS_RESPONSE)
        val mockSomFilterList = GetSomFilterMapper.mapToSomFilterUiModel(mockResponseSomFilterList)

        somFilterViewModel.setSomFilterUiModel(mockSomFilterList)
        somFilterViewModel.getSomFilterUiModel().forEachIndexed { index, item ->
            assertEquals(item.nameFilter, mockSomFilterList[index].nameFilter)
            assertEquals(item.isDividerVisible, mockSomFilterList[index].isDividerVisible)
            assertEquals(item.canSelectMany, mockSomFilterList[index].canSelectMany)
            assertEquals(item.somFilterData.size, mockSomFilterList[index].somFilterData.size)
        }
        assertEquals(somFilterViewModel.getSomFilterUiModel().size, mockSomFilterList.size)
        assertNotNull(somFilterViewModel.getSomFilterUiModel())
    }

    @Test
    fun `when get som filter data should return success`() {
        runBlocking {
            val baseSomFilterList = mutableListOf<BaseSomFilter>()
            coEvery {
                getSomOrderFilterUseCase.execute()
            } returns baseSomFilterList

            somFilterViewModel.getSomFilterData(mockIdFilter, mockDate)
            coVerify {
                getSomOrderFilterUseCase.execute()
            }
            assertTrue(somFilterViewModel.filterResult.value is Success)
            assertNotNull(somFilterViewModel.filterResult.value)
        }
    }

    @Test
    fun `when get som filter data should return fail`() {
        runBlocking {
            val exception = NullPointerException()
            coEvery { getSomOrderFilterUseCase.execute() } coAnswers { throw exception }

            somFilterViewModel.getSomFilterData(mockIdFilter, mockDate)

            val expectedFail = Fail(exception)
            somFilterViewModel.filterResult.verifyErrorEquals(expectedFail)
        }
    }

    @Test
    fun `when update filter selected should return success`() {
        runBlocking {
            somFilterViewModel.updateFilterSelected(mockIdFilter, 2, ChipsUnify.TYPE_NORMAL, mockDate)
            somFilterViewModel.updateParamSom(mockIdFilter)
            val somFilterSuccess = (somFilterViewModel.updateFilterSelected.value as Success).data
            val somFilterDataList = somFilterSuccess.filterIsInstance<SomFilterUiModel>()
            val filterDate = somFilterSuccess.filterIsInstance<SomFilterDateUiModel>()?.firstOrNull()
            assertTrue(somFilterViewModel.somFilterOrderListParam.value is Success)
            assertNotNull(somFilterDataList)
            assertNotNull(filterDate)
        }
    }

    @Test
    fun `when update filter many selected should return success`() {
        runBlocking {
            somFilterViewModel.updateFilterManySelected(mockIdFilter, ChipsUnify.TYPE_NORMAL, 2, mockDate)
            somFilterViewModel.updateParamSom(mockIdFilter)
            val somFilterSuccess = (somFilterViewModel.updateFilterSelected.value as Success).data
            val somFilterDataList = somFilterSuccess.filterIsInstance<SomFilterUiModel>()
            val filterDate = somFilterSuccess.filterIsInstance<SomFilterDateUiModel>().firstOrNull()
            assertTrue(somFilterViewModel.somFilterOrderListParam.value is Success)
            assertNotNull(somFilterDataList)
            assertNotNull(filterDate)
        }
    }

    @Test
    fun `when update som filter see all should return success`() {
        runBlocking {
            val mockSubFilterList = mutableListOf<SomFilterChipsUiModel>().apply {
                add(SomFilterChipsUiModel(idList = listOf(220), idFilter = "Pesanan Baru", key = "new_order", amount = 0, isSelected = true))
                add(SomFilterChipsUiModel(idList = listOf(400), idFilter = "Siap Dikirim", key = "confirm_shipping", amount = 0, isSelected = true))
                add(SomFilterChipsUiModel(idList = listOf(450, 500, 501, 520, 530, 540, 550, 600, 601),
                        idFilter = "Dalam Pengiriman", key = "in_shipping", isSelected = true))
                add(SomFilterChipsUiModel(idList = listOf(690, 691, 695, 698, 699, 700, 701), idFilter = "Pesanan Selesai", key = "done", amount = 0, isSelected = false))
            }

            somFilterViewModel.updateSomFilterSeeAll(mockIdFilter, mockSubFilterList, mockDate)
            somFilterViewModel.updateParamSom(mockIdFilter)
            val somFilterSuccess = (somFilterViewModel.updateFilterSelected.value as Success).data
            val somFilterDataList = somFilterSuccess.filterIsInstance<SomFilterUiModel>()
            val filterDate = somFilterSuccess.filterIsInstance<SomFilterDateUiModel>().firstOrNull()
            assertNotNull(somFilterViewModel.somFilterOrderListParam.value)
            assertNotNull(somFilterDataList)
            assertNotNull(filterDate)
        }
    }

    @Test
    fun `when reset all of som filter should return success`() {
        runBlocking {
            somFilterViewModel.resetFilterSelected(mockIdFilter)
            val somFilterSuccess = (somFilterViewModel.updateFilterSelected.value as Success).data
            val somFilterDataList = somFilterSuccess.filterIsInstance<SomFilterUiModel>()
            val filterDate = somFilterSuccess.filterIsInstance<SomFilterDateUiModel>().firstOrNull()
            assertNotNull(somFilterDataList)
            assertNotNull(filterDate)
        }
    }
}