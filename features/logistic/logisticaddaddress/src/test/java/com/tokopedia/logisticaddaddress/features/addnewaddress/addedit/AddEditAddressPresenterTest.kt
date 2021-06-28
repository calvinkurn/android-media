package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.logisticCommon.domain.model.SuggestedPlace
import com.tokopedia.logisticaddaddress.common.AddressConstants
import com.tokopedia.logisticaddaddress.domain.model.add_address.AddAddressResponse
import com.tokopedia.logisticaddaddress.domain.model.add_address.Data
import com.tokopedia.logisticaddaddress.domain.model.add_address.KeroAddAddress
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictItem
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictZipcodes
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.KeroDistrictRecommendation
import com.tokopedia.logisticaddaddress.domain.usecase.AddAddressUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.AutoCompleteUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetZipCodeUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.network.exception.MessageErrorException
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import rx.Observable

class AddEditAddressPresenterTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val saveUseCase: AddAddressUseCase = mockk(relaxUnitFun = true)
    val zipUseCase: GetZipCodeUseCase = mockk(relaxUnitFun = true)
    val districtUseCase: GetDistrictUseCase = mockk(relaxUnitFun = true)
    val autoCompleteUseCase: AutoCompleteUseCase = mockk(relaxUnitFun = true)
    val view: AddEditView = mockk(relaxed = true)

    lateinit var presenter: AddEditAddressPresenter

    @Before
    fun setup() {
        presenter = AddEditAddressPresenter(saveUseCase, zipUseCase, districtUseCase, autoCompleteUseCase)

        mockkObject(AddNewAddressAnalytics)
        every { AddNewAddressAnalytics.eventClickButtonSimpanSuccess(any(), any()) } just Runs
        every { AddNewAddressAnalytics.eventClickButtonSimpanNegativeSuccess(any(), any()) } just Runs
        every { AddNewAddressAnalytics.eventClickButtonSimpanNotSuccess(any(), any(), any()) } just Runs

        presenter.attachView(view)
    }

    @Test
    fun `save address succcess from positive form`() {
        val model = SaveAddressDataModel()
        val successGql = AddAddressResponse(KeroAddAddress(
                Data(isSuccess = 1, addrId = 99)
        ))

        every { saveUseCase.execute(any(), "1")
        } returns Observable.just(successGql)

        presenter.saveAddress(model, AddressConstants.ANA_POSITIVE, true, true)

        verifyOrder {
            AddNewAddressAnalytics.eventClickButtonSimpanSuccess(any(), any())
            assertEquals(successGql.keroAddAddress.data.addrId, model.id)
            view.onSuccessAddAddress(model)
        }
    }

    @Test
    fun `save address not succcess from negative form`() {
        val model = SaveAddressDataModel()
        val notSuccessResponse = AddAddressResponse(KeroAddAddress(
                Data(isSuccess = 0)
        ))

        every { saveUseCase.execute(any(), any())
        } returns Observable.just(notSuccessResponse)

        presenter.saveAddress(model, AddressConstants.ANA_NEGATIVE, true, true)

        verifyOrder {
            AddNewAddressAnalytics.eventClickButtonSimpanNegativeSuccess(any(), any())
            view.showError(null)
        }
    }

    @Test
    fun `save address error gql response`() {
        val model = SaveAddressDataModel()
        val exception = MessageErrorException("hi")

        every { saveUseCase.execute(any(), "1")
        } returns Observable.error(exception)

        presenter.saveAddress(model, AddressConstants.ANA_POSITIVE, true, true)

        verifyOrder {
            AddNewAddressAnalytics.eventClickButtonSimpanNotSuccess(any(), any(), any())
            view.showError(exception)
        }
    }

    @Test
    fun `get zipCode success with zipcodes`() {
        val success = DistrictZipcodes(
                keroDistrictDetails = KeroDistrictRecommendation(
                        district = listOf(DistrictItem(zipCode = listOf("10910")))
                )
        )

        every { zipUseCase.execute(any())
        } returns Observable.just(success)

        presenter.getZipCodes("")

        verifyOrder {
            view.showZipCodes(success.keroDistrictDetails.district[0].zipCode)
        }
    }

    @Test
    fun `get zipCode success with no zipcodes`() {
        val success = DistrictZipcodes(
                keroDistrictDetails = KeroDistrictRecommendation(
                        district = listOf(DistrictItem(zipCode = listOf()))
                )
        )

        every { zipUseCase.execute(any())
        } returns Observable.just(success)

        presenter.getZipCodes("")

        verifyOrder {
            view.showManualZipCodes()
        }
    }

    @Test
    fun `get autocomplete`() {
        val givenPlaceId = "1"
        val givenLat = 0.3131
        val givenLong = 9.3232
        val successModel = Place(listOf(SuggestedPlace(givenPlaceId)))
        val successDistrict = GetDistrictDataUiModel(
                latitude = givenLat.toString(), longitude = givenLong.toString())

        every { autoCompleteUseCase.execute(any()) } returns Observable.just(successModel)
        every { districtUseCase.execute(any()) } returns Observable.just(successDistrict)

        presenter.getAutoComplete("")

        verifyOrder {
            view.moveMap(givenLat, givenLong)
        }
    }

    @Test
    fun `detact`() {
        presenter.detachView()

        verifyOrder {
            saveUseCase.unsubscribe()
            zipUseCase.unsubscribe()
            autoCompleteUseCase.unsubscribe()
            districtUseCase.unsubscribe()
        }
    }

}
