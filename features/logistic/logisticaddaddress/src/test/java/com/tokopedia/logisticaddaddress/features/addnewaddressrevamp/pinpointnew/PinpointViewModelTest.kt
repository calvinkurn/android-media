package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.response.Data
import com.tokopedia.logisticCommon.data.response.GetDistrictResponse
import com.tokopedia.logisticCommon.data.response.KeroPlacesGetDistrict
import com.tokopedia.logisticCommon.domain.param.GetDistrictParam
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictBoundariesUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictCenterUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictGeoCodeUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticCommon.uimodel.AddressUiState
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.usecase.MapsGeocodeUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.DistrictBoundaryResponseUiModel
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.PinpointViewModel
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.ChoosePinpoint
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.MoveMap
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.PinpointAction
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.PinpointBottomSheetState
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.PinpointUiModel
import com.tokopedia.usecase.coroutines.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException

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
    private val pinpointBottomsheetObserver: Observer<PinpointBottomSheetState> =
        mockk(relaxed = true)
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
        val districtName = "jakarta barat"
        val cityName = "jakarta"
        val lat = 106.77
        val lng = 77.03
        val placeId = "placeid"
        val districtId = 1111L
        val whDistrictId = 1111L
        val addressId = "868934"
        val uiState = AddressUiState.AddAddress
        val isEditWarehouse = false
        val source = "source"
        val isPositiveFlow: Boolean? = null

        viewModel.onViewCreated(
            districtName = districtName,
            cityName = cityName,
            lat = lat,
            long = lng,
            placeId = placeId,
            districtId = districtId,
            whDistrictId = whDistrictId,
            addressId = addressId,
            uiState = uiState,
            isEditWarehouse = isEditWarehouse,
            source = source,
            isPositiveFlow = isPositiveFlow
        )

        val expectedPinpointModel = PinpointUiModel(
            districtName = districtName,
            cityName = cityName,
            lat = lat,
            long = lng,
            placeId = placeId,
            districtId = districtId
        )

        assert(expectedPinpointModel == viewModel.uiModel)
        assert(whDistrictId == viewModel.whDistrictId)
        assert(addressId == viewModel.addressId)
        assert(uiState == viewModel.uiState)
        assert(isEditWarehouse == viewModel.isEditWarehouse)
        assert(source == source)
        assert(viewModel.isPositiveFlow)
    }

    @Test
    fun `WHEN place id is set THEN hit get district location GQL to get pinpoint detail`() {
        viewModel.onViewCreated(
            placeId = "place id",
            isEditWarehouse = false,
            isPositiveFlow = null,
            lat = 22.22,
            long = 44.44,
            source = "source",
            uiState = AddressUiState.AddAddress
        )

        viewModel.fetchData()

        coVerify { getDistrict(GetDistrictParam(placeId = "place id", isManageAddressFlow = true)) }
    }

    @Test
    fun `WHEN hit getDistrictLocation but response doesn't have postal code THEN show location not found bottom sheet`() {
        viewModel.onViewCreated(
            placeId = "place id",
            isEditWarehouse = false,
            isPositiveFlow = null,
            source = "source",
            uiState = AddressUiState.AddAddress
        )

        coEvery {
            getDistrict(
                GetDistrictParam(
                    placeId = "place id",
                    isManageAddressFlow = true
                )
            )
        } returns createDistrictResponse(postalCode = "")

        viewModel.fetchData()

        assert(viewModel.pinpointBottomSheet.value is PinpointBottomSheetState.LocationInvalid)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).type == PinpointBottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).showMapIllustration)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).buttonState.enable)
    }

    private fun createDistrictResponse(
        postalCode: String = "1111",
        lat: Double = 0.0,
        long: Double = 0.0,
        districtId: Long = 444,
        title: String = "title",
        description: String = "description",
        errorMessage: String = ""
    ): GetDistrictResponse {
        return GetDistrictResponse(
            KeroPlacesGetDistrict(
                Data(
                    postalCode = postalCode,
                    latitude = lat.toString(),
                    longitude = long.toString(),
                    districtId = districtId,
                    title = title,
                    formattedAddress = description
                ),
                messageError = listOf(errorMessage)
            )
        )
    }

    @Test
    fun `WHEN hit getDistrictLocation but response doesn't have district id THEN show location not found bottom sheet`() {
        viewModel.onViewCreated(
            placeId = "place id",
            isEditWarehouse = false,
            isPositiveFlow = null,
            source = "source",
            uiState = AddressUiState.PinpointOnly
        )

        coEvery {
            getDistrict(
                GetDistrictParam(
                    placeId = "place id",
                    isManageAddressFlow = true
                )
            )
        } returns createDistrictResponse(districtId = 0)

        viewModel.fetchData()

        assert(viewModel.pinpointBottomSheet.value is PinpointBottomSheetState.LocationInvalid)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).type == PinpointBottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).showMapIllustration)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).buttonState.enable)
    }

    @Test
    fun `WHEN hit getDistrictLocation in ANA negative flow but pinpoint is in different district THEN show toaster and disable primary button`() {
        viewModel.onViewCreated(
            placeId = "place id",
            isEditWarehouse = false,
            isPositiveFlow = false,
            lat = 11.22,
            long = 44.55,
            districtId = 3333,
            source = "source",
            uiState = AddressUiState.AddAddress
        )
        val title = "tokopedia tower"
        val description = "Setiabudi, Jakarta Selatan"
        val districtId = 2222L

        coEvery {
            getDistrict(
                GetDistrictParam(
                    placeId = "place id",
                    isManageAddressFlow = true
                )
            )
        } returns createDistrictResponse(districtId = districtId, title = title, description = description)

        viewModel.fetchData()

        assert(viewModel.action.value == PinpointAction.InvalidDistrictPinpoint(source = PinpointAction.InvalidDistrictPinpoint.InvalidDistrictPinpointSource.ADD_ADDRESS_BUYER))
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).title == title)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).description == description)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonPrimary.enable)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonPrimary.show)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonPrimary.success)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonSecondary.show)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonSecondary.enable)
        assert(viewModel.uiModel.title != title)
        assert(viewModel.uiModel.districtId != districtId)
    }

    @Test
    fun `WHEN hit getDistrictLocation in ANA negative flow but response has error message THEN show location not found bottom sheet`() {
        viewModel.onViewCreated(
            placeId = "place id",
            isEditWarehouse = false,
            isPositiveFlow = false,
            lat = 11.22,
            long = 44.55,
            districtId = 3333,
            source = "source",
            uiState = AddressUiState.EditAddress
        )
        val title = "tokopedia tower"
        val description = "Setiabudi, Jakarta Selatan"

        coEvery {
            getDistrict(
                GetDistrictParam(
                    placeId = "place id",
                    isManageAddressFlow = true
                )
            )
        } returns createDistrictResponse(title = title, description = description, errorMessage = "Lokasi gagal ditemukan", lat = 11.11, long = 22.22)

        viewModel.fetchData()

        assert(viewModel.pinpointBottomSheet.value is PinpointBottomSheetState.LocationInvalid)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).type == PinpointBottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).showMapIllustration)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).buttonState.enable)
    }

    @Test
    fun `WHEN hit getDistrictLocation success THEN show location detail bottom sheet and enable btn primary`() {
        viewModel.onViewCreated(
            placeId = "place id",
            isEditWarehouse = false,
            isPositiveFlow = false,
            lat = 11.22,
            long = 44.55,
            districtId = 3333,
            source = "source",
            uiState = AddressUiState.AddAddress
        )
        val title = "tokopedia tower"
        val description = "Setiabudi, Jakarta Selatan"
        val districtId = 3333L
        val lat = 11.22
        val long = 33.44

        coEvery {
            getDistrict(
                GetDistrictParam(
                    placeId = "place id",
                    isManageAddressFlow = true
                )
            )
        } returns createDistrictResponse(districtId = districtId, title = title, description = description, lat = lat, long = long)

        viewModel.fetchData()

        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).title == title)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).description == description)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonPrimary.enable)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonPrimary.show)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonPrimary.success)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonSecondary.show)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonSecondary.enable)
        assert(viewModel.uiModel.title == title)
        assert(viewModel.uiModel.lat == lat)
        assert(viewModel.uiModel.long == long)
        assert(viewModel.map.value == MoveMap(lat, long))
    }

    @Test
    fun `WHEN hit getDistrictLocation happens error network THEN show error network toaster`() {
        viewModel.onViewCreated(
            placeId = "place id",
            isEditWarehouse = false,
            isPositiveFlow = false,
            lat = 11.22,
            long = 44.55,
            districtId = 3333,
            source = "source",
            uiState = AddressUiState.AddAddress
        )

        coEvery {
            getDistrict(
                GetDistrictParam(
                    placeId = "place id",
                    isManageAddressFlow = true
                )
            )
        } throws UnknownHostException()

        viewModel.fetchData()

        assert(viewModel.action.value == PinpointAction.NetworkError(""))
    }

    @Test
    fun `WHEN hit getDistrictLocation returns error FOREGIN_COUNTRY_MESSAGE THEN show location out of reach bottom sheet`() {
        viewModel.onViewCreated(
            placeId = "place id",
            isEditWarehouse = false,
            isPositiveFlow = false,
            lat = 11.22,
            long = 44.55,
            districtId = 3333,
            source = "source",
            uiState = AddressUiState.EditAddress
        )

        coEvery {
            getDistrict(
                GetDistrictParam(
                    placeId = "place id",
                    isManageAddressFlow = true
                )
            )
        } throws Throwable("Lokasi di luar Indonesia.")

        viewModel.fetchData()

        assert(viewModel.pinpointBottomSheet.value is PinpointBottomSheetState.LocationInvalid)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).type == PinpointBottomSheetState.LocationInvalid.LocationInvalidType.OUT_OF_COVERAGE)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).showMapIllustration)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).buttonState.enable)
    }

    @Test
    fun `WHEN hit getDistrictLocation returns error LOCATION_NOT_FOUND_MESSAGE THEN show location not found bottom sheet`() {
        viewModel.onViewCreated(
            placeId = "place id",
            isEditWarehouse = false,
            isPositiveFlow = false,
            lat = 11.22,
            long = 44.55,
            districtId = 3333,
            source = "source",
            uiState = AddressUiState.EditAddress
        )

        coEvery {
            getDistrict(
                GetDistrictParam(
                    placeId = "place id",
                    isManageAddressFlow = true
                )
            )
        } throws Throwable("Lokasi gagal ditemukan")

        viewModel.fetchData()

        assert(viewModel.pinpointBottomSheet.value is PinpointBottomSheetState.LocationInvalid)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).type == PinpointBottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).showMapIllustration)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).buttonState.enable)
    }

    @Test
    fun `WHEN hit getDistrictLocation returns other error THEN show location not found bottom sheet`() {
        viewModel.onViewCreated(
            placeId = "place id",
            isEditWarehouse = false,
            isPositiveFlow = false,
            lat = 11.22,
            long = 44.55,
            districtId = 3333,
            source = "source",
            uiState = AddressUiState.EditAddress
        )

        coEvery {
            getDistrict(
                GetDistrictParam(
                    placeId = "place id",
                    isManageAddressFlow = true
                )
            )
        } throws defaultThrowable

        viewModel.fetchData()

        assert(viewModel.pinpointBottomSheet.value is PinpointBottomSheetState.LocationInvalid)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).type == PinpointBottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).showMapIllustration)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).buttonState.enable)
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
