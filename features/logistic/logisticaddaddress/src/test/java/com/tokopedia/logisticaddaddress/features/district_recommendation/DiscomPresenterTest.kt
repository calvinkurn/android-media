package com.tokopedia.logisticaddaddress.features.district_recommendation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeUseCase
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.domain.model.AddressResponse
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecommendation
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRequestUseCase
import com.tokopedia.logisticaddaddress.helper.DiscomDummyProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.Subscriber

class DiscomPresenterTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val firstPage = 1

    private val view: DiscomContract.View = mockk(relaxed = true)
    private val getDistrictRequestUseCase: GetDistrictRequestUseCase = mockk(relaxUnitFun = true)
    private val getDistrictRecommendation: GetDistrictRecommendation = mockk(relaxUnitFun = true)
    private val revGeocodeUseCase: RevGeocodeUseCase = mockk(relaxed = true)
    private val mapper = DistrictRecommendationMapper()

    lateinit var presenter: DiscomPresenter

    private val throwable = Throwable()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter = DiscomPresenter(getDistrictRequestUseCase, revGeocodeUseCase, getDistrictRecommendation, mapper)
        presenter.attach(view)
    }

    @Test
    fun `load data with data`() {
        val expected = DiscomDummyProvider.getSuccessResponse()

        every { getDistrictRecommendation.execute(any(), any())
        } answers  {
            Observable.just(expected)
        }

        presenter.loadData("jak", firstPage)

        verifyOrder {
            view.setLoadingState(true)
            view.setLoadingState(false)
            view.renderData(withArg { org.assertj.core.api.Assertions.assertThat(it).isNotEmpty }, expected.keroDistrictRecommendation.nextAvailable)
        }
    }

    @Test
    fun `load data with data return error`() {
        every { getDistrictRecommendation.execute(any(), any())
        } answers  {
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

        every { getDistrictRecommendation.execute(any(), any())
        } answers  {
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
    fun `load data with token data`() {
        val expected = AddressResponse().apply {
            addresses = arrayListOf(
                    Address().apply { cityName = "Jakarta" }
            )
            isNextAvailable = false
        }
        every { getDistrictRequestUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<AddressResponse>>().onNext(expected)
        }

        presenter.loadData("jak", firstPage, Token())

        verifyOrder {
            view.renderData(expected.addresses, expected.isNextAvailable)
        }
    }

    @Test
    fun `load data with token data return error`() {

        every { getDistrictRequestUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<AddressResponse>>().onError(throwable)
        }

        presenter.loadData("jak", firstPage, Token())

        verifyOrder {
            view.showGetListError(throwable)
        }
    }

    @Test
    fun `load data with token data return empty`() {
        val expected = AddressResponse().apply {
            addresses = arrayListOf()
            isNextAvailable = false
        }

        every { getDistrictRequestUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<AddressResponse>>().onNext(expected)
        }

        presenter.loadData("jak", firstPage, Token())

        verifyOrder {
            view.showEmpty()
        }
    }

    // TODO autofill

    @Test
    fun `autofill succcess`() {
        val keroMaps = KeroMapsAutofill(data = Data(title = "city test"), messageError = listOf())

        every { revGeocodeUseCase.execute(any())
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

        every { revGeocodeUseCase.execute(any())
        } answers {
            Observable.just(keroMaps)
        }

        presenter.autoFill(0.1, 0.1)

        verifyOrder {
            view.showToasterError()
        }
    }

    @Test
    fun `autofill success with foreign country`() {
        val keroMaps = KeroMapsAutofill(data = Data(title = "city test"), messageError = listOf("Lokasi di luar Indonesia."))
        every { revGeocodeUseCase.execute(any())
        } answers {
            Observable.just(keroMaps)
        }

        presenter.autoFill(0.1, 0.1)

        verifyOrder {
            view.showToasterError()
        }
    }

    @Test
    fun `autofill success with not found location`() {
        val keroMaps = KeroMapsAutofill(data = Data(title = "city test"), messageError = listOf("Lokasi gagal ditemukan"))
        every { revGeocodeUseCase.execute(any())
        } answers {
            Observable.just(keroMaps)
        }

        presenter.autoFill(0.1, 0.1)

        verifyOrder {
            view.showToasterError()
        }
    }

    @Test
    fun `onDetach`() {
        presenter.detach()

        verifyOrder {
            getDistrictRequestUseCase.unsubscribe()
            getDistrictRecommendation.unsubscribe()
        }
    }
}