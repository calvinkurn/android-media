package com.tokopedia.logisticaddaddress.features.district_recommendation

import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecommendation
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRequestUseCase
import com.tokopedia.logisticaddaddress.helper.DiscomDummyProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import rx.Observable

class DiscomPresenterTest {

    private val first_page: Int = 1
    val view: DiscomContract.View = mockk(relaxed = true)
    val rest: GetDistrictRequestUseCase = mockk()
    val gql: GetDistrictRecommendation = mockk()
    val mapper: DistrictRecommendationMapper = DistrictRecommendationMapper()
    lateinit var presenter: DiscomPresenter

    @Before
    fun setup() {
        presenter = DiscomPresenter(rest, gql, mapper)
        presenter.attach(view)
    }

    @Test
    fun loadDataWithData_returnSuccess() {
        val query = "jak"
        val expected = DiscomDummyProvider.getSuccessResponse()
        every { gql.execute(any(), any()) } answers { Observable.just(expected) }

        presenter.loadData(query, first_page)

        verify {
            view.renderData(withArg { assertThat(it).isNotEmpty }, expected.keroDistrictRecommendation.nextAvailable)
        }
        verifyOrder {
            view.setLoadingState(true)
            view.setLoadingState(false)
        }
    }

    @Test
    fun loadDataWithData_returnError() {
        val query = "jak"
        val throwable = Throwable()
        every { gql.execute(any(), any()) } answers { Observable.error(throwable)}

        presenter.loadData(query, first_page)

        verify {
            view.showGetListError(throwable)
        }
        verifyOrder {
            view.setLoadingState(true)
            view.setLoadingState(false)
        }
    }

    @Test
    fun loadDataWithData_returnEmpty() {
        val query = "qwr"
        val datum = DiscomDummyProvider.getEmptyResponse()
        every { gql.execute(any(), any()) } answers { Observable.just(datum) }

        presenter.loadData(query, first_page)

        verify {
            view.showEmpty()
        }
        verifyOrder {
            view.setLoadingState(true)
            view.setLoadingState(false)
        }
    }

}