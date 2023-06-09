package com.tokopedia.logisticaddaddress.features.pinpoint.webview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticaddaddress.features.pinpoint.webview.analytics.AddAddressPinpointTracker
import com.tokopedia.logisticaddaddress.features.pinpoint.webview.analytics.EditAddressPinpointTracker
import com.tokopedia.logisticaddaddress.helper.KeroMapsAutofillProvider
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
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
    private val liveDataObserver: Observer<PinpointWebviewState> = mockk(relaxed = true)

    private lateinit var viewModel: PinpointWebviewViewModel

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = PinpointWebviewViewModel(repo)
        viewModel.pinpointState.observeForever(liveDataObserver)
        every { liveDataObserver.onChanged(any()) } just Runs
    }

    @Test
    fun `WHEN saveLatLong with locationPass THEN should update locationPass with new data`() {
        val data = KeroMapsAutofillProvider.provideAutofillResponse()
        val lat = -6.571004247136069
        val long = 106.76730046907339
        val locationPass = LocationPass()
        viewModel.setLocationPass(locationPass)
        coEvery { repo.getDistrictGeocode(any()) } returns data

        viewModel.saveLatLong(lat, long)
        val result = viewModel.pinpointState.value

        assertTrue(result is PinpointWebviewState.AddressDetailResult.Success)
        val actual = result as PinpointWebviewState.AddressDetailResult.Success
        assertTrue(actual.locationPass?.districtName == data.keroMapsAutofill.data.districtName)
        assertTrue(actual.locationPass?.cityName == data.keroMapsAutofill.data.cityName)
        assertTrue(actual.locationPass?.latitude == data.keroMapsAutofill.data.latitude)
        assertTrue(actual.locationPass?.longitude == data.keroMapsAutofill.data.longitude)
    }

    @Test
    fun `WHEN saveLatLong with saveAddressDataModel THEN should update saveAddressDataModel with new data`() {
        val data = KeroMapsAutofillProvider.provideAutofillResponse()
        val lat = -6.571004247136069
        val long = 106.76730046907339
        val addressData = SaveAddressDataModel()
        viewModel.setAddressDataModel(addressData)
        coEvery { repo.getDistrictGeocode(any()) } returns data

        viewModel.saveLatLong(lat, long)
        val result = viewModel.pinpointState.value

        assertTrue(result is PinpointWebviewState.AddressDetailResult.Success)
        val actual = result as PinpointWebviewState.AddressDetailResult.Success
        assertTrue(actual.saveAddressDataModel?.districtName == data.keroMapsAutofill.data.districtName)
        assertTrue(actual.saveAddressDataModel?.cityName == data.keroMapsAutofill.data.cityName)
        assertTrue(actual.saveAddressDataModel?.latitude == data.keroMapsAutofill.data.latitude)
        assertTrue(actual.saveAddressDataModel?.longitude == data.keroMapsAutofill.data.longitude)
        assertTrue(actual.saveAddressDataModel?.districtId == data.keroMapsAutofill.data.districtId)
        assertTrue(actual.saveAddressDataModel?.cityId == data.keroMapsAutofill.data.cityId)
        assertTrue(actual.saveAddressDataModel?.provinceName == data.keroMapsAutofill.data.provinceName)
        assertTrue(actual.saveAddressDataModel?.provinceId == data.keroMapsAutofill.data.provinceId)
        assertTrue(actual.saveAddressDataModel?.postalCode == data.keroMapsAutofill.data.postalCode)
        assertTrue(actual.saveAddressDataModel?.address2 == "${data.keroMapsAutofill.data.latitude},${data.keroMapsAutofill.data.longitude}")
        assertTrue(actual.saveAddressDataModel?.selectedDistrict == data.keroMapsAutofill.data.formattedAddress)
    }

    @Test
    fun `WHEN saveLatLong THEN should return lat long from BE`() {
        val data = KeroMapsAutofillProvider.provideAutofillResponse()
        val lat = -6.571004247136069
        val long = 106.76730046907339
        val addressData = SaveAddressDataModel()
        viewModel.setAddressDataModel(addressData)
        coEvery { repo.getDistrictGeocode(any()) } returns data

        viewModel.saveLatLong(lat, long)
        val result = viewModel.pinpointState.value

        assertTrue(result is PinpointWebviewState.AddressDetailResult.Success)
        val actual = result as PinpointWebviewState.AddressDetailResult.Success
        assertTrue(actual.latitude == data.keroMapsAutofill.data.latitude.toDouble())
        assertTrue(actual.longitude == data.keroMapsAutofill.data.longitude.toDouble())
    }

    @Test
    fun `WHEN saveLatLong failed THEN should return error`() {
        val lat = -6.571004247136069
        val long = 106.76730046907339
        coEvery { repo.getDistrictGeocode(any()) } throws defaultThrowable

        viewModel.saveLatLong(lat, long)
        val result = viewModel.pinpointState.value

        assertTrue(result is PinpointWebviewState.AddressDetailResult.Fail)
    }

    @Test
    fun `WHEN saveLatLong success in add address positive THEN should hit analytic`() {
        val data = KeroMapsAutofillProvider.provideAutofillResponse()
        val lat = -6.571004247136069
        val long = 106.76730046907339
        viewModel.setSource("ADD_ADDRESS_POSITIVE")
        coEvery { repo.getDistrictGeocode(any(), any()) } returns data

        viewModel.saveLatLong(lat, long)

        verifySequence {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickPilihLokasiPositive
                    )
                }
            )
            liveDataObserver.onChanged(match { state -> state is PinpointWebviewState.AddressDetailResult.Success })
        }
    }

    @Test
    fun `WHEN saveLatLong success in add address negative THEN should hit analytic`() {
        val data = KeroMapsAutofillProvider.provideAutofillResponse()
        val lat = -6.571004247136069
        val long = 106.76730046907339
        viewModel.setSource("ADD_ADDRESS_NEGATIVE")
        coEvery { repo.getDistrictGeocode(any(), any()) } returns data

        viewModel.saveLatLong(lat, long)

        verifySequence {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickPilihLokasiNegative,
                        "success"
                    )
                }
            )
            liveDataObserver.onChanged(match { state -> state is PinpointWebviewState.AddressDetailResult.Success })
        }
    }

    @Test
    fun `WHEN saveLatLong success in edit address THEN should hit analytic`() {
        val data = KeroMapsAutofillProvider.provideAutofillResponse()
        val lat = -6.571004247136069
        val long = 106.76730046907339
        viewModel.setSource("EDIT_ADDRESS")
        coEvery { repo.getDistrictGeocode(any(), any()) } returns data

        viewModel.saveLatLong(lat, long)

        verifySequence {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.EditAddress(
                        EditAddressPinpointTracker.ClickPilihLokasiIni
                    )
                }
            )
            liveDataObserver.onChanged(match { state -> state is PinpointWebviewState.AddressDetailResult.Success })
        }
    }

    @Test
    fun `WHEN saveLatLong failed in add address negative THEN should hit analytic`() {
        val lat = -6.571004247136069
        val long = 106.76730046907339
        viewModel.setSource("ADD_ADDRESS_NEGATIVE")
        coEvery { repo.getDistrictGeocode(any()) } throws defaultThrowable

        viewModel.saveLatLong(lat, long)

        verifySequence {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickPilihLokasiNegative,
                        "not success"
                    )
                }
            )
            liveDataObserver.onChanged(match { state -> state is PinpointWebviewState.AddressDetailResult.Fail })
        }
    }

    @Test
    fun `WHEN sendTracker with invalid id THEN dont hit analytic`() {
        viewModel.setSource("EDIT_ADDRESS")
        val invalidTrackerId = "33333"

        viewModel.sendTracker(invalidTrackerId, null)

        assertTrue(invalidTrackerId !in EditAddressPinpointTracker.values().map { it.trackerId })
        verify(exactly = 0) {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.EditAddress(
                        EditAddressPinpointTracker.ClickPilihLokasiIni
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN sendTracker and source is empty string THEN dont hit tracker`() {
        viewModel.setSource("")

        viewModel.sendTracker("29688", null)

        verify(exactly = 0) {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.EditAddress(
                        EditAddressPinpointTracker.ClickPilihLokasiIni
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN sendTracker and source is invalid enum THEN dont hit tracker`() {
        viewModel.setSource("edit-address")

        viewModel.sendTracker("29688", null)

        verify(exactly = 0) {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.EditAddress(
                        EditAddressPinpointTracker.ClickPilihLokasiIni
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN saveLatLong success in add address positive flow with empty string THEN dont hit tracker`() {
        viewModel.setSource("")

        val data = KeroMapsAutofillProvider.provideAutofillResponse()
        val lat = -6.571004247136069
        val long = 106.76730046907339
        coEvery { repo.getDistrictGeocode(any()) } returns data

        viewModel.saveLatLong(lat, long)

        verify(exactly = 0) {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickPilihLokasiPositive
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN saveLatLong success in edit address with wrong enum THEN dont hit tracker`() {
        viewModel.setSource("edit-address")

        val data = KeroMapsAutofillProvider.provideAutofillResponse()
        val lat = -6.571004247136069
        val long = 106.76730046907339
        coEvery { repo.getDistrictGeocode(any()) } returns data

        viewModel.saveLatLong(lat, long)

        verify(exactly = 0) {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.EditAddress(
                        EditAddressPinpointTracker.ClickPilihLokasiIni
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN saveLatLong failed in add address positive flow THEN dont hit tracker`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        val lat = -6.571004247136069
        val long = 106.76730046907339
        coEvery { repo.getDistrictGeocode(any()) } throws defaultThrowable

        viewModel.saveLatLong(lat, long)

        verify(exactly = 0) {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickPilihLokasiNegative,
                        "not success"
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN saveLatLong failed in but no source specified THEN dont hit tracker`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("29688", null)

        val data = KeroMapsAutofillProvider.provideAutofillResponse()
        val lat = -6.571004247136069
        val long = 106.76730046907339
        coEvery { repo.getDistrictGeocode(any()) } returns data

        viewModel.saveLatLong(lat, long)

        verify(exactly = 0) {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickPilihLokasiNegative,
                        "not success"
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN finishWithoutSaveChanges on add address THEN should send analytic first`() {
        viewModel.setSource("ADD_ADDRESS_NEGATIVE")

        viewModel.finishWithoutSaveChanges()

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickBackArrowPinpoint
                    )
                }
            )

            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.FinishActivity
                }
            )
        }
    }

    @Test
    fun `WHEN finishWithoutSaveChanges on edit address THEN should send analytic first`() {
        viewModel.setSource("EDIT_ADDRESS")

        viewModel.finishWithoutSaveChanges()

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.EditAddress(
                        EditAddressPinpointTracker.ClickBackArrowPinpoint
                    )
                }
            )

            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.FinishActivity
                }
            )
        }
    }

    @Test
    fun `WHEN finishWithoutSaveChanges THEN should finish pinpoint activity`() {
        viewModel.finishWithoutSaveChanges()

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.FinishActivity
                }
            )
        }
    }
}
