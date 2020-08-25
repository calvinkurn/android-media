package com.tokopedia.manageaddress.ui.manageaddress

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logisticdata.domain.model.AddressListModel
import com.tokopedia.logisticdata.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.manageaddress.domain.DeletePeopleAddressUseCase
import com.tokopedia.manageaddress.domain.SetDefaultPeopleAddressUseCase
import com.tokopedia.manageaddress.domain.model.ManageAddressState
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable

class ManageAddressViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val success = "Success"

    private val getPeopleAddressUseCase: GetAddressCornerUseCase = mockk(relaxed = true)
    private val deletePeopleAddressUseCase: DeletePeopleAddressUseCase = mockk(relaxed = true)
    private val setDetaultPeopleAddressUseCase: SetDefaultPeopleAddressUseCase = mockk(relaxed = true)

    private lateinit var manageAddressViewModel: ManageAddressViewModel

    @Before
    fun setUp() {
        manageAddressViewModel = ManageAddressViewModel(getPeopleAddressUseCase, deletePeopleAddressUseCase, setDetaultPeopleAddressUseCase)
    }

    @Test
    fun `Search Address Success`() {
        val response = AddressListModel()
        every { getPeopleAddressUseCase.execute(any()) } returns Observable.just(response).doOnSubscribe {
            assertEquals(ManageAddressState.Loading, manageAddressViewModel.addressList.value)
        }

        manageAddressViewModel.searchAddress("")

        assertEquals(ManageAddressState.Success(response), manageAddressViewModel.addressList.value)
    }

    @Test
    fun `Search Address Failed`() {
        val response = Throwable()
        every { getPeopleAddressUseCase.execute(any()) } returns Observable.error(response)

        manageAddressViewModel.searchAddress("")

        assertEquals(ManageAddressState.Fail(response, ""), manageAddressViewModel.addressList.value)
    }

    @Test
    fun `Load More Address Success`() {
        val response = AddressListModel()
        every { getPeopleAddressUseCase.loadMore(any(), any()) } returns Observable.just(response).doOnSubscribe {
            assertEquals(ManageAddressState.Loading, manageAddressViewModel.addressList.value)
        }

        manageAddressViewModel.loadMore()

        assertEquals(ManageAddressState.Success(response), manageAddressViewModel.addressList.value)
    }

    @Test
    fun `Load More Address Failed`() {
        val response = Throwable()
        every { getPeopleAddressUseCase.loadMore(any(), any()) } returns Observable.error(response)

        manageAddressViewModel.loadMore()

        assertEquals(ManageAddressState.Fail(response, ""), manageAddressViewModel.addressList.value)
    }

    @Test
    fun `Set Default Address Success`() {
        every {
            setDetaultPeopleAddressUseCase.execute(any(), any(), any())
        } answers  {
            assertEquals(ManageAddressState.Loading, manageAddressViewModel.result.value)
            (secondArg() as ((String) -> Unit)).invoke(success)
        }

        manageAddressViewModel.setDefaultPeopleAddress("1")

        assertEquals(ManageAddressState.Success(success), manageAddressViewModel.result.value)
    }

    @Test
    fun `Set Default Address Fail`() {
        val response = Throwable()
        every {
            setDetaultPeopleAddressUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(ManageAddressState.Loading, manageAddressViewModel.result.value)
            (thirdArg() as ((Throwable) -> Unit)).invoke(response)
        }

        manageAddressViewModel.setDefaultPeopleAddress("1")

        assertEquals(ManageAddressState.Fail(response, ""), manageAddressViewModel.addressList.value)
    }

    @Test
    fun `Delete Address Success`() {
        every {
            deletePeopleAddressUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(ManageAddressState.Loading, manageAddressViewModel.result.value)
            (secondArg() as ((String) -> Unit)).invoke(success)
        }

        manageAddressViewModel.deletePeopleAddress("1")

        assertEquals(ManageAddressState.Success(success), manageAddressViewModel.result.value)
    }

    @Test
    fun `Delete Address Fail`() {
        val response = Throwable()
        every {
            deletePeopleAddressUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(ManageAddressState.Loading, manageAddressViewModel.result.value)
            (thirdArg() as ((Throwable) -> Unit)).invoke(response)
        }

        manageAddressViewModel.deletePeopleAddress("1")

        assertEquals(ManageAddressState.Fail(response, ""), manageAddressViewModel.addressList.value)
    }
}