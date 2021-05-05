package com.tokopedia.oneclickcheckout.preference.edit.view.address

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.oneclickcheckout.common.view.model.Failure
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
        addressListViewModel = AddressListViewModel(getAddressCornerUseCase, CoroutineTestDispatchersProvider)
    }

    @Test
    fun `Search Address Success`() {
        val token = Token()
        val response = AddressListModel(token = token)
        val searchQuery = "search"
        every { getAddressCornerUseCase.execute(any(), any(), any(), any()) } returns Observable.just(response).doOnSubscribe {
            assertEquals(OccState.Loading, addressListViewModel.addressList.value)
        }

        addressListViewModel.searchAddress(searchQuery, 0, "0", false)

        assertEquals(OccState.FirstLoad(response), addressListViewModel.addressList.value)
        assertEquals(searchQuery, addressListViewModel.savedQuery)
        assertEquals(token, addressListViewModel.token)
    }

    @Test
    fun `Search Address Success With Selected Id`() {
        val response = AddressListModel(listAddress = listOf(
                RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    latitude = "1"
                    longitude = "1"
                    postalCode = "1"
                    addressName = "1"
                    recipientName = "1"
                },
                RecipientAddressModel().apply {
                    id = "2"
                    destinationDistrictId = "2"
                    latitude = "2"
                    longitude = "2"
                    postalCode = "2"
                    addressName = "2"
                    recipientName = "2"
                }))
        every { getAddressCornerUseCase.execute(any(), any(), any(), any()) } returns Observable.just(response)

        addressListViewModel.selectedId = "2"
        addressListViewModel.searchAddress("", 0, "0", false)

        response.listAddress[1].isSelected = true
        assertEquals(OccState.FirstLoad(response), addressListViewModel.addressList.value)
        assertEquals("2", addressListViewModel.selectedId)
        assertEquals("2", addressListViewModel.destinationLongitude)
        assertEquals("2", addressListViewModel.destinationLatitude)
        assertEquals("2", addressListViewModel.destinationDistrict)
        assertEquals("2", addressListViewModel.destinationPostalCode)
    }

    @Test
    fun `Search Address Success With Chosen Address`() {
        val response = AddressListModel(listAddress = listOf(
                RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    latitude = "1"
                    longitude = "1"
                    postalCode = "1"
                    addressName = "1"
                    recipientName = "1"
                },
                RecipientAddressModel().apply {
                    id = "2"
                    destinationDistrictId = "2"
                    latitude = "2"
                    longitude = "2"
                    postalCode = "2"
                    isStateChosenAddress = true
                    addressName = "2"
                    recipientName = "2"
                }))
        every { getAddressCornerUseCase.execute(any(), any(), any(), any()) } returns Observable.just(response)

        addressListViewModel.searchAddress("", 0, "0", false)

        response.listAddress[1].isSelected = true
        assertEquals(OccState.FirstLoad(response), addressListViewModel.addressList.value)
        assertEquals("2", addressListViewModel.selectedId)
        assertEquals("2", addressListViewModel.destinationLongitude)
        assertEquals("2", addressListViewModel.destinationLatitude)
        assertEquals("2", addressListViewModel.destinationDistrict)
        assertEquals("2", addressListViewModel.destinationPostalCode)
    }

    @Test
    fun `Set Selected Address`() {
        val addressModel1 = RecipientAddressModel().apply {
            id = "1"
            destinationDistrictId = "1"
            latitude = "1"
            longitude = "1"
            postalCode = "1"
            addressName = "1"
            recipientName = "1"
        }
        val addressModel2 = RecipientAddressModel().apply {
            id = "2"
            destinationDistrictId = "2"
            latitude = "2"
            longitude = "2"
            postalCode = "2"
            addressName = "2"
            recipientName = "2"
        }
        val response = AddressListModel(listAddress = listOf(addressModel1, addressModel2))
        every { getAddressCornerUseCase.execute(any(), any(), any(), any()) } returns Observable.just(response)

        addressListViewModel.searchAddress("", 0, "0", false)
        addressListViewModel.setSelectedAddress(addressModel2)

        response.listAddress[1].isSelected = true
        assertEquals(OccState.Success(response), addressListViewModel.addressList.value)
        assertEquals("2", addressListViewModel.selectedId)
        assertEquals("2", addressListViewModel.destinationLongitude)
        assertEquals("2", addressListViewModel.destinationLatitude)
        assertEquals("2", addressListViewModel.destinationDistrict)
        assertEquals("2", addressListViewModel.destinationPostalCode)
    }

    @Test
    fun `Set Selected Address On Null State`() {
        val addressModel2 = RecipientAddressModel().apply {
            id = "2"
            destinationDistrictId = "2"
            latitude = "2"
            longitude = "2"
            postalCode = "2"
        }

        addressListViewModel.setSelectedAddress(addressModel2)

        assertEquals("-1", addressListViewModel.selectedId)
        assertEquals("", addressListViewModel.destinationLongitude)
        assertEquals("", addressListViewModel.destinationLatitude)
        assertEquals("", addressListViewModel.destinationDistrict)
        assertEquals("", addressListViewModel.destinationPostalCode)
    }

    @Test
    fun `Set Selected Address On Invalid State`() {
        val addressModel1 = RecipientAddressModel().apply {
            id = "1"
            destinationDistrictId = "1"
            latitude = "1"
            longitude = "1"
            postalCode = "1"
        }
        val addressModel2 = RecipientAddressModel().apply {
            id = "2"
            destinationDistrictId = "2"
            latitude = "2"
            longitude = "2"
            postalCode = "2"
        }
        val response = AddressListModel(listAddress = listOf(addressModel1, addressModel2))
        every { getAddressCornerUseCase.execute("", null, null, false) } returns Observable.just(response)
        addressListViewModel.searchAddress("", 0, "0", false)

        addressListViewModel.searchAddress("search", 0, "0", false)
        addressListViewModel.setSelectedAddress(addressModel2)

        assertEquals("-1", addressListViewModel.selectedId)
        assertEquals("", addressListViewModel.destinationLongitude)
        assertEquals("", addressListViewModel.destinationLatitude)
        assertEquals("", addressListViewModel.destinationDistrict)
        assertEquals("", addressListViewModel.destinationPostalCode)
    }

    @Test
    fun `Search Address Failed`() {
        val response = Throwable()
        every { getAddressCornerUseCase.execute(any(), any(), any(), any()) } returns Observable.error(response)

        addressListViewModel.searchAddress("", 0, "0", false)

        assertEquals(OccState.Failed(Failure(response)), addressListViewModel.addressList.value)
    }

    @Test
    fun `Load More Address Success With No Next Page`() {
        val token = Token()
        val listAddress = arrayListOf<RecipientAddressModel>()
        for (i in 0..9) {
            listAddress.add(RecipientAddressModel().apply {
                id = i.toString()
            })
        }
        val response = AddressListModel(token = token, listAddress = listAddress)
        every { getAddressCornerUseCase.execute(any(), any(), any(), any()) } returns Observable.just(response)
        addressListViewModel.searchAddress("", 0, "0", false)
        val additionalAddress = RecipientAddressModel().apply {
            id = "11"
        }
        every { getAddressCornerUseCase.loadMore(any(), any(), any(), any(), any()) } returns Observable.just(AddressListModel(token = token, listAddress = listOf(additionalAddress)))

        addressListViewModel.loadMore(0, "0", false)

        assertEquals(OccState.Success(response.copy(hasNext = false, listAddress = listAddress + additionalAddress)), addressListViewModel.addressList.value)
    }

    @Test
    fun `Load More Address Success With Has Next Page`() {
        val token = Token()
        val listAddress = arrayListOf<RecipientAddressModel>()
        for (i in 0..9) {
            listAddress.add(RecipientAddressModel().apply {
                id = i.toString()
            })
        }
        val response = AddressListModel(token = token, listAddress = listAddress)
        every { getAddressCornerUseCase.execute(any(), any(), any(), any()) } returns Observable.just(response)
        addressListViewModel.searchAddress("", 0, "0", false)
        every { getAddressCornerUseCase.loadMore(any(), any(), any(), any(), any()) } returns Observable.just(AddressListModel(token = token, listAddress = listAddress))

        addressListViewModel.loadMore(0, "0", false)

        assertEquals(OccState.Success(response.copy(hasNext = true, listAddress = listAddress + listAddress)), addressListViewModel.addressList.value)
    }

    @Test
    fun `Load More Address Failed`() {
        val token = Token()
        val listAddress = arrayListOf<RecipientAddressModel>()
        for (i in 0..9) {
            listAddress.add(RecipientAddressModel().apply {
                id = i.toString()
            })
        }
        every { getAddressCornerUseCase.execute(any(), any(), any(), any()) } returns Observable.just(AddressListModel(token = token, listAddress = listAddress))
        addressListViewModel.searchAddress("", 0, "0", false)
        val response = Throwable()
        every { getAddressCornerUseCase.loadMore(any(), any(), any(), any(), any()) } returns Observable.error(response)

        addressListViewModel.loadMore(0, "0", false)

        assertEquals(OccState.Failed(Failure(response)), addressListViewModel.addressList.value)
    }
}
