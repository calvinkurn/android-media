package com.tokopedia.logisticaddaddress.features.district_recommendation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.entity.response.AutoFillResponse
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeCoroutineUseCase
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper
import com.tokopedia.logisticaddaddress.domain.model.AddressResponse
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictRecommendationResponse
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecommendationCoroutineUseCase
import com.tokopedia.logisticaddaddress.features.district_recommendation.uimodel.AutofillUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DiscomViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var discomViewModel: DiscomViewModel
    private val testDispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers

    private val districtRecomObserver: Observer<Result<AddressResponse>> = mockk(relaxed = true)
    private val autofillObserver: Observer<Result<AutofillUiModel>> = mockk(relaxed = true)
    private val loadingObserver: Observer<Boolean> = mockk(relaxed = true)

    private val reverseGeoCode: RevGeocodeCoroutineUseCase = mockk(relaxed = true)
    private val getDistrictRecommendation: GetDistrictRecommendationCoroutineUseCase =
        mockk(relaxed = true)

    private val mapper = DistrictRecommendationMapper()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatchers.coroutineDispatcher)
        discomViewModel =
            DiscomViewModel(testDispatchers, reverseGeoCode, getDistrictRecommendation, mapper)
        discomViewModel.autoFill.observeForever(autofillObserver)
        discomViewModel.districtRecommendation.observeForever(districtRecomObserver)
        discomViewModel.loading.observeForever(loadingObserver)
    }

    @Test
    fun `WHEN load get district recom success THEN return data`() {
        // given
        val response = DistrictRecommendationResponse()
        coEvery { getDistrictRecommendation(any()) } returns response
        val model = mapper.transform(response)

        // when
        discomViewModel.loadData("", 1)

        // then
        verifyOrder {
            loadingObserver.onChanged(match { it })
            loadingObserver.onChanged(match { !it })
            districtRecomObserver.onChanged(match { it is Success })
        }
    }

    @Test
    fun `WHEN load get district recom failed THEN throw error`() {
        // given
        val error = Exception("error")
        coEvery { getDistrictRecommendation(any()) } throws error

        // when
        discomViewModel.loadData("", 1)

        // then
        verifyOrder {
            loadingObserver.onChanged(match { it })
            loadingObserver.onChanged(match { !it })
            districtRecomObserver.onChanged(match { it == Fail(error) })
        }
    }

    @Test
    fun `WHEN get address detail from lat long success THEN return data`() {
        // given
        val lat = 6.34434342
        val long = 27.3495023
        val response = AutoFillResponse()
        coEvery { reverseGeoCode(any()) } returns response

        // when
        discomViewModel.reverseGeoCode(lat, long)

        // then
        verifyOrder {
            loadingObserver.onChanged(match { it })
            loadingObserver.onChanged(match { !it })
            autofillObserver.onChanged(
                match {
                    it == Success(
                        AutofillUiModel(
                            response.keroMapsAutofill.data,
                            lat,
                            long
                        )
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN get address detail from lat long error circuit breaker THEN show error toaster`() {
        // given
        val lat = 6.34434342
        val long = 27.3495023
        val response = AutoFillResponse(KeroMapsAutofill(errorCode = 101, messageError = listOf("error")))
        coEvery { reverseGeoCode(any()) } returns response

        // when
        discomViewModel.reverseGeoCode(lat, long)

        // then
        verifyOrder {
            loadingObserver.onChanged(match { it })
            loadingObserver.onChanged(match { !it })
            autofillObserver.onChanged(
                match {
                    (it as Fail).throwable.message == "Oops, alamat gagal dipilih. Silakan coba lagi."
                }
            )
        }
    }

    @Test
    fun `WHEN get address detail from lat long error FOREIGN_COUNTRY_MESSAGE THEN show error toaster`() {
        // given
        val lat = 6.34434342
        val long = 27.3495023
        val response = AutoFillResponse(KeroMapsAutofill(errorCode = 0, messageError = listOf("Lokasi di luar Indonesia.")))
        coEvery { reverseGeoCode(any()) } returns response

        // when
        discomViewModel.reverseGeoCode(lat, long)

        // then
        verifyOrder {
            loadingObserver.onChanged(match { it })
            loadingObserver.onChanged(match { !it })
            autofillObserver.onChanged(
                match {
                    (it as Fail).throwable.message == "Lokasi di luar Indonesia."
                }
            )
        }
    }

    @Test
    fun `WHEN get address detail from lat long error LOCATION_NOT_FOUND_MESSAGE THEN show error toaster`() {
        // given
        val lat = 6.34434342
        val long = 27.3495023
        val response = AutoFillResponse(KeroMapsAutofill(errorCode = 0, messageError = listOf("Lokasi gagal ditemukan")))
        coEvery { reverseGeoCode(any()) } returns response

        // when
        discomViewModel.reverseGeoCode(lat, long)

        // then
        verifyOrder {
            loadingObserver.onChanged(match { it })
            loadingObserver.onChanged(match { !it })
            autofillObserver.onChanged(
                match {
                    (it as Fail).throwable.message == "Lokasi gagal ditemukan"
                }
            )
        }
    }
}
