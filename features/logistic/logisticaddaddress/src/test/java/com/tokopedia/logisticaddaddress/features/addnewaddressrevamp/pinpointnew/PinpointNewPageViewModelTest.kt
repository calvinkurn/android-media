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
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.model.mapsgeocode.KeroAddressGeocode
import com.tokopedia.logisticaddaddress.domain.model.mapsgeocode.MapsGeocodeResponse
import com.tokopedia.logisticaddaddress.domain.usecase.MapsGeocodeUseCase
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.MapsGeocodeState
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.DistrictBoundaryResponseUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.DistrictCenterUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.PinpointNewPageFragment
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.PinpointNewPageViewModel
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
    fun `verify set district and city name when has district value and city name is null`() {
        pinpointNewPageViewModel.setDistrictAndCityName(
            districtName = "solo",
            cityName = null
        )

        with(pinpointNewPageViewModel.getAddress()) {
            assertTrue(districtName.isBlank())
            assertTrue(cityName.isBlank())
        }
    }

    @Test
    fun `verify set district and city name when has district value and city name is empty`() {
        pinpointNewPageViewModel.setDistrictAndCityName(
            districtName = "solo",
            cityName = ""
        )

        with(pinpointNewPageViewModel.getAddress()) {
            assertTrue(districtName.isBlank())
            assertTrue(cityName.isBlank())
        }
    }

    @Test
    fun `verify set district and city name when has city name value and distrcit name is null`() {
        pinpointNewPageViewModel.setDistrictAndCityName(
            districtName = null,
            cityName = "Solo"
        )

        with(pinpointNewPageViewModel.getAddress()) {
            assertTrue(districtName.isBlank())
            assertTrue(cityName.isBlank())
        }
    }

    @Test
    fun `verify set district and city name when has city name value and distrcit name is empty`() {
        pinpointNewPageViewModel.setDistrictAndCityName(
            districtName = "",
            cityName = "Solo"
        )

        with(pinpointNewPageViewModel.getAddress()) {
            assertTrue(districtName.isBlank())
            assertTrue(cityName.isBlank())
        }
    }

    @Test
    fun `verify set district and city name when district name and city name null`() {
        pinpointNewPageViewModel.setDistrictAndCityName(
            districtName = null,
            cityName = null
        )

        with(pinpointNewPageViewModel.getAddress()) {
            assertTrue(districtName.isBlank())
            assertTrue(cityName.isBlank())
        }
    }

    @Test
    fun `verify set district and city name when district name and city name has data`() {
        pinpointNewPageViewModel.setDistrictAndCityName(
            districtName = "Bantul",
            cityName = "Yogyakarta"
        )

        with(pinpointNewPageViewModel.getAddress()) {
            assertTrue(districtName.isNotBlank())
            assertTrue(cityName.isNotBlank())
        }
    }

    @Test
    fun `verify set lat long is correctly`() {
        val latitude = -6.121435
        val longitude = 106.774124

        pinpointNewPageViewModel.setLatLong(
            lat = latitude,
            long = longitude
        )

        with(pinpointNewPageViewModel.getAddress()) {
            assertEquals(this.latitude, latitude.toString())
            assertEquals(this.longitude, longitude.toString())
        }
    }

    @Test
    fun `verify when getGeocodeByDistrictAndCityName is success`() {
        val mockLocation = spyk(Location())

        val mockResponse = spyk(
            MapsGeocodeResponse(
                keroAddressGeocode = spyk(
                    KeroAddressGeocode(
                        data = arrayListOf(
                            spyk(
                                CoordinateModel().apply {
                                    geometry = spyk(
                                        Geometry().apply {
                                            location = mockLocation
                                        }
                                    )
                                }
                            )
                        )
                    )
                )
            )
        )

        coEvery {
            mapsGeocodeUseCase.invoke(any())
        } returns mockResponse

        pinpointNewPageViewModel.getGeocodeByDistrictAndCityName()

        verify {
            mapsGeocodeStateObserver.onChanged(MapsGeocodeState.Success(mockLocation))
        }
    }

    @Test
    fun `verify when getGeocodeByDistrictAndCityName empty location`() {
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
    fun `verify when getGeocodeByDistrictAndCityName error`() {
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
    fun `verify when send share address request error with error message`() {
        val errorMessage = "error"

        coEvery {
            mapsGeocodeUseCase.invoke(any())
        } throws Exception(errorMessage)

        pinpointNewPageViewModel.getGeocodeByDistrictAndCityName()

        verify {
            mapsGeocodeStateObserver.onChanged(MapsGeocodeState.Fail(errorMessage))
        }
    }

    @Test
    fun `verify when send share address request error without error message`() {
        coEvery {
            mapsGeocodeUseCase.invoke(any())
        } throws Exception()

        pinpointNewPageViewModel.getGeocodeByDistrictAndCityName()

        verify {
            mapsGeocodeStateObserver.onChanged(MapsGeocodeState.Fail(""))
        }
    }
}
