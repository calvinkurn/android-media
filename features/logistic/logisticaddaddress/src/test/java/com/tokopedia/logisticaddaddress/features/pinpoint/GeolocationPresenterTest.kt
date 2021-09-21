package com.tokopedia.logisticaddaddress.features.pinpoint

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.uimodel.CoordinateUiModel
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeUseCase
import com.tokopedia.logisticaddaddress.data.RetrofitInteractor
import com.tokopedia.logisticaddaddress.data.RetrofitInteractorImpl
import com.tokopedia.logisticaddaddress.domain.mapper.GeolocationMapper
import com.tokopedia.user.session.UserSession
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable

class GeolocationPresenterTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var presenter: GeolocationPresenter
    val retrofitImpl: RetrofitInteractorImpl = mockk(relaxUnitFun = true)
    val userSession: UserSession = mockk(relaxed = true){
        every { userId } returns "abcde"
        every { deviceId } returns "cdef"
    }
    val revGeocode: RevGeocodeUseCase = mockk(relaxUnitFun = true)
    val mapFragment: GoogleMapFragment = mockk(relaxed = true)
    val geoMapper = GeolocationMapper()

    @Before
    fun setup() {
        presenter = GeolocationPresenter(retrofitImpl, userSession, revGeocode, mapFragment, geoMapper)
    }

    @Test
    fun `geocoding success`() {
        val res = CoordinateUiModel().apply {
            coordinate = LatLng(12.0, 12.0)
        }

        every { retrofitImpl.generateLatLng(any(), any())
        } answers {
            secondArg<RetrofitInteractor.GenerateLatLongListener>().onSuccess(res)
        }

        presenter.geoCode("123")

        verifyOrder {
            mapFragment.moveMap(res.coordinate)
        }
    }

    @Test
    fun `geocoing error`() {
        val err = "error msg"

        every { retrofitImpl.generateLatLng(any(), any())
        } answers {
            secondArg<RetrofitInteractor.GenerateLatLongListener>().onError(err)
        }

        presenter.geoCode("123")

        verifyOrder {
            mapFragment.toastMessage(err)
        }
    }

    @Test
    fun `reverse geocode success`() {
        val testFormatted = "Jl Senang"
        val res = KeroMapsAutofill(data = Data(
                formattedAddress = testFormatted
        ))

        every {
            revGeocode.execute(any())
        } returns Observable.just(res)

        presenter.getReverseGeoCoding("99", "99")

        verifyOrder {
            mapFragment.setLoading(true)
            mapFragment.setLoading(false)
            mapFragment.setValuePointer(testFormatted)
            mapFragment.setNewLocationPass(any())
        }
    }

    @Test
    fun `get interactor`() {
        var i: Any? = null

        i = presenter.interactor

        Assert.assertTrue(i is RetrofitInteractor)
    }

    @Test
    fun `on destroy`() {
        presenter.onDestroy()

        verifyOrder {
            retrofitImpl.unSubscribe()
            revGeocode.unsubscribe()
        }
    }
}