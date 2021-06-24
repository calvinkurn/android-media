package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.entity.response.AutoFillResponse
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.response.AutoCompleteResponse
import com.tokopedia.logisticCommon.data.response.GetDistrictBoundaryResponse
import com.tokopedia.logisticCommon.data.response.GetDistrictResponse
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_boundary.DistrictBoundaryResponseUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
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

class PinpointNewPageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repo: KeroRepository = mockk(relaxed = true)
    private val getDistrictMapper = GetDistrictMapper()
    private val districtBoundaryMapper = DistrictBoundaryMapper()

    private lateinit var pinpointNewPageViewModel: PinpointNewPageViewModel

    private val autofillDistrictDataObserver: Observer<Result<KeroMapsAutofill>> = mockk(relaxed = true)
    private val districtLocationObserver: Observer<Result<GetDistrictDataUiModel>> = mockk(relaxed = true)
    private val autoCompleteDataObserver: Observer<Result<AutoCompleteResponse>> = mockk(relaxed = true)
    private val districtBoundaryObserver: Observer<Result<DistrictBoundaryResponseUiModel>> = mockk(relaxed = true)

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        pinpointNewPageViewModel = PinpointNewPageViewModel(repo, getDistrictMapper, districtBoundaryMapper)
        pinpointNewPageViewModel.autofillDistrictData.observeForever(autofillDistrictDataObserver)
        pinpointNewPageViewModel.autoCompleteData.observeForever(autoCompleteDataObserver)
        pinpointNewPageViewModel.districtLocation.observeForever(districtLocationObserver)
        pinpointNewPageViewModel.districtBoundary.observeForever(districtBoundaryObserver)
    }

    @Test
    fun `Get District Data Success`() {
        coEvery { repo.getDistrictGeocode(any()) } returns AutoFillResponse()
        pinpointNewPageViewModel.getDistrictData(1134.5, -6.4214)
        verify { autofillDistrictDataObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get District Data Faile`() {
        coEvery { repo.getDistrictGeocode(any()) } throws defaultThrowable
        pinpointNewPageViewModel.getDistrictData(1134.5, -6.4214)
        verify { autofillDistrictDataObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Get AutoComplete Data Success`() {
        coEvery { repo.getAutoComplete(any()) } returns AutoCompleteResponse()
        pinpointNewPageViewModel.getAutoComplete("Setiabudi")
        verify { autoCompleteDataObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get AutoComplete Data Fail`() {
        coEvery { repo.getAutoComplete(any()) } throws defaultThrowable
        pinpointNewPageViewModel.getAutoComplete("Setiabudi")
        verify { autoCompleteDataObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Get District Location Success`() {
        coEvery { repo.getDistrict(any()) } returns GetDistrictResponse()
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
        pinpointNewPageViewModel.getDistrictBoundaries(11986)
        verify { districtBoundaryObserver.onChanged(match { it is Success }) }
    }


    @Test
    fun `Get District Boundaries Fail`() {
        coEvery { repo.getDistrictBoundaries(any()) } throws defaultThrowable
        pinpointNewPageViewModel.getDistrictBoundaries(11986)
        verify { districtBoundaryObserver.onChanged(match { it is Fail }) }
    }

}