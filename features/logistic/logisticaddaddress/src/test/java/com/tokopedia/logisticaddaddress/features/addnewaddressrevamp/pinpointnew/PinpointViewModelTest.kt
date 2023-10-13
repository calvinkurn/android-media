package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictBoundariesUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictCenterUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictGeoCodeUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.usecase.MapsGeocodeUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.DistrictBoundaryResponseUiModel
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.PinpointViewModel
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.ChoosePinpoint
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.MoveMap
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.PinpointAction
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.PinpointBottomSheetState
import com.tokopedia.usecase.coroutines.Result
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
open class PinpointViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val getDistrict: GetDistrictUseCase = mockk(relaxed = true)
    private val getDistrictBoundaries: GetDistrictBoundariesUseCase = mockk(relaxed = true)
    private val getDistrictCenter: GetDistrictCenterUseCase = mockk(relaxed = true)
    private val getDistrictGeoCode: GetDistrictGeoCodeUseCase = mockk(relaxed = true)
    private val mapsGeocodeUseCase = mockk<MapsGeocodeUseCase>(relaxed = true)
    private val getDistrictMapper = GetDistrictMapper()
    private val districtBoundaryMapper = DistrictBoundaryMapper()

    private lateinit var viewModel: PinpointViewModel

    private val districtBoundaryObserver: Observer<Result<DistrictBoundaryResponseUiModel>> =
        mockk(relaxed = true)
    private val actionObserver: Observer<PinpointAction> = mockk(relaxed = true)
    private val choosePinpointObserver: Observer<ChoosePinpoint> = mockk(relaxed = true)
    private val pinpointBottomsheetObserver: Observer<PinpointBottomSheetState> = mockk(relaxed = true)
    private val mapObserver: Observer<MoveMap> = mockk(relaxed = true)

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel =
            PinpointViewModel(
                getDistrict,
                getDistrictBoundaries,
                getDistrictCenter,
                getDistrictGeoCode,
                getDistrictMapper,
                districtBoundaryMapper,
                mapsGeocodeUseCase
            )
        viewModel.action.observeForever(actionObserver)
        viewModel.choosePinpoint.observeForever(choosePinpointObserver)
        viewModel.districtBoundary.observeForever(districtBoundaryObserver)
        viewModel.pinpointBottomSheet.observeForever(pinpointBottomsheetObserver)
        viewModel.map.observeForever(mapObserver)
    }

    @Test
    fun `WHEN onViewCreated THEN set all flags`() {
        TODO()
    }

    @Test
    fun `WHEN place id is set THEN hit get district location GQL to get pinpoint detail`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictLocation but response doesn't have postal code THEN show location not found bottom sheet`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictLocation but response doesn't have district id THEN show location not found bottom sheet`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictLocation in ANA negative flow but pinpoint is in different district THEN show toaster and disable primary button`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictLocation in ANA negative flow but response has error message THEN show location not found bottom sheet`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictLocation success THEN show location detail bottom sheet and enable btn primary`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictLocation happens error network THEN show error network toaster`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictLocation returns error FOREGIN_COUNTRY_MESSAGE THEN show location out of reach bottom sheet`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictLocation returns error LOCATION_NOT_FOUND_MESSAGE THEN show location not found bottom sheet`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictLocation returns other error THEN show location not found bottom sheet`() {
        TODO()
    }

    // region getLocationFromLatLong
    @Test
    fun `WHEN lat long is set THEN hit getDistrictData with respective lat long`() {
        TODO()
    }

    // region getDistrictData
    @Test
    fun `WHEN getDistrictData success THEN show location detail bottom sheet and enable btn primary`() {
        TODO()
    }

    @Test
    fun `WHEN getDistrictData in ANA negative flow but pinpoint is in different district THEN show location detail bottom sheet and disable btn primary`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictData has messageError FOREGIN_COUNTRY_MESSAGE THEN show location out of reach bottom sheet`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictData has messageError LOCATION_NOT_FOUND_MESSAGE THEN show location not found bottom sheet`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictData has messageError THEN show location not found bottom sheet`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictData happens error network THEN show error network toaster`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictData returns error FOREGIN_COUNTRY_MESSAGE THEN show location out of reach bottom sheet`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictData returns error LOCATION_NOT_FOUND_MESSAGE THEN show location not found bottom sheet`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictData returns other error THEN show location not found bottom sheet`() {
        TODO()
    }

    // region getDistrictCenter
    @Test
    fun `WHEN district id is set (ANA Revamp Negative Flow) THEN move map to district center's lat long and hit getDistrictData`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictCenter happens error network THEN show error network toaster`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictCenter returns error FOREGIN_COUNTRY_MESSAGE THEN show location out of reach bottom sheet`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictCenter returns error LOCATION_NOT_FOUND_MESSAGE THEN show location not found bottom sheet`() {
        TODO()
    }

    @Test
    fun `WHEN hit getDistrictCenter returns other error THEN show location not found bottom sheet`() {
        TODO()
    }

    // region getGeocodeByDistrictAndCityName
    @Test
    fun `WHEN city name and district name is set (Pinpoint Only) THEN move map to district center's lat long and hit getDistrictData`() {
        TODO()
    }

    @Test
    fun `WHEN hit getGeocodeByDistrictAndCityName success THEN move map to result's lat long and hit getDistrictData`() {
        TODO()
    }

    @Test
    fun `WHEN hit getGeocodeByDistrictAndCityName doesn't have response THEN show location not found bottom sheet`() {
        TODO()
    }

    @Test
    fun `WHEN hit getGeocodeByDistrictAndCityName happens error network THEN show error network toaster`() {
        TODO()
    }

    @Test
    fun `WHEN hit getGeocodeByDistrictAndCityName returns error FOREGIN_COUNTRY_MESSAGE THEN show location out of reach bottom sheet`() {
        TODO()
    }

    @Test
    fun `WHEN hit getGeocodeByDistrictAndCityName returns error LOCATION_NOT_FOUND_MESSAGE THEN show location not found bottom sheet`() {
        TODO()
    }

    @Test
    fun `WHEN hit getGeocodeByDistrictAndCityName returns other error THEN show location not found bottom sheet`() {
        TODO()
    }

    // region onResultFromSearchAddress
    @Test
    fun `WHEN search address sent place id THEN set place id to ui model and hit getDistrictLocation`() {
        TODO()
    }

    @Test
    fun `WHEN search address sent lat long THEN move map and hit getDistrictData`() {
        TODO()
    }

    // region validatePinpoint
    @Test
    fun `WHEN click save pinpoint on shop address use case and pinpoint district doesn't match THEN show error toaster`() {
        TODO()
    }

    @Test
    fun `WHEN click save pinpoint on pinpoint only THEN set pinpoint result`() {
        TODO()
    }

    @Test
    fun `WHEN click save pinpoint on add or edit address THEN go to add address page as positive flow`() {
        TODO()
    }

    @Test
    fun `WHEN click save pinpoint on add or edit address and isPositiveFlow already set THEN go to add address page with current isPositiveFlow value`() {
        TODO()
    }

    // region discardPinpoint
    @Test
    fun `WHEN click discardPinpoint THEN go to add address page as positive flow`() {
        TODO()
    }

    @Test
    fun `WHEN click discardPinpoint and isPositiveFlow already set THEN go to add address page with current isPositiveFlow value`() {
        TODO()
    }

    // region getInitialBottomSheetState
    @Test
    fun `WHEN getInitialBottomSheetState on add address THEN show secondary button`() {
        TODO()
    }

    @Test
    fun `WHEN getInitialBottomSheetState on edit address THEN set button primary text and hide secondary button`() {
        TODO()
    }

    @Test
    fun `WHEN getInitialBottomSheetState on pinpoint only THEN set button primary text and hide secondary button`() {
        TODO()
    }

    // region createButtonSecondary
    @Test
    fun `WHEN createButtonSecondary on add address THEN show secondary button`() {
        TODO()
    }

    @Test
    fun `WHEN createButtonSecondary on edit address THEN hide secondary button`() {
        TODO()
    }

    @Test
    fun `WHEN createButtonSecondary on pinpoint only THEN hide secondary button`() {
        TODO()
    }

    // region validateDistrict
    @Test
    fun `WHEN validateDistrict on add address and positive flow THEN returns true`() {
        TODO()
    }

    @Test
    fun `WHEN validateDistrict on edit address THEN just pass (returns true)`() {
        TODO()
    }

    @Test
    fun `WHEN validateDistrict on pinpoint only THEN just pass (returns true)`() {
        TODO()
    }

    // region createButtonPrimary

    @Test
    fun `WHEN createButtonPrimary on add address THEN append lanjut isi alamat wording`() {
        TODO()
    }

    @Test
    fun `WHEN createButtonPrimary on edit address THEN append lanjut isi alamat wording`() {
        TODO()
    }

    @Test
    fun `WHEN createButtonPrimary on pinpoint only THEN remove lanjut isi alamat wording`() {
        TODO()
    }

    // region locationNotFound

    @Test
    fun `WHEN create locationNotFound on pinpoint only THEN show invalid location detail wording`() {
        TODO()
    }

    @Test
    fun `WHEN create locationNotFound but has already pinpoint before THEN show invalid location detail wording`() {
        TODO()
    }

    @Test
    fun `WHEN create locationNotFound on pinpoint only THEN hide button`() {
        TODO()
    }

    @Test
    fun `WHEN create locationNotFound on edit address that has pinpoint THEN hide button`() {
        TODO()
    }

    @Test
    fun `WHEN create locationNotFound on edit address that doesnt have pinpoint THEN show button`() {
        TODO()
    }

    @Test
    fun `WHEN create locationNotFound on add address that doesnt have pinpoint THEN show button`() {
        TODO()
    }

    // region location out of reach
    @Test
    fun `WHEN create locationOutOfReach THEN show invalid location`() {
        TODO()
    }
}
