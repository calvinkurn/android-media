package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeUseCase
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.usecase.DistrictBoundaryUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_boundary.DistrictBoundaryGeometryUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_boundary.DistrictBoundaryResponseUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.Subscriber

class PinpointMapPresenterTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val getDistrictUseCase: GetDistrictUseCase = mockk(relaxUnitFun = true)
    val revGeoCodeUseCase: RevGeocodeUseCase = mockk(relaxUnitFun = true)
    val districtBoundUseCase: DistrictBoundaryUseCase = mockk(relaxUnitFun = true)
    val districtBoundMapper: DistrictBoundaryMapper = mockk()
    val view: PinpointMapView = mockk(relaxed = true)
    lateinit var presenter: PinpointMapPresenter

    @Before
    fun setup() {
        presenter = PinpointMapPresenter(getDistrictUseCase, revGeoCodeUseCase, districtBoundUseCase, districtBoundMapper)
        presenter.attachView(view)
    }

    @Test
    fun `get success district`() {
        val successModel = GetDistrictDataUiModel(
                districtId = 1)

        every { getDistrictUseCase.execute(any())
        } answers {
            Observable.just(successModel)
        }

        presenter.getDistrict("123")

        verifyOrder {
            view.onSuccessPlaceGetDistrict(successModel)
        }
    }

    @Test
    fun `autofill`() {
        presenter.autoFill(-6.175794, 106.826457, 5.0f)

        verifyOrder {
            view.showUndetectedDialog()
        }
    }

    @Test
    fun `autofill succcess`() {
        val keroMaps = KeroMapsAutofill(data = Data(title = "city test"), messageError = listOf())

        every { revGeoCodeUseCase.execute(any())
        } answers {
            Observable.just(keroMaps)
        }

        presenter.autoFill(0.1, 0.1, 0.0f)

        verifyOrder {
            view.onSuccessAutofill(keroMaps.data)
        }
    }

    @Test
    fun `autofill success with foreign country`() {
        val keroMaps = KeroMapsAutofill(data = Data(title = "city test"), messageError = listOf("Lokasi di luar Indonesia."))
        every { revGeoCodeUseCase.execute(any())
        } answers {
            Observable.just(keroMaps)
        }

        presenter.autoFill(0.1, 0.1, 0.0f)

        verifyOrder {
            view.showOutOfReachDialog()
        }
    }

    @Test
    fun `autofill success with not found location`() {
        val keroMaps = KeroMapsAutofill(data = Data(title = "city test"), messageError = listOf("Lokasi gagal ditemukan"))
        every { revGeoCodeUseCase.execute(any())
        } answers {
            Observable.just(keroMaps)
        }

        presenter.autoFill(0.1, 0.1, 0.0f)

        verifyOrder {
            view.showLocationNotFoundCTA()
        }
    }

    @Test
    fun `district boundary success`() {
        val anyGql = GraphqlResponse(null, null, false)
        val listBoundaries = mutableListOf(LatLng(12.4, 12.5))
        val response = DistrictBoundaryResponseUiModel(
                geometry = DistrictBoundaryGeometryUiModel(listBoundaries)
        )

        every { districtBoundUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<GraphqlResponse>>().onNext(anyGql)
        }

        every { districtBoundMapper.map(anyGql)
        } returns response

        presenter.getDistrictBoundary(0, "asdn", 0)

        verifyOrder {
            view.showBoundaries(listBoundaries)
        }
    }

    @Test
    fun `get unnamed road` () {
        val address = SaveAddressDataModel(formattedAddress = "Unnamed Road, Jl Testimoni", selectedDistrict = "Testimoni")
        val result: SaveAddressDataModel?

        presenter.setAddress(address)
        result = presenter.getUnnamedRoadModelFormat()

        Assert.assertFalse(result.formattedAddress.contains("Unnamed Road"))
        Assert.assertEquals(result.formattedAddress, result.selectedDistrict)
    }

    @Test
    fun `detach`() {
        presenter.detachView()

        verifyOrder {
            getDistrictUseCase.unsubscribe()
            revGeoCodeUseCase.unsubscribe()
            districtBoundUseCase.unsubscribe()
        }
    }
}