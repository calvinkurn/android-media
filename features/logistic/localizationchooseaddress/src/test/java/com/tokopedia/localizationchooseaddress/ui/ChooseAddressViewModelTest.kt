package com.tokopedia.localizationchooseaddress.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.localizationchooseaddress.data.repository.ChooseAddressRepository
import com.tokopedia.localizationchooseaddress.domain.mapper.ChooseAddressMapper
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressList
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.model.DefaultChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.response.GetChosenAddressListQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressGqlResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.SetStateChosenAddressQqlResponse
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ChooseAddressViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val chooseAddressRepo: ChooseAddressRepository = mockk(relaxed = true)
    private val chooseAddressMapper = ChooseAddressMapper()

    private lateinit var chooseAddressViewModel: ChooseAddressViewModel

    private val chosenAddressListObserver: Observer<Result<List<ChosenAddressList>>> = mockk(relaxed = true)
    private val setChosenAddressObserver: Observer<Result<ChosenAddressModel>> = mockk(relaxed = true)
    private val getChosenAddressObserver: Observer<Result<ChosenAddressModel>> = mockk(relaxed = true)
    private val getDefaultAddressObserver: Observer<Result<DefaultChosenAddressModel>> = mockk(relaxed = true)

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        chooseAddressViewModel = ChooseAddressViewModel(chooseAddressRepo, chooseAddressMapper)
        chooseAddressViewModel.chosenAddressList.observeForever(chosenAddressListObserver)
        chooseAddressViewModel.setChosenAddress.observeForever(setChosenAddressObserver)
        chooseAddressViewModel.getChosenAddress.observeForever(getChosenAddressObserver)
        chooseAddressViewModel.getDefaultAddress.observeForever(getDefaultAddressObserver)
    }

    @Test
    fun `Get Chosen Address List Success`() {
        coEvery { chooseAddressRepo.getChosenAddressList(any()) } returns GetChosenAddressListQglResponse()
        chooseAddressViewModel.getChosenAddressList("address")
        verify { chosenAddressListObserver.onChanged(match { it is Success }) }

    }

    @Test
    fun `Get Chosen Address List Fail`() {
        coEvery { chooseAddressRepo.getChosenAddressList(any()) } throws defaultThrowable
        chooseAddressViewModel.getChosenAddressList("address")
        verify { chosenAddressListObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Set Chosen Address Success`() {
        coEvery { chooseAddressRepo.setStateChosenAddress(any(), any(), any(), any(), any(), any(), any(), any()) } returns SetStateChosenAddressQqlResponse()
        chooseAddressViewModel.setStateChosenAddress(3, "11234", "Hutagalung", "Rumah", "-6.22119739999998", "106.81941940000002", "2270", "12950")
        verify { setChosenAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Set Chosen Address Fail`() {
        coEvery { chooseAddressRepo.setStateChosenAddress(any(), any(), any(), any(), any(), any(), any(), any()) } throws defaultThrowable
        chooseAddressViewModel.setStateChosenAddress(3, "11234", "Hutagalung", "Rumah", "-6.22119739999998", "106.81941940000002", "2270", "12950")
        verify { setChosenAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Get Chosen Address Success`() {
        coEvery { chooseAddressRepo.getStateChosenAddress(any()) } returns GetStateChosenAddressQglResponse()
        chooseAddressViewModel.getStateChosenAddress("address")
        verify { getChosenAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Chosen Address Fail`() {
        coEvery { chooseAddressRepo.getStateChosenAddress(any()) } throws defaultThrowable
        chooseAddressViewModel.getStateChosenAddress("address")
        verify { getChosenAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Get Default Chosen Address Success`() {
        coEvery { chooseAddressRepo.getDefaultChosenAddress(any(), any()) } returns GetDefaultChosenAddressGqlResponse()
        chooseAddressViewModel.getDefaultChosenAddress("-6.22119739999998,106.81941940000002", "address")
        verify { getDefaultAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Default Chosen Address Fail`() {
        coEvery { chooseAddressRepo.getDefaultChosenAddress(any(), any()) } throws defaultThrowable
        chooseAddressViewModel.getDefaultChosenAddress("-6.22119739999998,106.81941940000002", "address")
        verify { getDefaultAddressObserver.onChanged(match { it is Fail }) }
    }
}