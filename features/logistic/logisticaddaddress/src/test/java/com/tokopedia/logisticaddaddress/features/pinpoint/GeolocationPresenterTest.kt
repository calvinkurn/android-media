package com.tokopedia.logisticaddaddress.features.pinpoint

import android.content.Context
import com.tokopedia.logisticaddaddress.data.RetrofitInteractorImpl
import com.tokopedia.logisticaddaddress.domain.mapper.GeolocationMapper
import com.tokopedia.logisticdata.domain.usecase.RevGeocodeUseCase
import com.tokopedia.user.session.UserSession
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object GeolocationPresenterTest : Spek({

    lateinit var presenter: GeolocationPresenter
    val context: Context = mockk(relaxed = true)
    val retrofitImpl: RetrofitInteractorImpl = mockk(relaxUnitFun = true)
    val userSession: UserSession = mockk()
    val revGeocode: RevGeocodeUseCase = mockk(relaxUnitFun = true)
    val mapFragment: GoogleMapFragment = mockk()
    val geoMapper: GeolocationMapper = mockk()

    beforeEachTest {
        presenter = GeolocationPresenter(
                context,
                retrofitImpl,
                userSession,
                revGeocode,
                mapFragment,
                geoMapper
        )
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

    Feature("connect google api") {
        Scenario("") {
            When("on connected") {
                presenter.connectGoogleApi()
            }
            Then("nothing to verify") {

            }
        }
    }

    Feature("disconnect google api") {
        Scenario("") {
            When("on disconnect") {
                presenter.disconnectGoogleApi()
            }
            Then("nothing to verify") {

            }
        }
    }

})