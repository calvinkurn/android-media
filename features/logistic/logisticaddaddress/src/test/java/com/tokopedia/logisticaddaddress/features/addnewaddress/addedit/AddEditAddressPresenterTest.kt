package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

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
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.logisticCommon.domain.model.SuggestedPlace
import com.tokopedia.network.exception.MessageErrorException
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable

object AddEditAddressPresenterTest : Spek({

    val saveUseCase: AddAddressUseCase = mockk(relaxUnitFun = true)
    val zipUseCase: GetZipCodeUseCase = mockk(relaxUnitFun = true)
    val districtUseCase: GetDistrictUseCase = mockk(relaxUnitFun = true)
    val autoCompleteUseCase: AutoCompleteUseCase = mockk(relaxUnitFun = true)
    val view: AddEditView = mockk(relaxed = true)

    mockkObject(AddNewAddressAnalytics)
    every { AddNewAddressAnalytics.eventClickButtonSimpanSuccess(any(), any()) } just Runs
    every { AddNewAddressAnalytics.eventClickButtonSimpanNegativeSuccess(any(), any()) } just Runs
    every { AddNewAddressAnalytics.eventClickButtonSimpanNotSuccess(any(), any(), any()) } just Runs

    lateinit var presenter: AddEditAddressPresenter

    beforeEachTest {
        presenter = AddEditAddressPresenter(saveUseCase, zipUseCase,
                districtUseCase, autoCompleteUseCase)
        presenter.attachView(view)
    }

    Feature("save address") {
        val model = SaveAddressDataModel()
        Scenario("success from positive form") {
            val successGql = AddAddressResponse(KeroAddAddress(
                    Data(isSuccess = 1, addrId = 99)
            ))
            Given("success answer") {
                every { saveUseCase.execute(any(), "1") } returns Observable.just(successGql)
            }
            When("executed from positive form") {
                presenter.saveAddress(model, AddressConstants.ANA_POSITIVE, isFullFlow = true, isLogisticLabel = true)
            }
            Then("analytics simpan success is hit") {
                verify {
                    AddNewAddressAnalytics.eventClickButtonSimpanSuccess(any(), any())
                }
            }
            Then("view show success") {
                assertEquals(successGql.keroAddAddress.data.addrId, model.id)
                verify { view.onSuccessAddAddress(model) }
            }
        }

        Scenario("not success response from negative form") {
            val notSuccessResponse = AddAddressResponse(KeroAddAddress(
                    Data(isSuccess = 0)
            ))
            Given("not success answer") {
                every { saveUseCase.execute(any(), any()) } returns Observable.just(notSuccessResponse)
            }
            When("executed from negative form ") {
                presenter.saveAddress(model, AddressConstants.ANA_NEGATIVE, isFullFlow = true, isLogisticLabel = true)
            }
            Then("analytics simpan success is hit") {
                verify {
                    AddNewAddressAnalytics.eventClickButtonSimpanNegativeSuccess(any(), any())
                }
            }
            Then("view show success") {
                verify {
                    view.showError(null)
                }
            }
        }

        Scenario("error gql response") {
            val exception = MessageErrorException("hi")
            Given("error answer") {
                every { saveUseCase.execute(any(), any()) } returns Observable.error(exception)
            }
            When("executed") {
                presenter.saveAddress(model, AddressConstants.ANA_POSITIVE, isFullFlow = true, isLogisticLabel = true)
            }
            Then("view shows error") {
                verify {
                    view.showError(exception)
                }
            }
        }
    }

    Feature("get zip code") {
        Scenario("success with zipcodes") {
            val success = DistrictZipcodes(
                    keroDistrictDetails = KeroDistrictRecommendation(
                            district = listOf(DistrictItem(zipCode = listOf("10910")))
                    )
            )
            Given("success with list of zipcodes") {
                every { zipUseCase.execute(any()) } returns Observable.just(success)
            }
            When("executed") {
                presenter.getZipCodes("")
            }
            Then("view showing zipcodes") {
                verify {
                    view.showZipCodes(success.keroDistrictDetails.district[0].zipCode)
                }
            }
        }
        Scenario("success with no zipcode") {
            val success = DistrictZipcodes(
                    keroDistrictDetails = KeroDistrictRecommendation(
                            district = listOf(DistrictItem(zipCode = listOf()))
                    )
            )
            Given("success with no zipcode") {
                every { zipUseCase.execute(any()) } returns Observable.just(success)
            }
            When("executed") {
                presenter.getZipCodes("")
            }
            Then("view shows manual zipcode") {
                verify { view.showManualZipCodes() }
            }
        }
    }

    Feature("get autocomplete") {
        Scenario("positive") {
            val givenPlaceId = "1"
            val givenLat = 0.3131
            val givenLong = 9.3232
            val successModel = Place(listOf(SuggestedPlace(givenPlaceId)))
            val successDistrict = GetDistrictDataUiModel(
                    latitude = givenLat.toString(), longitude = givenLong.toString())
            Given("returns positive") {
                every { autoCompleteUseCase.execute(any()) } returns Observable.just(successModel)
                every { districtUseCase.execute(any()) } returns Observable.just(successDistrict)
            }
            When("executed") {
                presenter.getAutoComplete("")
            }
            Then("view moves map") {
                verify {
                    view.moveMap(givenLat, givenLong)
                }
            }
        }
    }

    Feature("detach") {
        Scenario("detached") {
            When("detached") {
                presenter.detachView()
            }
            Then("all use cases unsubscribed") {
                verify {
                    saveUseCase.unsubscribe()
                    zipUseCase.unsubscribe()
                    autoCompleteUseCase.unsubscribe()
                    districtUseCase.unsubscribe()
                }
            }
        }
    }
})