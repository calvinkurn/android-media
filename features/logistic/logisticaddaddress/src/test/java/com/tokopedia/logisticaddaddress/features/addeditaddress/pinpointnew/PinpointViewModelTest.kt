package com.tokopedia.logisticaddaddress.features.addeditaddress.pinpointnew

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.CoordinateModel
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.Geometry
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.Location
import com.tokopedia.logisticCommon.data.entity.response.AutoFillResponse
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.data.response.Data
import com.tokopedia.logisticCommon.data.response.GetDistrictBoundaryResponse
import com.tokopedia.logisticCommon.data.response.GetDistrictResponse
import com.tokopedia.logisticCommon.data.response.KeroAddrGetDistrictCenterResponse
import com.tokopedia.logisticCommon.data.response.KeroPlacesGetDistrict
import com.tokopedia.logisticCommon.domain.model.SuggestedPlace
import com.tokopedia.logisticCommon.domain.param.GetDistrictGeoCodeParam
import com.tokopedia.logisticCommon.domain.param.GetDistrictParam
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictBoundariesUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictCenterUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictGeoCodeUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticCommon.uimodel.AddressUiState
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.model.mapsgeocode.KeroAddressGeocode
import com.tokopedia.logisticaddaddress.domain.model.mapsgeocode.MapsGeocodeParam
import com.tokopedia.logisticaddaddress.domain.model.mapsgeocode.MapsGeocodeResponse
import com.tokopedia.logisticaddaddress.domain.usecase.MapsGeocodeUseCase
import com.tokopedia.logisticaddaddress.features.addeditaddress.uimodel.DistrictBoundaryResponseUiModel
import com.tokopedia.logisticaddaddress.features.pinpoint.PinpointViewModel
import com.tokopedia.logisticaddaddress.features.pinpoint.uimodel.ChoosePinpoint
import com.tokopedia.logisticaddaddress.features.pinpoint.uimodel.MoveMap
import com.tokopedia.logisticaddaddress.features.pinpoint.uimodel.PinpointAction
import com.tokopedia.logisticaddaddress.features.pinpoint.uimodel.PinpointBottomSheetState
import com.tokopedia.logisticaddaddress.features.pinpoint.uimodel.PinpointUiModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
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
            districtId = districtId,
            whDistrictId = whDistrictId,
            addressId = addressId,
            uiState = uiState,
            isEditWarehouse = isEditWarehouse,
            source = source,
            isPositiveFlow = isPositiveFlow,
            searchAddressData = null
        )

        val expectedPinpointModel = PinpointUiModel(
            districtName = districtName,
            cityName = cityName,
            lat = lat,
            long = lng,
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
            searchAddressData = SuggestedPlace(placeId = "place id"),
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
    fun `WHEN address detail is set THEN dont hit get district location GQL to get pinpoint detail`() {
        val districtName = "jakarta barat"
        val cityName = "jakarta"
        val lat = "106.77"
        val lng = "77.03"
        val districtId = 1111L
        val postalCode = "13456"
        viewModel.onViewCreated(
            searchAddressData = SuggestedPlace(
                districtName = districtName,
                cityName = cityName,
                lat = lat,
                long = lng,
                districtId = districtId,
                postalCode = postalCode
            ),
            isEditWarehouse = false,
            isPositiveFlow = null,
            lat = 22.22,
            long = 44.44,
            source = "source",
            uiState = AddressUiState.AddAddress
        )

        viewModel.fetchData()

        coVerify(exactly = 0) {
            getDistrict(
                GetDistrictParam(
                    placeId = "place id",
                    isManageAddressFlow = true
                )
            )
        }
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonPrimary.enable)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonPrimary.show)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonPrimary.success)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonSecondary.show)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonSecondary.enable)
        assert(viewModel.uiModel.districtName == districtName)
        assert(viewModel.uiModel.cityName == cityName)
        assert(viewModel.uiModel.lat == lat.toDouble())
        assert(viewModel.uiModel.long == lng.toDouble())
        assert(viewModel.map.value == MoveMap(lat.toDouble(), lng.toDouble()))
    }

    @Test
    fun `WHEN hit getDistrictLocation but response doesn't have postal code THEN show location not found bottom sheet`() {
        viewModel.onViewCreated(
            searchAddressData = SuggestedPlace(placeId = "place id"),
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
        errorMessage: String = "",
        districtName: String = "",
        cityName: String = "",
        provinceName: String = ""
    ): GetDistrictResponse {
        return GetDistrictResponse(
            KeroPlacesGetDistrict(
                Data(
                    postalCode = postalCode,
                    latitude = lat.toString(),
                    longitude = long.toString(),
                    districtId = districtId,
                    title = title,
                    districtName = districtName,
                    cityName = cityName,
                    provinceName = provinceName
                ),
                messageError = listOf(errorMessage)
            )
        )
    }

    @Test
    fun `WHEN hit getDistrictLocation but response doesn't have district id THEN show location not found bottom sheet`() {
        viewModel.onViewCreated(
            searchAddressData = SuggestedPlace(placeId = "place id"),
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
            searchAddressData = SuggestedPlace(placeId = "place id"),
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
        } returns createDistrictResponse(
            districtId = districtId,
            title = title
        )

        viewModel.fetchData()

        assert(viewModel.action.value == PinpointAction.InvalidDistrictPinpoint(source = PinpointAction.InvalidDistrictPinpoint.InvalidDistrictPinpointSource.ADD_ADDRESS_BUYER))
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).title == title)
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
            searchAddressData = SuggestedPlace(placeId = "place id"),
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
        } returns createDistrictResponse(
            lat = 11.11,
            long = 22.22,
            title = title,
            errorMessage = "Lokasi gagal ditemukan"
        )

        viewModel.fetchData()

        assert(viewModel.pinpointBottomSheet.value is PinpointBottomSheetState.LocationInvalid)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).type == PinpointBottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).showMapIllustration)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).buttonState.enable)
    }

    @Test
    fun `WHEN hit getDistrictLocation success THEN show location detail bottom sheet and enable btn primary`() {
        viewModel.onViewCreated(
            searchAddressData = SuggestedPlace(placeId = "place id"),
            isEditWarehouse = false,
            isPositiveFlow = false,
            lat = 11.22,
            long = 44.55,
            districtId = 3333,
            source = "source",
            uiState = AddressUiState.AddAddress
        )
        val title = "tokopedia tower"
        val districtName = "district name"
        val cityName = "city name"
        val provinceName = "province name"
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
        } returns createDistrictResponse(
            lat = lat,
            long = long,
            districtId = districtId,
            title = title,
            districtName = districtName,
            cityName = cityName,
            provinceName = provinceName
        )

        viewModel.fetchData()

        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).title == title)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).description == "$districtName, $cityName, $provinceName")
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
            searchAddressData = SuggestedPlace(placeId = "place id"),
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
        assert(viewModel.pinpointBottomSheet.value is PinpointBottomSheetState.LocationDetail)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).title.isEmpty())
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).description.isEmpty())
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonPrimary.success)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonPrimary.enable)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonSecondary.enable)
    }

    @Test
    fun `WHEN hit getDistrictLocation returns error FOREGIN_COUNTRY_MESSAGE THEN show location out of reach bottom sheet`() {
        viewModel.onViewCreated(
            searchAddressData = SuggestedPlace(placeId = "place id"),
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
            searchAddressData = SuggestedPlace(placeId = "place id"),
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
            searchAddressData = SuggestedPlace(placeId = "place id"),
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
        val lat = 11.22
        val lng = 44.55
        viewModel.onViewCreated(
            searchAddressData = null,
            isEditWarehouse = false,
            isPositiveFlow = false,
            lat = lat,
            long = lng,
            districtId = 3333,
            source = "source",
            uiState = AddressUiState.EditAddress
        )

        viewModel.fetchData()

        coVerify {
            getDistrictGeoCode(GetDistrictGeoCodeParam("$lat,$lng", isManageAddressFlow = true))
        }
    }

    // region getDistrictData
    @Test
    fun `WHEN getDistrictData success THEN show location detail bottom sheet and enable btn primary`() {
        val lat = 11.22
        val lng = 44.55
        val title = "title"
        val districtName = "district name"
        val cityName = "city name"
        val provinceName = "province name"
        val formattedAddress = "$districtName, $cityName, $provinceName"
        viewModel.onViewCreated(
            searchAddressData = null,
            isEditWarehouse = false,
            isPositiveFlow = false,
            lat = lat,
            long = lng,
            districtId = 0,
            source = "source",
            uiState = AddressUiState.EditAddress
        )

        coEvery {
            getDistrictGeoCode(GetDistrictGeoCodeParam("$lat,$lng", isManageAddressFlow = true))
        } returns AutoFillResponse(
            KeroMapsAutofill(
                com.tokopedia.logisticCommon.data.entity.response.Data(
                    title = title,
                    cityName = cityName,
                    provinceName = provinceName,
                    districtName = districtName,
                    districtId = 333,
                    latitude = lat.toString(),
                    longitude = lng.toString(),
                    formattedAddress = formattedAddress
                )
            )
        )

        viewModel.getDistrictData(lat, lng)

        assert(viewModel.pinpointBottomSheet.value is PinpointBottomSheetState.LocationDetail)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).title == title)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).description == formattedAddress)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonPrimary.enable)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonPrimary.show)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonPrimary.success)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonSecondary.show)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonSecondary.enable)
        assert(viewModel.uiModel.title == title)
        assert(viewModel.uiModel.lat == lat)
        assert(viewModel.uiModel.long == lng)
    }

    @Test
    fun `WHEN getDistrictData in ANA negative flow but pinpoint is in different district THEN show location detail bottom sheet and disable btn primary`() {
        val lat = 11.22
        val lng = 44.55
        val districtId = 1111L
        val title = "title"
        val districtName = "district name"
        val cityName = "city name"
        val provinceName = "province name"
        val formattedAddress = "$cityName, $districtName, $provinceName"
        val returnedDistrictId = 333L
        viewModel.onViewCreated(
            searchAddressData = null,
            isEditWarehouse = false,
            isPositiveFlow = false,
            lat = lat,
            long = lng,
            districtId = districtId,
            source = "source",
            uiState = AddressUiState.AddAddress
        )

        coEvery {
            getDistrictGeoCode(GetDistrictGeoCodeParam("$lat,$lng", isManageAddressFlow = true))
        } returns AutoFillResponse(
            KeroMapsAutofill(
                com.tokopedia.logisticCommon.data.entity.response.Data(
                    title = title,
                    cityName = cityName,
                    provinceName = provinceName,
                    districtName = districtName,
                    districtId = returnedDistrictId,
                    latitude = lat.toString(),
                    longitude = lng.toString(),
                    formattedAddress = formattedAddress
                )
            )
        )

        viewModel.getDistrictData(lat, lng)

        assert(viewModel.action.value == PinpointAction.InvalidDistrictPinpoint(source = PinpointAction.InvalidDistrictPinpoint.InvalidDistrictPinpointSource.ADD_ADDRESS_BUYER))
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).title == title)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).description == formattedAddress)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonPrimary.enable)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonPrimary.show)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonPrimary.success)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonSecondary.show)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationDetail).buttonSecondary.enable)
        assert(viewModel.uiModel.title != title)
        assert(viewModel.uiModel.districtId != returnedDistrictId)
    }

    @Test
    fun `WHEN hit getDistrictData has messageError FOREGIN_COUNTRY_MESSAGE THEN show location out of reach bottom sheet`() {
        val lat = 11.22
        val lng = 44.55
        val districtId = 1111L
        val title = "title"
        val districtName = "district name"
        val cityName = "city name"
        val provinceName = "province name"
        val formattedAddress = "$cityName, $districtName, $provinceName"
        viewModel.onViewCreated(
            searchAddressData = null,
            isEditWarehouse = false,
            isPositiveFlow = false,
            lat = lat,
            long = lng,
            districtId = districtId,
            source = "source",
            uiState = AddressUiState.AddAddress
        )

        coEvery {
            getDistrictGeoCode(GetDistrictGeoCodeParam("$lat,$lng", isManageAddressFlow = true))
        } returns AutoFillResponse(
            KeroMapsAutofill(
                com.tokopedia.logisticCommon.data.entity.response.Data(
                    title = title,
                    cityName = cityName,
                    provinceName = provinceName,
                    districtName = districtName,
                    latitude = lat.toString(),
                    longitude = lng.toString(),
                    formattedAddress = formattedAddress
                ),
                messageError = listOf("Lokasi di luar Indonesia.")
            )
        )

        viewModel.getDistrictData(lat, lng)

        assert(viewModel.pinpointBottomSheet.value is PinpointBottomSheetState.LocationInvalid)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).type == PinpointBottomSheetState.LocationInvalid.LocationInvalidType.OUT_OF_COVERAGE)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).showMapIllustration)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).buttonState.enable)
    }

    @Test
    fun `WHEN hit getDistrictData happens error network THEN show error network toaster`() {
        val lat = 11.22
        val lng = 44.55
        val districtId = 1111L
        viewModel.onViewCreated(
            searchAddressData = null,
            isEditWarehouse = false,
            isPositiveFlow = false,
            lat = lat,
            long = lng,
            districtId = districtId,
            source = "source",
            uiState = AddressUiState.AddAddress
        )

        coEvery {
            getDistrictGeoCode(GetDistrictGeoCodeParam("$lat,$lng", isManageAddressFlow = true))
        } throws UnknownHostException("network error")

        viewModel.getDistrictData(lat, lng)

        assert(viewModel.action.value == PinpointAction.NetworkError("network error"))
    }

    // region getDistrictCenter
    @Test
    fun `WHEN district id is set (ANA Revamp Negative Flow) THEN move map to district center's lat long and hit getDistrictData`() {
        val districtId = 111L
        val lat = 33.44
        val long = 55.66
        viewModel.onViewCreated(
            searchAddressData = null,
            isEditWarehouse = false,
            isPositiveFlow = false,
            districtId = districtId,
            source = "source",
            uiState = AddressUiState.AddAddress
        )
        coEvery { getDistrictCenter(districtId) } returns KeroAddrGetDistrictCenterResponse.Data(
            KeroAddrGetDistrictCenterResponse.Data.KeroAddrGetDistrictCenter(
                KeroAddrGetDistrictCenterResponse.Data.KeroAddrGetDistrictCenter.District(
                    latitude = lat,
                    longitude = long,
                    districtId = districtId
                )
            )
        )
        viewModel.fetchData()
        coVerify { getDistrictCenter(districtId) }
        coVerify { viewModel.getDistrictData(lat, long) }
        assert(viewModel.map.value == MoveMap(lat, long))
    }

    @Test
    fun `WHEN hit getDistrictCenter returns other error THEN show location not found bottom sheet`() {
        val districtId = 111L
        viewModel.onViewCreated(
            searchAddressData = null,
            isEditWarehouse = false,
            isPositiveFlow = false,
            districtId = districtId,
            source = "source",
            uiState = AddressUiState.AddAddress
        )
        coEvery { getDistrictCenter(districtId) } throws defaultThrowable

        viewModel.fetchData()

        assert(viewModel.pinpointBottomSheet.value is PinpointBottomSheetState.LocationInvalid)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).type == PinpointBottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).showMapIllustration)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).buttonState.enable)
    }

    // region getGeocodeByDistrictAndCityName
    @Test
    fun `WHEN city name and district name is set (Pinpoint Only) THEN move map to district center's lat long and hit getDistrictData`() {
        val districtName = "district name"
        val cityName = "city name"
        val returnedLat = 11.11
        val returnedLng = 11.33
        viewModel.onViewCreated(
            searchAddressData = null,
            isEditWarehouse = false,
            isPositiveFlow = false,
            districtId = 0,
            districtName = districtName,
            cityName = cityName,
            source = "source",
            uiState = AddressUiState.PinpointOnly
        )
        coEvery {
            mapsGeocodeUseCase(
                MapsGeocodeParam(
                    input = MapsGeocodeParam.MapsGeocodeDataParam(
                        address = "$districtName, $cityName"
                    )
                )
            )
        } returns MapsGeocodeResponse(
            KeroAddressGeocode(
                data = listOf(
                    CoordinateModel().apply {
                        geometry = Geometry().apply {
                            location = Location().apply {
                                lat = returnedLat
                                lng = returnedLng
                            }
                        }
                    }
                )
            )
        )

        viewModel.fetchData()

        coVerify {
            mapsGeocodeUseCase(
                MapsGeocodeParam(
                    input = MapsGeocodeParam.MapsGeocodeDataParam(
                        address = "$districtName, $cityName"
                    )
                )
            )
        }
        assert(viewModel.map.value == MoveMap(returnedLat, returnedLng))
        coVerify { viewModel.getDistrictData(returnedLat, returnedLng) }
    }

    @Test
    fun `WHEN hit getGeocodeByDistrictAndCityName doesn't have response THEN show location not found bottom sheet`() {
        val districtName = "district name"
        val cityName = "city name"
        viewModel.onViewCreated(
            searchAddressData = null,
            isEditWarehouse = false,
            isPositiveFlow = false,
            districtId = 0,
            districtName = districtName,
            cityName = cityName,
            source = "source",
            uiState = AddressUiState.PinpointOnly
        )
        coEvery {
            mapsGeocodeUseCase(
                MapsGeocodeParam(
                    input = MapsGeocodeParam.MapsGeocodeDataParam(
                        address = "$districtName, $cityName"
                    )
                )
            )
        } returns MapsGeocodeResponse(
            KeroAddressGeocode(
                data = listOf()
            )
        )

        viewModel.fetchData()

        assert(viewModel.pinpointBottomSheet.value is PinpointBottomSheetState.LocationInvalid)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).type == PinpointBottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).showMapIllustration)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).buttonState.enable)
    }

    @Test
    fun `WHEN hit getGeocodeByDistrictAndCityName returns other error THEN show location not found bottom sheet`() {
        val districtName = "district name"
        val cityName = "city name"
        viewModel.onViewCreated(
            searchAddressData = null,
            isEditWarehouse = false,
            isPositiveFlow = false,
            districtId = 0,
            districtName = districtName,
            cityName = cityName,
            source = "source",
            uiState = AddressUiState.PinpointOnly
        )
        coEvery {
            mapsGeocodeUseCase(
                MapsGeocodeParam(
                    input = MapsGeocodeParam.MapsGeocodeDataParam(
                        address = "$districtName, $cityName"
                    )
                )
            )
        } throws defaultThrowable

        viewModel.fetchData()

        assert(viewModel.pinpointBottomSheet.value is PinpointBottomSheetState.LocationInvalid)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).type == PinpointBottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).showMapIllustration)
        assert(!(viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).buttonState.enable)
    }

    // region onResultFromSearchAddress
    @Test
    fun `WHEN search address sent place id THEN set place id to ui model and hit getDistrictLocation`() {
        viewModel.onResultFromSearchAddress(
            searchAddressData = SuggestedPlace(placeId = "placeid"),
            lat = 0.0,
            long = 0.0
        )
        coVerify { getDistrict(GetDistrictParam("placeid", isManageAddressFlow = true)) }
    }

    @Test
    fun `WHEN search address sent lat long THEN move map and hit getDistrictData`() {
        viewModel.onResultFromSearchAddress(searchAddressData = null, lat = 13.11, long = 88.09)
        assert(viewModel.map.value == MoveMap(13.11, 88.09))
        coVerify { viewModel.getDistrictData(13.11, 88.09) }
    }

    // region validatePinpoint
    @Test
    fun `WHEN click save pinpoint on shop address use case and pinpoint district doesn't match THEN show error toaster`() {
        viewModel.onViewCreated(
            whDistrictId = 222,
            isEditWarehouse = true,
            uiState = AddressUiState.PinpointOnly,
            isPositiveFlow = null,
            source = "shop address",
            searchAddressData = null
        )

        viewModel.uiModel = viewModel.uiModel.copy(districtId = 1111L)

        viewModel.validatePinpoint()

        assert(viewModel.action.value == PinpointAction.InvalidDistrictPinpoint(source = PinpointAction.InvalidDistrictPinpoint.InvalidDistrictPinpointSource.SHOP_ADDRESS))
    }

    @Test
    fun `WHEN click save pinpoint on pinpoint only THEN set pinpoint result`() {
        viewModel.onViewCreated(
            isEditWarehouse = false,
            uiState = AddressUiState.PinpointOnly,
            isPositiveFlow = null,
            source = "checkout",
            searchAddressData = null
        )

        val result = PinpointUiModel(
            districtName = "district name",
            cityName = "city name",
            provinceName = "province name",
            districtId = 1111L,
            cityId = 3333,
            provinceId = 2222,
            postalCode = "12048",
            lat = 22.22,
            long = 44.44,
            title = "title"
        )
        val addressModel = SaveAddressDataModel(
            districtName = result.districtName,
            cityName = result.cityName,
            provinceName = result.provinceName,
            districtId = result.districtId,
            cityId = result.cityId,
            provinceId = result.provinceId,
            postalCode = result.postalCode,
            latitude = result.lat.toString(),
            longitude = result.long.toString(),
            title = result.title,
            formattedAddress = result.formattedAddress,
            selectedDistrict = result.selectedDistrict
        )
        viewModel.uiModel = result

        viewModel.validatePinpoint()
        assert(
            viewModel.choosePinpoint.value == ChoosePinpoint.SetPinpointResult(
                saveAddressDataModel = addressModel,
                pinpointUiModel = result
            )
        )
    }

    @Test
    fun `WHEN click save pinpoint on add or edit address THEN go to add address page as positive flow`() {
        viewModel.onViewCreated(
            isEditWarehouse = false,
            uiState = AddressUiState.AddAddress,
            isPositiveFlow = null,
            source = "checkout",
            searchAddressData = null
        )

        val result = PinpointUiModel(
            districtName = "district name",
            cityName = "city name",
            provinceName = "province name",
            districtId = 1111L,
            cityId = 3333,
            provinceId = 2222,
            postalCode = "12048",
            lat = 22.22,
            long = 44.44,
            title = "title"
        )
        viewModel.uiModel = result

        viewModel.validatePinpoint()
        assert(
            viewModel.choosePinpoint.value == ChoosePinpoint.GoToAddressForm(
                saveChanges = true,
                pinpointUiModel = result,
                addressState = AddressUiState.AddAddress,
                source = "checkout",
                isPositiveFlow = true
            )
        )
    }

    @Test
    fun `WHEN click save pinpoint on add or edit address and isPositiveFlow already set THEN go to add address page with current isPositiveFlow value`() {
        viewModel.onViewCreated(
            isEditWarehouse = false,
            uiState = AddressUiState.AddAddress,
            isPositiveFlow = false,
            source = "checkout",
            searchAddressData = null
        )

        val result = PinpointUiModel(
            districtName = "district name",
            cityName = "city name",
            provinceName = "province name",
            districtId = 1111L,
            cityId = 3333,
            provinceId = 2222,
            postalCode = "12048",
            lat = 22.22,
            long = 44.44,
            title = "title"
        )
        viewModel.uiModel = result

        viewModel.validatePinpoint()
        assert(
            viewModel.choosePinpoint.value == ChoosePinpoint.GoToAddressForm(
                saveChanges = true,
                pinpointUiModel = result,
                addressState = AddressUiState.AddAddress,
                source = "checkout",
                isPositiveFlow = false
            )
        )
    }

    // region discardPinpoint
    @Test
    fun `WHEN click discardPinpoint THEN go to add address page as negative flow`() {
        viewModel.onViewCreated(
            isEditWarehouse = false,
            uiState = AddressUiState.AddAddress,
            isPositiveFlow = null,
            source = "checkout",
            searchAddressData = null
        )

        viewModel.discardPinpoint()
        assert(viewModel.choosePinpoint.value is ChoosePinpoint.GoToAddressForm)
        assert((viewModel.choosePinpoint.value as ChoosePinpoint.GoToAddressForm).saveChanges == false)
        assert((viewModel.choosePinpoint.value as ChoosePinpoint.GoToAddressForm).isPositiveFlow == false)
    }

    @Test
    fun `WHEN click discardPinpoint and isPositiveFlow already set THEN go to add address page with current isPositiveFlow value`() {
        viewModel.onViewCreated(
            isEditWarehouse = false,
            uiState = AddressUiState.AddAddress,
            isPositiveFlow = true,
            source = "checkout",
            searchAddressData = null
        )

        viewModel.discardPinpoint()
        assert(viewModel.choosePinpoint.value is ChoosePinpoint.GoToAddressForm)
        assert((viewModel.choosePinpoint.value as ChoosePinpoint.GoToAddressForm).saveChanges == false)
        assert((viewModel.choosePinpoint.value as ChoosePinpoint.GoToAddressForm).isPositiveFlow == true)
    }

    // region getDistrictBoundary
    @Test
    fun `WHEN getDistrictBoundary in ana revamp negative flow THEN hit getDistrictBoundary BE`() {
        val districtId = 111L
        viewModel.onViewCreated(
            isEditWarehouse = false,
            uiState = AddressUiState.AddAddress,
            isPositiveFlow = false,
            source = "checkout",
            districtId = districtId,
            searchAddressData = null
        )

        coEvery { getDistrictBoundaries(districtId) } returns GetDistrictBoundaryResponse()

        viewModel.getDistrictBoundaries()

        coVerify { getDistrictBoundaries(districtId) }
        assert(viewModel.districtBoundary.value is Success)
    }

    @Test
    fun `WHEN getDistrictBoundary in ana revamp positive flow THEN dont hit getDistrictBoundary BE`() {
        val districtId = 111L
        viewModel.onViewCreated(
            isEditWarehouse = false,
            uiState = AddressUiState.AddAddress,
            isPositiveFlow = true,
            source = "checkout",
            searchAddressData = null
        )

        coEvery { getDistrictBoundaries(districtId) } returns GetDistrictBoundaryResponse()

        viewModel.getDistrictBoundaries()

        coVerify(exactly = 0) { getDistrictBoundaries(districtId) }
    }

    @Test
    fun `WHEN getDistrictBoundary in edit address flow THEN dont hit getDistrictBoundary BE`() {
        val districtId = 111L
        viewModel.onViewCreated(
            isEditWarehouse = false,
            uiState = AddressUiState.EditAddress,
            isPositiveFlow = true,
            source = "checkout",
            districtId = districtId,
            searchAddressData = null
        )

        coEvery { getDistrictBoundaries(districtId) } returns GetDistrictBoundaryResponse()

        viewModel.getDistrictBoundaries()

        coVerify(exactly = 0) { getDistrictBoundaries(districtId) }
    }

    @Test
    fun `WHEN getDistrictBoundary in pinpoint only THEN dont hit getDistrictBoundary BE`() {
        val districtId = 111L
        viewModel.onViewCreated(
            isEditWarehouse = false,
            uiState = AddressUiState.PinpointOnly,
            isPositiveFlow = true,
            source = "checkout",
            districtId = districtId,
            searchAddressData = null
        )

        coEvery { getDistrictBoundaries(districtId) } returns GetDistrictBoundaryResponse()

        viewModel.getDistrictBoundaries()

        coVerify(exactly = 0) { getDistrictBoundaries(districtId) }
    }

    @Test
    fun `WHEN getDistrictBoundary in ANA revamp negative error THEN show error`() {
        val districtId = 111L
        viewModel.onViewCreated(
            isEditWarehouse = false,
            uiState = AddressUiState.AddAddress,
            isPositiveFlow = false,
            source = "checkout",
            districtId = districtId,
            searchAddressData = null
        )

        coEvery { getDistrictBoundaries(districtId) } throws defaultThrowable

        viewModel.getDistrictBoundaries()

        assert(viewModel.pinpointBottomSheet.value is PinpointBottomSheetState.LocationInvalid)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).type == PinpointBottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).showMapIllustration)
        assert((viewModel.pinpointBottomSheet.value as PinpointBottomSheetState.LocationInvalid).buttonState.enable)
    }

    @Test
    fun `WHEN set save address data model from pinpoint webview THEN save address data model pinpoint`() {
        val address = SaveAddressDataModel()
        viewModel.setAddress(address)

        assert(address == viewModel.getAddress())
    }

    @Test
    fun `WHEN on pinpoint only and does not give any data THEN trigger get current location`() {
        viewModel.onViewCreated(
            uiState = AddressUiState.PinpointOnly,
            isEditWarehouse = false,
            source = "checkout",
            isPositiveFlow = null,
            searchAddressData = null
        )

        viewModel.fetchData()

        assert(viewModel.action.value is PinpointAction.GetCurrentLocation)
    }

    // region isPositiveFlow

    @Test
    fun `WHEN user on ana revamp negative flow THEN isPositiveFlow returns false`() {
        viewModel.onViewCreated(
            uiState = AddressUiState.AddAddress,
            isEditWarehouse = false,
            source = "checkout",
            isPositiveFlow = false,
            searchAddressData = null
        )

        assert(!viewModel.isPositiveFlow)
    }

    @Test
    fun `WHEN user on ana revamp positive flow THEN isPositiveFlow returns true`() {
        viewModel.onViewCreated(
            uiState = AddressUiState.AddAddress,
            isEditWarehouse = false,
            source = "checkout",
            isPositiveFlow = true,
            searchAddressData = null
        )

        assert(viewModel.isPositiveFlow)
    }

    @Test
    fun `WHEN user on ana revamp flow is undecided THEN isPositiveFlow returns true for analytic`() {
        viewModel.onViewCreated(
            uiState = AddressUiState.AddAddress,
            isEditWarehouse = false,
            source = "checkout",
            isPositiveFlow = null,
            searchAddressData = null
        )

        assert(viewModel.isPositiveFlow)
    }
}
