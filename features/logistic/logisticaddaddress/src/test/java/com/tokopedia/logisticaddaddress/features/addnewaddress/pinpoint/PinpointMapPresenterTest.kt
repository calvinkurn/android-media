package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import com.google.android.gms.maps.model.LatLng
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.usecase.DistrictBoundaryUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_boundary.DistrictBoundaryGeometryUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_boundary.DistrictBoundaryResponseUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.Subscriber

object PinpointMapPresenterTest : Spek({

    val getDistrictUseCase: GetDistrictUseCase = mockk(relaxUnitFun = true)
    val revGeoCodeUseCase: RevGeocodeUseCase = mockk(relaxUnitFun = true)
    val districtBoundUseCase: DistrictBoundaryUseCase = mockk(relaxUnitFun = true)
    val districtBoundMapper: DistrictBoundaryMapper = mockk()
    val view: PinpointMapView = mockk(relaxed = true)
    lateinit var presenter: PinpointMapPresenter

    beforeEachTest {
        presenter = PinpointMapPresenter(getDistrictUseCase, revGeoCodeUseCase,
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
            val successModel = GetDistrictDataUiModel(
                    districtId = 1
            )
            Given("usecase gives success") {
                every { getDistrictUseCase.execute(any()) } returns Observable.just(successModel)
            }
            When("executed") {
                presenter.getDistrict("123")
            }
            Then("on success is called") {
                verify {
                    view.onSuccessPlaceGetDistrict(successModel)
                }
            }
        }
    }

    Feature("auto fill") {
        Scenario("has default lat long") {
            When("executed with default lat long") {
                presenter.autoFill(-6.175794, 106.826457, 5.0f)
            }
            Then("view shows undetected dialog") {
                verify {
                    view.showUndetectedDialog()
                }
            }
        }

        Scenario("success") {
            val keroMaps = KeroMapsAutofill(data = Data(title = "city test"), messageError = listOf())
            Given("success response") {
                every { revGeoCodeUseCase.execute(any()) } returns Observable.just(keroMaps)
            }
            When("executed") {
                presenter.autoFill(0.1, 0.1, 0.0f)
            }
            Then("on success is called") {
                verify { view.onSuccessAutofill(keroMaps.data) }
            }
        }

        Scenario("success with foreign country") {
            val keroMaps = KeroMapsAutofill(data = Data(title = "city test"), messageError = listOf("Lokasi di luar Indonesia."))
            Given("response with foreign location error") {
                every { revGeoCodeUseCase.execute(any()) } returns Observable.just(keroMaps)
            }
            When("executed") {
                presenter.autoFill(0.1, 0.1, 0.0f)
            }
            Then("view shows out of reach dialog") {
                verify { view.showOutOfReachDialog() }
            }
        }

        Scenario("success with not found location") {
            val keroMaps = KeroMapsAutofill(data = Data(title = "city test"), messageError = listOf("Lokasi gagal ditemukan"))
            Given("response with location not found error") {
                every { revGeoCodeUseCase.execute(any()) } returns Observable.just(keroMaps)
            }
            When("executed") {
                presenter.autoFill(0.1, 0.1, 0.0f)
            }
            Then("view shows out of reach dialog") {
                verify { view.showLocationNotFoundCTA() }
            }
        }
    }

    Feature("district boundary") {
        Scenario("success") {
            val anyGql = GraphqlResponse(null, null, false)
            val listBoundaries = mutableListOf(LatLng(12.4, 12.5))
            val response = DistrictBoundaryResponseUiModel(
                    geometry = DistrictBoundaryGeometryUiModel(listBoundaries)
            )
            Given("success response") {
                every { districtBoundUseCase.execute(any(), any()) } answers {
                    secondArg<Subscriber<GraphqlResponse>>().onNext(anyGql)
                }
                every { districtBoundMapper.map(anyGql) } returns response
            }

            When("executed") {
                presenter.getDistrictBoundary(0, "asdn", 0)
            }

            Then("view shows boundary") {
                verify { view.showBoundaries(listBoundaries) }
            }
        }
    }

    Feature("get unnamed") {
        Scenario("set") {
            val address = SaveAddressDataModel(formattedAddress = "Unnamed Road, Jl Testimoni", selectedDistrict = "Testimoni")
            var result: SaveAddressDataModel? = null
            Given("set address") {
                presenter.setAddress(address)
            }
            When("retrieved") {
                result = presenter.getUnnamedRoadModelFormat()
            }
            Then("Unnamed road is removed") {
                assertFalse(result?.formattedAddress?.contains("Unnamed Road") ?: true)
                assertEquals(result?.formattedAddress, result?.selectedDistrict)
            }
        }

    }
})