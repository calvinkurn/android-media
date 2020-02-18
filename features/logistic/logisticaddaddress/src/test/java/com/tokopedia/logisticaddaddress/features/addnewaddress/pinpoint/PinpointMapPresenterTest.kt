package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.usecase.DistrictBoundaryUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticdata.domain.usecase.RevGeocodeUseCase
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

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
})