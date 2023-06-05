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
import com.tokopedia.logisticaddaddress.common.uimodel.district_boundary.DistrictBoundaryResponseUiModel
import com.tokopedia.logisticaddaddress.common.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.data.entity.mapsgeocode.KeroAddressGeocode
import com.tokopedia.logisticaddaddress.data.entity.mapsgeocode.MapsGeocodeResponse
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.usecase.MapsGeocodeUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.uimodel.MapsGeocodeState
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.DistrictCenterUiModel
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
}
