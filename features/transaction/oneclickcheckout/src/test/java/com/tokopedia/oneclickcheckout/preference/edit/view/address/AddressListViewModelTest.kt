package com.tokopedia.oneclickcheckout.preference.edit.view.address

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticdata.domain.model.AddressListModel
import com.tokopedia.logisticdata.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.oneclickcheckout.common.dispatchers.TestDispatchers
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable

class AddressListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val getAddressCornerUseCase: GetAddressCornerUseCase = mockk(relaxed = true)

    private lateinit var addressListViewModel: AddressListViewModel

    @Before
    fun setUp() {
        addressListViewModel = AddressListViewModel(getAddressCornerUseCase, TestDispatchers)
    }

    @Test
    fun `Search Address Success`() {
        val response = AddressListModel()
        every { getAddressCornerUseCase.getAll(any()) } returns Observable.just(response).doOnSubscribe {
            assertEquals(OccState.Loading, addressListViewModel.addressList.value)
        }

        addressListViewModel.searchAddress("")

        assertEquals(OccState.Success(response), addressListViewModel.addressList.value)
    }

    @Test
    fun `Set Selected Address`() {
        val response = AddressListModel(listAddress = listOf(RecipientAddressModel().apply { id = "1" },
                RecipientAddressModel().apply {
                    id = "2"
                    destinationDistrictId = "2"
                    latitude = "2"
                    longitude = "2"
                    postalCode = "2"
                }))
        every { getAddressCornerUseCase.getAll(any()) } returns Observable.just(response)

        addressListViewModel.searchAddress("")
        addressListViewModel.setSelectedAddress("2")

        response.listAddress[1].isSelected = true
        assertEquals(OccState.Success(response), addressListViewModel.addressList.value)
    }

    @Test
    fun `Search Address Failed`() {
        val response = Throwable()
        every { getAddressCornerUseCase.getAll(any()) } returns Observable.error(response)

        addressListViewModel.searchAddress("")

        assertEquals(OccState.Fail(false, response, ""), addressListViewModel.addressList.value)
    }

    @Test
    fun `Consume Search Address Failed`() {
        val response = Throwable()
        every { getAddressCornerUseCase.getAll(any()) } returns Observable.error(response)

        addressListViewModel.searchAddress("")
        addressListViewModel.consumeSearchAddressFail()

        assertEquals(OccState.Fail(true, response, ""), addressListViewModel.addressList.value)
    }
}
