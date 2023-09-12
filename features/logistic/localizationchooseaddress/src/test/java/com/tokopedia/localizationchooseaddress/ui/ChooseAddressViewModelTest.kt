package com.tokopedia.localizationchooseaddress.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.localizationchooseaddress.data.repository.ChooseAddressRepository
import com.tokopedia.localizationchooseaddress.domain.mapper.ChooseAddressMapper
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressList
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.model.DefaultChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.model.StateChooseAddressParam
import com.tokopedia.localizationchooseaddress.domain.response.GetChosenAddressListQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressGqlResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.RefreshTokonowDataResponse
import com.tokopedia.localizationchooseaddress.domain.response.SetStateChosenAddressQqlResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.RefreshTokonowDataUsecase
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
    private val refreshTokonowDataUseCase: RefreshTokonowDataUsecase = mockk(relaxed = true)

    private lateinit var chooseAddressViewModel: ChooseAddressViewModel

    private val chosenAddressListObserver: Observer<Result<List<ChosenAddressList>>> = mockk(relaxed = true)
    private val setChosenAddressObserver: Observer<Result<ChosenAddressModel>> = mockk(relaxed = true)
    private val getChosenAddressObserver: Observer<Result<ChosenAddressModel>> = mockk(relaxed = true)
    private val getDefaultAddressObserver: Observer<Result<DefaultChosenAddressModel>> = mockk(relaxed = true)
    private val tokonowDataObserver: Observer<Result<RefreshTokonowDataResponse.Data.RefreshTokonowData.RefreshTokonowDataSuccess>> = mockk(relaxed = true)

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        chooseAddressViewModel = ChooseAddressViewModel(chooseAddressRepo, chooseAddressMapper, refreshTokonowDataUseCase)
        chooseAddressViewModel.chosenAddressList.observeForever(chosenAddressListObserver)
        chooseAddressViewModel.setChosenAddress.observeForever(setChosenAddressObserver)
        chooseAddressViewModel.getChosenAddress.observeForever(getChosenAddressObserver)
        chooseAddressViewModel.getDefaultAddress.observeForever(getDefaultAddressObserver)
        chooseAddressViewModel.tokonowData.observeForever(tokonowDataObserver)
    }

    @Test
    fun `Get Chosen Address List Success`() {
        coEvery { chooseAddressRepo.getChosenAddressList(any(), any()) } returns GetChosenAddressListQglResponse()
        chooseAddressViewModel.getChosenAddressList("address", true)
        verify { chosenAddressListObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Chosen Address List Fail`() {
        coEvery { chooseAddressRepo.getChosenAddressList(any(), any()) } throws defaultThrowable
        chooseAddressViewModel.getChosenAddressList("address", true)
        verify { chosenAddressListObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Set Chosen Address Success`() {
        val model = StateChooseAddressParam(
            3, 11234, "Hutagalung", "Rumah",
            "-6.22119739999998", "106.81941940000002", 2270,
            "12950", false
        )
        coEvery { chooseAddressRepo.setStateChosenAddress(any()) } returns SetStateChosenAddressQqlResponse()
        chooseAddressViewModel.setStateChosenAddress(model)
        verify { setChosenAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Set Chosen Address Fail`() {
        val model = StateChooseAddressParam(
            3, 11234, "Hutagalung", "Rumah",
            "-6.22119739999998", "106.81941940000002", 2270,
            "12950", false
        )
        coEvery { chooseAddressRepo.setStateChosenAddress(any()) } throws defaultThrowable
        chooseAddressViewModel.setStateChosenAddress(model)
        verify { setChosenAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Get Chosen Address Success`() {
        coEvery { chooseAddressRepo.getStateChosenAddress(any(), any()) } returns GetStateChosenAddressQglResponse()
        chooseAddressViewModel.getStateChosenAddress("address", false)
        verify { getChosenAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Chosen Address Fail`() {
        coEvery { chooseAddressRepo.getStateChosenAddress(any(), any()) } throws defaultThrowable
        chooseAddressViewModel.getStateChosenAddress("address", false)
        verify { getChosenAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Get Default Chosen Address Success`() {
        coEvery { chooseAddressRepo.getDefaultChosenAddress(any(), any(), any()) } returns GetDefaultChosenAddressGqlResponse()
        chooseAddressViewModel.getDefaultChosenAddress("-6.22119739999998,106.81941940000002", "address", false)
        verify { getDefaultAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Default Chosen Address Fail`() {
        coEvery { chooseAddressRepo.getDefaultChosenAddress(any(), any(), any()) } throws defaultThrowable
        chooseAddressViewModel.getDefaultChosenAddress("-6.22119739999998,106.81941940000002", "address", false)
        verify { getDefaultAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Refresh Tokonow Data Success`() {
        coEvery { refreshTokonowDataUseCase.execute(any()) } returns RefreshTokonowDataResponse.Data()
        chooseAddressViewModel.getTokonowData(LocalCacheModel())
        verify { tokonowDataObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Refresh Tokonow Data Fail`() {
        coEvery { refreshTokonowDataUseCase.execute(any()) } throws defaultThrowable
        chooseAddressViewModel.getTokonowData(LocalCacheModel())
        verify { tokonowDataObserver.onChanged(match { it is Fail }) }
    }
}
