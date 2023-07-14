package com.tokopedia.logisticaddaddress.features.district_recommendation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeUseCase
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecommendation
import com.tokopedia.logisticaddaddress.helper.DiscomDummyProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyOrder
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable

class DiscomPresenterTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val firstPage = 1

    private val view: DiscomContract.View = mockk(relaxed = true)
    private val getDistrictRecommendation: GetDistrictRecommendation = mockk(relaxUnitFun = true)
    private val revGeocodeUseCase: RevGeocodeUseCase = mockk(relaxed = true)
    private val mapper = DistrictRecommendationMapper()

    lateinit var presenter: DiscomPresenter

    private val throwable = Throwable()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter = DiscomPresenter(revGeocodeUseCase, getDistrictRecommendation, mapper)
        presenter.attach(view)
    }

    @Test
    fun `load data with data`() {
        val expected = DiscomDummyProvider.getSuccessResponse()

        every {
            getDistrictRecommendation.execute(any(), any())
        } answers {
            Observable.just(expected)
        }

        presenter.loadData("jak", firstPage)

        verifyOrder {
            view.setLoadingState(true)
            view.setLoadingState(false)
            view.renderData(any(), expected.keroDistrictRecommendation.nextAvailable)
        }
    }

    @Test
    fun `load data with data return error`() {
        every {
            getDistrictRecommendation.execute(any(), any())
        } answers {
            Observable.error(throwable)
        }

        presenter.loadData("jak", firstPage)

        verifyOrder {
            view.setLoadingState(true)
            view.setLoadingState(false)
            view.showGetListError(throwable)
        }
    }

    @Test
    fun `load data with data return empty`() {
        val expected = DiscomDummyProvider.getEmptyResponse()

        every {
            getDistrictRecommendation.execute(any(), any())
        } answers {
            Observable.just(expected)
        }

        presenter.loadData("jak", firstPage)

        verifyOrder {
            view.setLoadingState(true)
            view.setLoadingState(false)
            view.showEmpty()
        }
    }

    @Test
    fun `autofill succcess`() {
        val keroMaps = KeroMapsAutofill(data = Data(title = "city test"), messageError = listOf())

        every {
            revGeocodeUseCase.execute(any())
        } answers {
            Observable.just(keroMaps)
        }

        val lat = 0.1
        val lng = 0.1

        presenter.autoFill(lat, lng)

        verifyOrder {
            view.setResultDistrict(keroMaps.data, lat, lng)
        }
    }

    @Test
    fun `autofill success with circuit breaker on code`() {
        val keroMaps = KeroMapsAutofill(data = Data(title = "city test"), messageError = listOf("message error"), errorCode = 101)

        every {
            revGeocodeUseCase.execute(any())
        } answers {
            Observable.just(keroMaps)
        }

        presenter.autoFill(0.1, 0.1)

        verifyOrder {
            view.showToasterError("")
        }
    }

    @Test
    fun `autofill success with foreign country`() {
        val keroMaps = KeroMapsAutofill(data = Data(title = "city test"), messageError = listOf("Lokasi di luar Indonesia."))
        every {
            revGeocodeUseCase.execute(any())
        } answers {
            Observable.just(keroMaps)
        }

        presenter.autoFill(0.1, 0.1)

        verifyOrder {
            view.showToasterError("Lokasi di luar Indonesia.")
        }
    }

    @Test
    fun `autofill success with not found location`() {
        val keroMaps = KeroMapsAutofill(data = Data(title = "city test"), messageError = listOf("Lokasi gagal ditemukan"))
        every {
            revGeocodeUseCase.execute(any())
        } answers {
            Observable.just(keroMaps)
        }

        presenter.autoFill(0.1, 0.1)

        verifyOrder {
            view.showToasterError("Lokasi gagal ditemukan")
        }
    }

    @Test
    fun `autofill fails return error`() {
        val defaultThrowable = spyk(Throwable())
        every { revGeocodeUseCase.execute(any()) } answers {
            Observable.error(defaultThrowable)
        }

        presenter.autoFill(0.1, 0.1)

        verifyOrder {
            defaultThrowable.printStackTrace()
        }
    }

    @Test
    fun `verify set gms availability flag is correct`() {
        val gmsAvailable = false
        presenter.setLocationAvailability(gmsAvailable)

        Assert.assertEquals(presenter.getLocationAvailability(), gmsAvailable)
    }

    @Test
    fun `onDetach`() {
        presenter.detach()

        verifyOrder {
            getDistrictRecommendation.unsubscribe()
        }
    }
}
