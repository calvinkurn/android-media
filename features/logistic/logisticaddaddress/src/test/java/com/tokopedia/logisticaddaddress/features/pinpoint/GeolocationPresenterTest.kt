package com.tokopedia.logisticaddaddress.features.pinpoint

import com.google.android.gms.maps.model.LatLng
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.uimodel.CoordinateUiModel
import com.tokopedia.logisticaddaddress.data.RetrofitInteractor
import com.tokopedia.logisticaddaddress.data.RetrofitInteractorImpl
import com.tokopedia.logisticaddaddress.domain.mapper.GeolocationMapper
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeUseCase
import com.tokopedia.user.session.UserSession
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable

object GeolocationPresenterTest : Spek({

    lateinit var presenter: GeolocationPresenter
    val retrofitImpl: RetrofitInteractorImpl = mockk(relaxUnitFun = true)
    val userSession: UserSession = mockk(relaxed = true){
        every { userId } returns "abcde"
        every { deviceId } returns "cdef"
    }
    val revGeocode: RevGeocodeUseCase = mockk(relaxUnitFun = true)
    val mapFragment: GoogleMapFragment = mockk(relaxed = true)
    val geoMapper = GeolocationMapper()

    beforeEachTest {
        presenter = GeolocationPresenter(
                retrofitImpl,
                userSession,
                revGeocode,
                mapFragment,
                geoMapper
        )
    }

    Feature("geocoding") {
        Scenario("success") {
            val res = CoordinateUiModel().apply {
                coordinate = LatLng(12.0, 12.0)
            }
            Given("success response") {
                every { retrofitImpl.generateLatLng(any(), any()) } answers {
                    secondArg<RetrofitInteractor.GenerateLatLongListener>().onSuccess(res)
                }
            }
            When("executed") {
                presenter.geoCode("123")
            }
            Then("view moves to map") {
                verify { mapFragment.moveMap(res.coordinate) }
            }
        }
        Scenario("error") {
            val err = "error msg"
            Given("success response") {
                every { retrofitImpl.generateLatLng(any(), any()) } answers {
                    secondArg<RetrofitInteractor.GenerateLatLongListener>().onError(err)
                }
            }
            When("executed") {
                presenter.geoCode("123")
            }
            Then("view moves to map") {
                verify { mapFragment.toastMessage(err) }
            }
        }
    }

    Feature("reverse geocode") {
        Scenario("success") {
            val testFormatted = "Jl Senang"
            val res = KeroMapsAutofill(data = Data(
                    formattedAddress = testFormatted
            ))
            Given("success response") {
                every { revGeocode.execute(any()) } returns Observable.just(res)
            }
            When("executed") {
                presenter.getReverseGeoCoding("99", "99")
            }
            Then("view is called") {
                verify {
                    mapFragment.setLoading(true)
                    mapFragment.setLoading(false)
                    mapFragment.setValuePointer(testFormatted)
                    mapFragment.setNewLocationPass(any())
                }
            }
        }
    }

    Feature("get interactor") {
        Scenario("called") {
            var i: Any? = null
            When("on destroy is called") {
                i = presenter.interactor
            }
            Then("unsubscribed") {
                assertTrue(i is RetrofitInteractor)
            }
        }
    }

    Feature("on destroy") {
        Scenario("called") {
            When("on destroy is called") {
                presenter.onDestroy()
            }
            Then("unsubscribed") {
                verify {
                    retrofitImpl.unSubscribe()
                    revGeocode.unsubscribe()
                }
            }
        }
    }

})