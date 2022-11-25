package com.tokopedia.logisticaddaddress.features.pinpoint.webview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticaddaddress.domain.mapper.SaveAddressMapper
import com.tokopedia.logisticaddaddress.helper.KeroMapsAutofillProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PinpointWebviewViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repo: KeroRepository = mockk(relaxed = true)

    private lateinit var viewModel: PinpointWebviewViewModel

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = PinpointWebviewViewModel(repo, SaveAddressMapper())
    }

    @Test
    fun `WHEN saveLatLong with locationPass THEN should update locationPass with new data`() {
        val data = KeroMapsAutofillProvider.provideAutofillResponse()
        val lat = -6.571004247136069
        val long = 106.76730046907339
        val locationPass = LocationPass()
        viewModel.locationPass = locationPass
        coEvery { repo.getDistrictGeocode(any()) } returns data

        viewModel.saveLatLong(lat, long)

        assertTrue(viewModel.locationPass?.districtName == data.keroMapsAutofill.data.districtName)
        assertTrue(viewModel.locationPass?.cityName == data.keroMapsAutofill.data.cityName)
        assertTrue(viewModel.locationPass?.latitude == data.keroMapsAutofill.data.latitude)
        assertTrue(viewModel.locationPass?.longitude == data.keroMapsAutofill.data.longitude)
    }

    @Test
    fun `WHEN saveLatLong with saveAddressDataModel THEN should update saveAddressDataModel with new data`() {
        val data = KeroMapsAutofillProvider.provideAutofillResponse()
        val lat = -6.571004247136069
        val long = 106.76730046907339
        val addressData = SaveAddressDataModel()
        viewModel.saveAddressDataModel = addressData
        coEvery { repo.getDistrictGeocode(any()) } returns data

        viewModel.saveLatLong(lat, long)

        assertTrue(viewModel.saveAddressDataModel?.districtName == data.keroMapsAutofill.data.districtName)
        assertTrue(viewModel.saveAddressDataModel?.cityName == data.keroMapsAutofill.data.cityName)
        assertTrue(viewModel.saveAddressDataModel?.latitude == data.keroMapsAutofill.data.latitude)
        assertTrue(viewModel.saveAddressDataModel?.longitude == data.keroMapsAutofill.data.longitude)
        assertTrue(viewModel.saveAddressDataModel?.districtId == data.keroMapsAutofill.data.districtId)
        assertTrue(viewModel.saveAddressDataModel?.cityId == data.keroMapsAutofill.data.cityId)
        assertTrue(viewModel.saveAddressDataModel?.provinceName == data.keroMapsAutofill.data.provinceName)
        assertTrue(viewModel.saveAddressDataModel?.provinceId == data.keroMapsAutofill.data.provinceId)
        assertTrue(viewModel.saveAddressDataModel?.postalCode == data.keroMapsAutofill.data.postalCode)
        assertTrue(viewModel.saveAddressDataModel?.address2 == "${data.keroMapsAutofill.data.latitude},${data.keroMapsAutofill.data.longitude}")
        assertTrue(viewModel.saveAddressDataModel?.selectedDistrict == data.keroMapsAutofill.data.formattedAddress)
    }

    @Test
    fun `WHEN saveLatLong THEN should return lat long from BE`() {
        val data = KeroMapsAutofillProvider.provideAutofillResponse()
        val lat = -6.571004247136069
        val long = 106.76730046907339
        val addressData = SaveAddressDataModel()
        viewModel.saveAddressDataModel = addressData
        coEvery { repo.getDistrictGeocode(any()) } returns data

        viewModel.saveLatLong(lat, long)

        val result = viewModel.pinpointState.value

        assertTrue(
            result == Success(
                Pair(
                    data.keroMapsAutofill.data.latitude.toDouble(),
                    data.keroMapsAutofill.data.longitude.toDouble()
                )
            )
        )
    }

    @Test
    fun `WHEN saveLatLong failed THEN should return error`() {
        val lat = -6.571004247136069
        val long = 106.76730046907339
        coEvery { repo.getDistrictGeocode(any()) } throws defaultThrowable

        viewModel.saveLatLong(lat, long)

        val result = viewModel.pinpointState.value

        assertTrue(result is Fail)
    }
}
