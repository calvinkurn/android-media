package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.CoordinateModel
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.Geometry
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.Location
import com.tokopedia.logisticCommon.data.entity.response.AutoFillResponse
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.response.GetDistrictBoundaryResponse
import com.tokopedia.logisticCommon.data.response.GetDistrictResponse
import com.tokopedia.logisticCommon.data.response.KeroAddrGetDistrictCenterResponse
import com.tokopedia.logisticaddaddress.data.entity.mapsgeocode.KeroAddressGeocode
import com.tokopedia.logisticaddaddress.data.entity.mapsgeocode.MapsGeocodeResponse
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.usecase.MapsGeocodeUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_boundary.DistrictBoundaryResponseUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.uimodel.MapsGeocodeState
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.DistrictCenterUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
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
class PinpointNewPageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repo: KeroRepository = mockk(relaxed = true)
    private val getDistrictMapper = GetDistrictMapper()
    private val districtBoundaryMapper = DistrictBoundaryMapper()

    private lateinit var pinpointNewPageViewModel: PinpointNewPageViewModel

    private val autofillDistrictDataObserver: Observer<Result<KeroMapsAutofill>> =
        mockk(relaxed = true)
    private val districtLocationObserver: Observer<Result<GetDistrictDataUiModel>> =
        mockk(relaxed = true)
    private val districtBoundaryObserver: Observer<Result<DistrictBoundaryResponseUiModel>> =
        mockk(relaxed = true)
    private val districtCenterObserver: Observer<Result<DistrictCenterUiModel>> =
        mockk(relaxed = true)
    private val mapsGeocodeStateObserver: Observer<MapsGeocodeState> =
        mockk(relaxed = true)

    private val mapsGeocodeUseCase = mockk<MapsGeocodeUseCase>(relaxed = true)

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        pinpointNewPageViewModel =
            PinpointNewPageViewModel(
                repo,
                getDistrictMapper,
                districtBoundaryMapper,
                mapsGeocodeUseCase
            )
        pinpointNewPageViewModel.autofillDistrictData.observeForever(autofillDistrictDataObserver)
        pinpointNewPageViewModel.districtLocation.observeForever(districtLocationObserver)
        pinpointNewPageViewModel.districtBoundary.observeForever(districtBoundaryObserver)
        pinpointNewPageViewModel.districtCenter.observeForever(districtCenterObserver)
        pinpointNewPageViewModel.mapsGeocodeState.observeForever(mapsGeocodeStateObserver)
    }

    @Test
    fun `Get District Data Success`() {
        coEvery { repo.getDistrictGeocode(any(), any()) } returns AutoFillResponse()
        pinpointNewPageViewModel.getDistrictData(1134.5, -6.4214)
        verify { autofillDistrictDataObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get District Data Fail`() {
        coEvery { repo.getDistrictGeocode(any()) } throws defaultThrowable
        pinpointNewPageViewModel.getLocationFromLatLong()
        verify { autofillDistrictDataObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Get District Location Success`() {
        coEvery { repo.getDistrict(any(), any()) } returns GetDistrictResponse()
        pinpointNewPageViewModel.getDistrictLocation("12312")
        verify { districtLocationObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get District Location Fail`() {
        coEvery { repo.getDistrict(any()) } throws defaultThrowable
        pinpointNewPageViewModel.getDistrictLocation("12312")
        verify { districtLocationObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Get District Boundaries Success`() {
        coEvery { repo.getDistrictBoundaries(any()) } returns GetDistrictBoundaryResponse()
        pinpointNewPageViewModel.getDistrictBoundaries()
        verify { districtBoundaryObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get District Boundaries Fail`() {
        coEvery { repo.getDistrictBoundaries(any()) } throws defaultThrowable
        pinpointNewPageViewModel.getDistrictBoundaries()
        verify { districtBoundaryObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Get District Center by District ID Success`() {
        coEvery { repo.getDistrictCenter(any()) } returns KeroAddrGetDistrictCenterResponse.Data()
        pinpointNewPageViewModel.getDistrictCenter()
        verify { districtCenterObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get District Center by District ID Fail`() {
        coEvery { repo.getDistrictCenter(any()) } throws defaultThrowable
        pinpointNewPageViewModel.getDistrictCenter()
        verify { districtCenterObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `get save address`() {
        val address = SaveAddressDataModel(
            formattedAddress = "Unnamed Road, Jl Testimoni",
            selectedDistrict = "Testimoni"
        )
        pinpointNewPageViewModel.setAddress(address)
        Assert.assertEquals(pinpointNewPageViewModel.getAddress(), address)
    }

    @Test
    fun `verify set gms availability flag is correct`() {
        val gmsAvailable = true
        pinpointNewPageViewModel.isGmsAvailable = gmsAvailable

        Assert.assertEquals(pinpointNewPageViewModel.isGmsAvailable, gmsAvailable)
    }

    @Test
    fun `verify set data from arguments is correctly`() {
        // Inject
        val currentPlaceId = "123"
        val latitude = 1.0
        val longitude = 1.0
        val addressData = spyk(SaveAddressDataModel())
        val isPositiveFlow = true
        val isPolygon = false
        val isFromAddressForm = false
        val isEdit = false
        val source = "source"
        val isGetPinPointOnly = false

        // When
        pinpointNewPageViewModel.setDataFromArguments(
            currentPlaceId = currentPlaceId,
            latitude = latitude,
            longitude = longitude,
            addressData = addressData,
            isPositiveFlow = isPositiveFlow,
            isPolygon = isPolygon,
            isFromAddressForm = isFromAddressForm,
            isEdit = isEdit,
            source = source,
            isGetPinPointOnly = isGetPinPointOnly,
            districtName = null,
            cityName = null
        )

        // Then
        with(pinpointNewPageViewModel) {
            Assert.assertEquals(this.currentPlaceId, currentPlaceId)
            Assert.assertNotEquals(this.getAddress().latitude, latitude.toString())
            Assert.assertNotEquals(this.getAddress().longitude, longitude.toString())
            Assert.assertEquals(this.isPositiveFlow, isPositiveFlow)
            Assert.assertEquals(this.isPolygon, isPolygon)
            Assert.assertEquals(this.isFromAddressForm, isFromAddressForm)
            Assert.assertEquals(this.isEdit, isEdit)
            Assert.assertEquals(this.source, source)
            Assert.assertEquals(this.isGetPinPointOnly, isGetPinPointOnly)
            Assert.assertFalse(this.isEditOrGetPinPointOnly)
            Assert.assertFalse(this.hasDistrictAndCityName)
        }
    }

    @Test
    fun `verify set data from arguments when address data null is correctly`() {
        // Inject
        val currentPlaceId = "123"
        val latitude = 1.0
        val longitude = 1.0
        val isPositiveFlow = true
        val isPolygon = false
        val isFromAddressForm = false
        val isEdit = false
        val source = "source"
        val isGetPinPointOnly = true
        val districtName = "Gajahmungkur"
        val cityName = "Semarang"

        // When
        pinpointNewPageViewModel.setDataFromArguments(
            currentPlaceId = currentPlaceId,
            latitude = latitude,
            longitude = longitude,
            addressData = null,
            isPositiveFlow = isPositiveFlow,
            isPolygon = isPolygon,
            isFromAddressForm = isFromAddressForm,
            isEdit = isEdit,
            source = source,
            isGetPinPointOnly = isGetPinPointOnly,
            districtName = districtName,
            cityName = cityName
        )

        // Then
        with(pinpointNewPageViewModel) {
            Assert.assertEquals(this.currentPlaceId, currentPlaceId)
            Assert.assertEquals(this.getAddress().latitude, latitude.toString())
            Assert.assertEquals(this.getAddress().longitude, longitude.toString())
            Assert.assertEquals(this.isPositiveFlow, isPositiveFlow)
            Assert.assertEquals(this.isPolygon, isPolygon)
            Assert.assertEquals(this.isFromAddressForm, isFromAddressForm)
            Assert.assertEquals(this.isEdit, isEdit)
            Assert.assertEquals(this.source, source)
            Assert.assertEquals(this.isGetPinPointOnly, isGetPinPointOnly)
            Assert.assertTrue(this.isEditOrGetPinPointOnly)
            Assert.assertTrue(this.hasDistrictAndCityName)
            Assert.assertEquals(this.getAddress().districtName, districtName)
            Assert.assertEquals(this.getAddress().cityName, cityName)
        }
    }

    @Test
    fun `verify set data from arguments when city and district empty is correctly`() {
        // Inject
        val currentPlaceId = "123"
        val latitude = 1.0
        val longitude = 1.0
        val isPositiveFlow = true
        val isPolygon = false
        val isFromAddressForm = false
        val isEdit = false
        val source = "source"
        val isGetPinPointOnly = true
        val districtName = ""
        val cityName = ""
        val addressData = SaveAddressDataModel(cityName = "Semarang", districtName = "Gajah Mungkur")

        // When
        pinpointNewPageViewModel.setDataFromArguments(
            currentPlaceId = currentPlaceId,
            latitude = latitude,
            longitude = longitude,
            isPositiveFlow = isPositiveFlow,
            isPolygon = isPolygon,
            isFromAddressForm = isFromAddressForm,
            isEdit = isEdit,
            source = source,
            isGetPinPointOnly = isGetPinPointOnly,
            districtName = districtName,
            cityName = cityName,
            addressData = addressData
        )

        // Then
        with(pinpointNewPageViewModel) {
            Assert.assertNotEquals(this.getAddress().districtName, districtName)
            Assert.assertNotEquals(this.getAddress().cityName, cityName)
        }
    }

    @Test
    fun `verify set data from arguments when city empty is correctly`() {
        // Inject
        val currentPlaceId = "123"
        val latitude = 1.0
        val longitude = 1.0
        val isPositiveFlow = true
        val isPolygon = false
        val isFromAddressForm = false
        val isEdit = false
        val source = "source"
        val isGetPinPointOnly = true
        val districtName = "Gajah Mungku"
        val cityName = ""
        val addressData = SaveAddressDataModel(cityName = "Semarang", districtName = "Gajah Mungkur")

        // When
        pinpointNewPageViewModel.setDataFromArguments(
            currentPlaceId = currentPlaceId,
            latitude = latitude,
            longitude = longitude,
            isPositiveFlow = isPositiveFlow,
            isPolygon = isPolygon,
            isFromAddressForm = isFromAddressForm,
            isEdit = isEdit,
            source = source,
            isGetPinPointOnly = isGetPinPointOnly,
            districtName = districtName,
            cityName = cityName,
            addressData = addressData
        )

        // Then
        with(pinpointNewPageViewModel) {
            Assert.assertNotEquals(this.getAddress().districtName, districtName)
            Assert.assertNotEquals(this.getAddress().cityName, cityName)
        }
    }

    @Test
    fun `verify set data from arguments when district empty is correctly`() {
        // Inject
        val currentPlaceId = "123"
        val latitude = 1.0
        val longitude = 1.0
        val isPositiveFlow = true
        val isPolygon = false
        val isFromAddressForm = false
        val isEdit = false
        val source = "source"
        val isGetPinPointOnly = true
        val districtName = ""
        val cityName = "Semaran"
        val addressData = SaveAddressDataModel(cityName = "Semarang", districtName = "Gajah Mungkur")

        // When
        pinpointNewPageViewModel.setDataFromArguments(
            currentPlaceId = currentPlaceId,
            latitude = latitude,
            longitude = longitude,
            isPositiveFlow = isPositiveFlow,
            isPolygon = isPolygon,
            isFromAddressForm = isFromAddressForm,
            isEdit = isEdit,
            source = source,
            isGetPinPointOnly = isGetPinPointOnly,
            districtName = districtName,
            cityName = cityName,
            addressData = addressData
        )

        // Then
        with(pinpointNewPageViewModel) {
            Assert.assertNotEquals(this.getAddress().districtName, districtName)
            Assert.assertNotEquals(this.getAddress().cityName, cityName)
        }
    }

    @Test
    fun `verify set data from arguments when district null is correctly`() {
        // Inject
        val currentPlaceId = "123"
        val latitude = 1.0
        val longitude = 1.0
        val isPositiveFlow = true
        val isPolygon = false
        val isFromAddressForm = false
        val isEdit = false
        val source = "source"
        val isGetPinPointOnly = true
        val districtName = null
        val cityName = "Semaran"
        val addressData = SaveAddressDataModel(cityName = "Semarang", districtName = "Gajah Mungkur")

        // When
        pinpointNewPageViewModel.setDataFromArguments(
            currentPlaceId = currentPlaceId,
            latitude = latitude,
            longitude = longitude,
            isPositiveFlow = isPositiveFlow,
            isPolygon = isPolygon,
            isFromAddressForm = isFromAddressForm,
            isEdit = isEdit,
            source = source,
            isGetPinPointOnly = isGetPinPointOnly,
            districtName = districtName,
            cityName = cityName,
            addressData = addressData
        )

        // Then
        with(pinpointNewPageViewModel) {
            Assert.assertNotEquals(this.getAddress().districtName, districtName)
            Assert.assertNotEquals(this.getAddress().cityName, cityName)
        }
    }

    @Test
    fun `verify set data from arguments when city null is correctly`() {
        // Inject
        val currentPlaceId = "123"
        val latitude = 1.0
        val longitude = 1.0
        val isPositiveFlow = true
        val isPolygon = false
        val isFromAddressForm = false
        val isEdit = false
        val source = "source"
        val isGetPinPointOnly = true
        val districtName = "Gajah"
        val cityName = null
        val addressData = SaveAddressDataModel(cityName = "Semarang", districtName = "Gajah Mungkur")

        // When
        pinpointNewPageViewModel.setDataFromArguments(
            currentPlaceId = currentPlaceId,
            latitude = latitude,
            longitude = longitude,
            isPositiveFlow = isPositiveFlow,
            isPolygon = isPolygon,
            isFromAddressForm = isFromAddressForm,
            isEdit = isEdit,
            source = source,
            isGetPinPointOnly = isGetPinPointOnly,
            districtName = districtName,
            cityName = cityName,
            addressData = addressData
        )

        // Then
        with(pinpointNewPageViewModel) {
            Assert.assertNotEquals(this.getAddress().districtName, districtName)
            Assert.assertNotEquals(this.getAddress().cityName, cityName)
        }
    }

    @Test
    fun `verify when get maps geocode is success`() {
        val latitude = -7.7831383
        val longitude = 110.42679
        val location = Location().apply {
            lat = latitude
            lng = longitude
        }
        val mockResponse = spyk(
            MapsGeocodeResponse().apply {
                keroAddressGeocode = spyk(
                    KeroAddressGeocode(
                        data = arrayListOf(
                            spyk(
                                CoordinateModel().apply {
                                    geometry = Geometry().apply {
                                        this.location = location
                                    }
                                }
                            )
                        )
                    )
                )
            }
        )

        coEvery {
            mapsGeocodeUseCase.invoke(any())
        } returns mockResponse

        pinpointNewPageViewModel.getGeocodeByDistrictAndCityName()

        verify {
            mapsGeocodeStateObserver.onChanged(MapsGeocodeState.Success(location))
        }
    }

    @Test
    fun `verify when get maps geocode is empty data`() {
        val mockResponse = spyk(MapsGeocodeResponse())

        coEvery {
            mapsGeocodeUseCase.invoke(any())
        } returns mockResponse

        pinpointNewPageViewModel.getGeocodeByDistrictAndCityName()

        verify {
            mapsGeocodeStateObserver.onChanged(MapsGeocodeState.Fail(PinpointNewPageFragment.LOCATION_NOT_FOUND_MESSAGE))
        }
    }

    @Test
    fun `verify when get maps geocode error`() {
        coEvery {
            mapsGeocodeUseCase.invoke(any())
        } throws defaultThrowable

        pinpointNewPageViewModel.getGeocodeByDistrictAndCityName()

        verify {
            mapsGeocodeStateObserver.onChanged(MapsGeocodeState.Fail(defaultThrowable.message.orEmpty()))
        }
    }

    @Test
    fun `verify when get maps geocode error with null message then will show empty string`() {
        coEvery {
            mapsGeocodeUseCase.invoke(any())
        } throws Throwable()

        pinpointNewPageViewModel.getGeocodeByDistrictAndCityName()

        verify {
            mapsGeocodeStateObserver.onChanged(MapsGeocodeState.Fail(""))
        }
    }
}
