package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.content.Context
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.common.AddressConstants
import com.tokopedia.logisticaddaddress.domain.mapper.AddAddressMapper
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictItem
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictZipcodes
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.KeroDistrictRecommendation
import com.tokopedia.logisticaddaddress.domain.usecase.AddAddressUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetZipCodeUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.add_address.AddAddressDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.add_address.AddAddressResponseUiModel
import com.tokopedia.logisticdata.data.entity.address.SaveAddressDataModel
import io.mockk.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.Subscriber

object AddEditAddressPresenterTest : Spek({

    val saveUseCase: AddAddressUseCase = mockk(relaxUnitFun = true)
    val zipUseCase: GetZipCodeUseCase = mockk(relaxUnitFun = true)
    val mapper: AddAddressMapper = mockk()
    val context: Context = mockk()
    val view: AddEditAddressListener = mockk(relaxed = true)

    mockkObject(AddNewAddressAnalytics)
    every { AddNewAddressAnalytics.eventClickButtonSimpanSuccess() } just Runs
    every { AddNewAddressAnalytics.eventClickButtonSimpanNegativeSuccess() } just Runs

    lateinit var presenter: AddEditAddressPresenter

    beforeEachTest {
        presenter = AddEditAddressPresenter(context, saveUseCase, zipUseCase, mapper)
        presenter.attachView(view)
    }

    Feature("save address") {
        Scenario("success from positive form") {
            val successGql = GraphqlResponse(mapOf(), mapOf(), false)
            val successAnswer = AddAddressResponseUiModel(
                    data = AddAddressDataUiModel(123, 1),
                    status = "OK"
            )
            val model = SaveAddressDataModel()
            Given("success answer") {
                every { saveUseCase.execute(any(), any()) } answers {
                    secondArg<Subscriber<GraphqlResponse>>().onNext(successGql)
                }
                every { mapper.map(successGql) } returns successAnswer
            }
            When("executed from positive form") {
                presenter.saveAddress(model, AddressConstants.ANA_POSITIVE)
            }
            Then("analytics simpan success is hit") {
                verify {
                    AddNewAddressAnalytics.eventClickButtonSimpanSuccess()
                }
            }
            Then("view show success") {
                verify { view.onSuccessAddAddress(model) }
            }
        }

        Scenario("error from negative form") {
            val successGql = GraphqlResponse(mapOf(), mapOf(), false)
            val failAnswer = AddAddressResponseUiModel(
                    data = AddAddressDataUiModel(0, 0),
                    status = "OK"
            )
            val model = SaveAddressDataModel()
            Given("not success answer") {
                every { saveUseCase.execute(any(), any()) } answers {
                    secondArg<Subscriber<GraphqlResponse>>().onNext(successGql)
                }
                every { mapper.map(successGql) } returns failAnswer
            }
            When("executed from negative form ") {
                presenter.saveAddress(model, AddressConstants.ANA_NEGATIVE)
            }
            Then("analytics simpan success is hit") {
                verify {
                    AddNewAddressAnalytics.eventClickButtonSimpanNegativeSuccess()
                }
            }
            Then("view show success") {
                verify { view.showError(any()) }
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

    Feature("detach") {
        Scenario("detached") {
            When("detached") {
                presenter.detachView()
            }
            Then("all usecases unsubscribed") {
                verifyOrder {
                    saveUseCase.unsubscribe()
                    zipUseCase.unsubscribe()
                }
            }
        }
    }
})