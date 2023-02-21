package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AddressFormViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repo: KeroRepository = mockk(relaxed = true)
    private val saveAddressDataModel = SaveAddressDataModel()
    private val addressId = "12345"

    private val districtDetailObserver: Observer<Result<KeroDistrictRecommendation>> = mockk(relaxed = true)
    private val saveAddressObserver: Observer<Result<DataAddAddress>> = mockk(relaxed = true)
    private val defaultAddressObserver: Observer<Result<DefaultAddressData>> = mockk(relaxed = true)
    private val editAddressObserver: Observer<Result<KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse>> = mockk(relaxed = true)
    private val detailAddressObserver: Observer<Result<KeroGetAddressResponse.Data>> = mockk(relaxed = true)
    private val pinpointValidationObserver: Observer<Result<PinpointValidationResponse.PinpointValidations.PinpointValidationResponseData>> = mockk(relaxed = true)

    private lateinit var addressFormViewModel: AddressFormViewModel

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        addressFormViewModel = AddressFormViewModel(repo)
        addressFormViewModel.districtDetail.observeForever(districtDetailObserver)
        addressFormViewModel.saveAddress.observeForever(saveAddressObserver)
        addressFormViewModel.defaultAddress.observeForever(defaultAddressObserver)
        addressFormViewModel.editAddress.observeForever(editAddressObserver)
        addressFormViewModel.addressDetail.observeForever(detailAddressObserver)
        addressFormViewModel.pinpointValidation.observeForever(pinpointValidationObserver)
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
        coEvery { repo.saveAddress(any(), any()) } returns AddAddressResponse()
        addressFormViewModel.saveAddress(saveAddressDataModel)
        verify { saveAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Save Address Data Fail`() {
        coEvery { repo.saveAddress(any(), any()) } throws defaultThrowable
        addressFormViewModel.saveAddress(saveAddressDataModel)
        verify { saveAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Get Address Detail Data Success`() {
        coEvery { repo.getAddressDetail(any(), any()) } returns KeroGetAddressResponse.Data()
        addressFormViewModel.getAddressDetail(addressId)
        verify { detailAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Address Detail Data Fail`() {
        coEvery { repo.getAddressDetail(any(), any()) } throws defaultThrowable
        addressFormViewModel.getAddressDetail(addressId)
        verify { detailAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Save Edit Address Data Success`() {
        coEvery { repo.editAddress(any(), any()) } returns KeroEditAddressResponse.Data()
        addressFormViewModel.saveEditAddress(saveAddressDataModel)
        verify { editAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Save Edit Address Data Fail`() {
        coEvery { repo.editAddress(any(), any()) } throws defaultThrowable
        addressFormViewModel.saveEditAddress(saveAddressDataModel)
        verify { editAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Pinpoint Validation Data Success`() {
        coEvery { repo.pinpointValidation(any(), any(), any(), any()) } returns PinpointValidationResponse()
        addressFormViewModel.validatePinpoint(saveAddressDataModel)
        verify { pinpointValidationObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Pinpoint Validation Data Fail`() {
        coEvery { repo.pinpointValidation(any(), any(), any(), any()) } throws defaultThrowable
        addressFormViewModel.validatePinpoint(saveAddressDataModel)
        verify { pinpointValidationObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `verify when set page source is correctly`() {
        val source = "source"

        addressFormViewModel.source = source

        Assert.assertEquals(addressFormViewModel.source, source)
    }

    @Test
    fun `verify save edit address data success from tokonow`() {
        coEvery { repo.editAddress(any(), any()) } returns KeroEditAddressResponse.Data()
        addressFormViewModel.source = ManageAddressSource.TOKONOW.source
        addressFormViewModel.saveEditAddress(saveAddressDataModel)
        verify { editAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `verify set gms availability flag is correct`() {
        val gmsAvailable = true
        addressFormViewModel.isGmsAvailable = gmsAvailable

        Assert.assertEquals(addressFormViewModel.isGmsAvailable, gmsAvailable)
    }
}
