package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.response.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddressFormViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repo: KeroRepository = mockk(relaxed = true)
    private val saveAddressDataModel = SaveAddressDataModel()

    private val districtDetailObserver: Observer<Result<KeroDistrictRecommendation>> = mockk(relaxed = true)
    private val saveAddressObserver: Observer<Result<DataAddAddress>> = mockk(relaxed = true)
    private val defaultAddressObserver: Observer<Result<DefaultAddressData>> = mockk(relaxed = true)

    private lateinit var addressFormViewModel: AddressFormViewModel

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        addressFormViewModel = AddressFormViewModel(repo)
        addressFormViewModel.districtDetail.observeForever(districtDetailObserver)
        addressFormViewModel.saveAddress.observeForever(saveAddressObserver)
        addressFormViewModel.defaultAddress.observeForever(defaultAddressObserver)
    }

    @Test
    fun `Get District Detail Success`() {
        coEvery { repo.getZipCode(any()) } returns GetDistrictDetailsResponse()
        addressFormViewModel.getDistrictDetail("123")
        verify { districtDetailObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get District Detail Fail`() {
        coEvery { repo.getZipCode(any()) } throws defaultThrowable
        addressFormViewModel.getDistrictDetail("123")
        verify { districtDetailObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Get Default Address Success`() {
        coEvery { repo.getDefaultAddress(any()) } returns GetDefaultAddressResponse()
        addressFormViewModel.getDefaultAddress("address")
        verify { defaultAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Default Address Fail`() {
        coEvery { repo.getDefaultAddress(any()) } throws defaultThrowable
        addressFormViewModel.getDefaultAddress("address")
        verify { defaultAddressObserver.onChanged(match { it is Fail }) }
    }


    @Test
    fun `Save Address Data Success`() {
        coEvery { repo.saveAddress(any()) } returns AddAddressResponse()
        addressFormViewModel.saveAddress(saveAddressDataModel)
        verify { saveAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Save Address Data Fail`() {
        coEvery { repo.saveAddress(any()) } throws defaultThrowable
        addressFormViewModel.saveAddress(saveAddressDataModel)
        verify { saveAddressObserver.onChanged(match { it is Fail }) }
    }

}