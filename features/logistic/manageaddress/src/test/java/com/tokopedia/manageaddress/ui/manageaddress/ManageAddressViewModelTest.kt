package com.tokopedia.manageaddress.ui.manageaddress

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logisticdata.domain.model.AddressListModel
import com.tokopedia.logisticdata.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.manageaddress.domain.DeletePeopleAddressUseCase
import com.tokopedia.manageaddress.domain.SetDefaultPeopleAddressUseCase
import com.tokopedia.manageaddress.domain.model.ManageAddressState
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
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
        MockKAnnotations.init(this)
        manageAddressViewModel = ManageAddressViewModel(getPeopleAddressUseCase, deletePeopleAddressUseCase, setDetaultPeopleAddressUseCase)
    }

    @Test
    fun `Search Address Success`() {
        val response = AddressListModel()
        every { getPeopleAddressUseCase.getAll(any()) } returns Observable.just(response).doOnSubscribe {
            Assert.assertEquals(ManageAddressState.Loading, manageAddressViewModel.addressList.value)
        }

        manageAddressViewModel.searchAddress("")

        Assert.assertEquals(ManageAddressState.Success(response), manageAddressViewModel.addressList.value)
    }

    @Test
    fun `Search Address Failed`() {
        val response = Throwable()
        every { getPeopleAddressUseCase.getAll(any()) } returns Observable.error(response)

        manageAddressViewModel.searchAddress("")
        Assert.assertEquals(ManageAddressState.Fail(response, ""), manageAddressViewModel.addressList.value)
    }

    @Test
    fun `Set Default Address Success`() {
        every {
            setDetaultPeopleAddressUseCase.execute(any(), any(), any())
        } answers  {
            Assert.assertEquals(ManageAddressState.Loading, manageAddressViewModel.result.value)
            (secondArg() as ((String) -> Unit)).invoke(success)
        }

        manageAddressViewModel.setDefaultPeopleAddress("1")

        Assert.assertEquals(ManageAddressState.Success(success), manageAddressViewModel.result.value)
    }

    @Test
    fun `Set Default Address Fail`() {
        val response = Throwable()
        every {
            setDetaultPeopleAddressUseCase.execute(any(), any(), any())
        } answers {
            Assert.assertEquals(ManageAddressState.Loading, manageAddressViewModel.result.value)
            (thirdArg() as ((Throwable) -> Unit)).invoke(response)
        }

        manageAddressViewModel.setDefaultPeopleAddress("1")

        Assert.assertEquals(ManageAddressState.Fail(response, ""), manageAddressViewModel.addressList.value)
    }
}