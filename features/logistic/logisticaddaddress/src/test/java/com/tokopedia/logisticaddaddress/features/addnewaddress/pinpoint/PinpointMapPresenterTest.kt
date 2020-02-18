package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.usecase.DistrictBoundaryUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictResponseUiModel
import com.tokopedia.logisticdata.domain.usecase.RevGeocodeUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber

object PinpointMapPresenterTest : Spek({

    val getDistrictUseCase: GetDistrictUseCase = mockk(relaxUnitFun = true)
    val getDistrictMapper: GetDistrictMapper = mockk()
    val revGeoCodeUseCase: RevGeocodeUseCase = mockk(relaxUnitFun = true)
    val districtBoundUseCase: DistrictBoundaryUseCase = mockk(relaxUnitFun = true)
    val districtBoundMapper: DistrictBoundaryMapper = mockk()
    val view: PinpointMapListener = mockk()
    lateinit var presenter: PinpointMapPresenter

    beforeEachTest {
        presenter = PinpointMapPresenter(getDistrictUseCase, getDistrictMapper, revGeoCodeUseCase,
                districtBoundUseCase, districtBoundMapper)
        presenter.attachView(view)
    }

    Feature("detach") {
        Scenario("detached") {
            When("detached") {
                presenter.detachView()
            }
            Then("usecases are unsubscribed") {
                verify {
                    getDistrictUseCase.unsubscribe()
                    revGeoCodeUseCase.unsubscribe()
                    districtBoundUseCase.unsubscribe()
                }
            }
        }
    }

    Feature("get district") {
        Scenario("get succcess district") {
            val gqlSuccess = GraphqlResponse(mapOf(), mapOf(), false)
            val uiModel = GetDistrictDataUiModel(title = "city")
            val successModel = GetDistrictResponseUiModel(
                    data = uiModel
            )
            Given("usecase gives success") {
                every { getDistrictUseCase.execute(any(), any()) } answers {
                    secondArg<Subscriber<GraphqlResponse>>().onNext(gqlSuccess)
                }
                every { getDistrictMapper.map(gqlSuccess) } returns successModel
            }
            Then("on success is called") {
                verify {
                    view.onSuccessPlaceGetDistrict(uiModel)
                }
            }
        }
    }
})