package com.tokopedia.manageaddress.ui.manageaddress

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.localizationchooseaddress.data.repository.ChooseAddressRepository
import com.tokopedia.localizationchooseaddress.domain.mapper.ChooseAddressMapper
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.SetStateChosenAddressQqlResponse
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.manageaddress.domain.DeletePeopleAddressUseCase
import com.tokopedia.manageaddress.domain.SetDefaultPeopleAddressUseCase
import com.tokopedia.manageaddress.domain.model.ManageAddressState
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable

@ExperimentalCoroutinesApi
class ManageAddressViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val success = "Success"

    private val getPeopleAddressUseCase: GetAddressCornerUseCase = mockk(relaxed = true)
    private val deletePeopleAddressUseCase: DeletePeopleAddressUseCase = mockk(relaxed = true)
    private val setDetaultPeopleAddressUseCase: SetDefaultPeopleAddressUseCase = mockk(relaxed = true)
    private val chooseAddressRepo: ChooseAddressRepository = mockk(relaxed = true)
    private val chooseAddressMapper: ChooseAddressMapper = mockk(relaxed = true)
    private val chosenAddressObserver: Observer<Result<ChosenAddressModel>> = mockk(relaxed = true)

    private lateinit var manageAddressViewModel: ManageAddressViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        manageAddressViewModel = ManageAddressViewModel(getPeopleAddressUseCase, deletePeopleAddressUseCase, setDetaultPeopleAddressUseCase, chooseAddressRepo, chooseAddressMapper)
        manageAddressViewModel.getChosenAddress.observeForever(chosenAddressObserver)
        manageAddressViewModel.setChosenAddress.observeForever(chosenAddressObserver)
    }

    @Test
    fun `Search Address Success`() {
        val response = AddressListModel()
        every { getPeopleAddressUseCase.execute(any(), any(), any(), any()) } returns Observable.just(response).doOnSubscribe {
            assertEquals(ManageAddressState.Loading, manageAddressViewModel.addressList.value)
        }

        manageAddressViewModel.searchAddress("", -1, -1, true)

        assertEquals(ManageAddressState.Success(response), manageAddressViewModel.addressList.value)
    }

    @Test
    fun `Search Address Failed`() {
        val response = Throwable()
        every { getPeopleAddressUseCase.execute(any(), any(), any(), any()) } returns Observable.error(response)

        manageAddressViewModel.searchAddress("", -1, -1, true)

        assertEquals(ManageAddressState.Fail(response, ""), manageAddressViewModel.addressList.value)
    }

    @Test
    fun `Load More Address Success`() {
        val response = AddressListModel()
        every { getPeopleAddressUseCase.loadMore(any(), any(), any(), any(), any()) } returns Observable.just(response).doOnSubscribe {
            assertEquals(ManageAddressState.Loading, manageAddressViewModel.addressList.value)
        }

        manageAddressViewModel.loadMore(-1, -1, true)

        assertEquals(ManageAddressState.Success(response), manageAddressViewModel.addressList.value)
    }

    @Test
    fun `Load More Address Failed`() {
        val response = Throwable()
        every { getPeopleAddressUseCase.loadMore(any(), any(), any(), any(), any()) } returns Observable.error(response)

        manageAddressViewModel.loadMore(-1, -1, true)

        assertEquals(ManageAddressState.Fail(response, ""), manageAddressViewModel.addressList.value)
    }

    @Test
    fun `Set Default Address Success`() {
        every {
            setDetaultPeopleAddressUseCase.execute(any(), any(), any(), any())
        } answers  {
            (thirdArg() as ((String) -> Unit)).invoke(success)
        }

        manageAddressViewModel.setDefaultPeopleAddress("1", true, -1, -1, true)

        assertEquals(ManageAddressState.Success(success), manageAddressViewModel.setDefault.value)
    }

    @Test
    fun `Set Default Address Fail`() {
        val response = Throwable()
        every {
            setDetaultPeopleAddressUseCase.execute(any(), any(), any(), any())
        } answers {
            (lastArg() as ((Throwable) -> Unit)).invoke(response)
        }

        manageAddressViewModel.setDefaultPeopleAddress("1", false, 0, 0, true)

        assertEquals(ManageAddressState.Fail(response, ""), manageAddressViewModel.setDefault.value)
    }

    @Test
    fun `Delete Address Success`() {
        every {
            deletePeopleAddressUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(ManageAddressState.Loading, manageAddressViewModel.result.value)
            (secondArg() as ((String) -> Unit)).invoke(success)
        }

        manageAddressViewModel.deletePeopleAddress("1", -1, -1, true)

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

        manageAddressViewModel.deletePeopleAddress("1", -1, -1, true)

        assertEquals(ManageAddressState.Fail(response, ""), manageAddressViewModel.addressList.value)
    }

    @Test
    fun `Get Chosen Address Success`() {
        coEvery { chooseAddressRepo.getStateChosenAddress(any()) } returns GetStateChosenAddressQglResponse()
        manageAddressViewModel.getStateChosenAddress("address")
        verify { chosenAddressObserver.onChanged(match { it is Success }) }
    }


    @Test
    fun `Get Chosen Address Fail`() {
        coEvery { chooseAddressRepo.getStateChosenAddress(any()) } throws Throwable("test error")
        manageAddressViewModel.getStateChosenAddress("address")
        verify { chosenAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Set Chosen Address Success`() {
        val model = RecipientAddressModel()
        coEvery { chooseAddressRepo.setStateChosenAddressFromAddress(any()) } returns SetStateChosenAddressQqlResponse()
        manageAddressViewModel.setStateChosenAddress(model)
        verify { chosenAddressObserver.onChanged(match { it is Success }) }
    }


    @Test
    fun `Set Chosen Address Fail`() {
        val model = RecipientAddressModel()
        coEvery { chooseAddressRepo.setStateChosenAddressFromAddress(any()) } throws Throwable("test error")
        manageAddressViewModel.setStateChosenAddress(model)
        verify { chosenAddressObserver.onChanged(match { it is Fail }) }
    }


}