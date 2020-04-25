package com.tokopedia.dropoff.ui.autocomplete

import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticaddaddress.InstantTaskExecutorRuleSpek
import com.tokopedia.dropoff.data.response.AddressResponse
import com.tokopedia.logisticaddaddress.domain.mapper.AutoCompleteMapper
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.AutocompleteResponse
import com.tokopedia.logisticaddaddress.domain.model.get_district.GetDistrictResponse
import com.tokopedia.dropoff.ui.autocomplete.model.SavedAddress
import com.tokopedia.dropoff.ui.autocomplete.model.SuggestedPlace
import com.tokopedia.dropoff.ui.autocomplete.model.ValidatedDistrict
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object AutoCompleteViewModelTest : Spek({

    InstantTaskExecutorRuleSpek(this)

    val dispatcher: CoroutineDispatcher = mockk()
    val autoCompleteUseCase: GraphqlUseCase<AutocompleteResponse> = mockk(relaxUnitFun = true)
    val getDistrictUseCase: GraphqlUseCase<GetDistrictResponse> = mockk(relaxUnitFun = true)
    val getSavedAddressUseCase: GraphqlUseCase<com.tokopedia.dropoff.data.response.AddressResponse> = mockk(relaxUnitFun = true)
    val mapper: AutoCompleteMapper = mockk()
    lateinit var viewModel: com.tokopedia.dropoff.ui.autocomplete.AutoCompleteViewModel

    beforeEachTest {
        viewModel = com.tokopedia.dropoff.ui.autocomplete.AutoCompleteViewModel(dispatcher, autoCompleteUseCase,
                getDistrictUseCase, getSavedAddressUseCase, mapper)
    }

    Feature("get auto complete") {
        val autoCompleteObserver: Observer<Result<List<com.tokopedia.dropoff.ui.autocomplete.model.SuggestedPlace>>> = mockk(relaxed = true)
        Scenario("success") {
            val successResponse = AutocompleteResponse()
            val successList = listOf(
                    com.tokopedia.dropoff.ui.autocomplete.model.SuggestedPlace("Jakarta Pusat")
            )
            Given("success callback") {
                every { autoCompleteUseCase.execute(any(), any()) } answers {
                    firstArg<(AutocompleteResponse) -> Unit>().invoke(successResponse)
                }
                every { mapper.mapAutoComplete(successResponse) } returns successList
            }
            When("executed") {
                viewModel.autoCompleteList.observeForever(autoCompleteObserver)
                viewModel.getAutoCompleteList("")
            }
            Then("live data is changed to success") {
                autoCompleteObserver.onChanged(Success(successList))
            }
        }

        Scenario("fail") {
            val testError = Throwable("test error")
            Given("error callback") {
                every { autoCompleteUseCase.execute(any(), any()) } answers {
                    secondArg<(Throwable) -> Unit>().invoke(testError)
                }
            }
            When("executed") {
                viewModel.autoCompleteList.observeForever(autoCompleteObserver)
                viewModel.getAutoCompleteList("")
            }
            Given("live data is changed to error") {
                autoCompleteObserver.onChanged(Fail(testError))
            }
        }
    }

    Feature("get latitude longitude") {
        val validateObserver: Observer<Result<com.tokopedia.dropoff.ui.autocomplete.model.ValidatedDistrict>> = mockk(relaxed = true)
        Scenario("success") {
            val successResponse = GetDistrictResponse()
            val successDistrict = com.tokopedia.dropoff.ui.autocomplete.model.ValidatedDistrict()
            Given("success callback") {
                every { getDistrictUseCase.execute(any(), any()) } answers {
                    firstArg<(GetDistrictResponse) -> Unit>().invoke(successResponse)
                }
                every { mapper.mapValidate(successResponse) } returns successDistrict
            }
            When("executed") {
                viewModel.validatedDistrict.observeForever(validateObserver)
                viewModel.getLatLng("")
            }
            Then("liva data is changed to success") {
                validateObserver.onChanged(Success(successDistrict))
            }
        }

        Scenario("fail") {
            val testError = Throwable("test error")
            Given("error callback") {
                every { getDistrictUseCase.execute(any(), any()) } answers {
                    secondArg<(Throwable) -> Unit>().invoke(testError)
                }
            }
            When("executed") {
                viewModel.validatedDistrict.observeForever(validateObserver)
                viewModel.getLatLng("")
            }
            Given("live data is changed to error") {
                validateObserver.onChanged(Fail(testError))
            }
        }
    }

    Feature("get saved address") {
        val savedObserver: Observer<Result<List<com.tokopedia.dropoff.ui.autocomplete.model.SavedAddress>>> = mockk(relaxed = true)
        Scenario("success") {
            val successResponse = com.tokopedia.dropoff.data.response.AddressResponse()
            val successDistrict = listOf(
                    com.tokopedia.dropoff.ui.autocomplete.model.SavedAddress(addrId = 99)
            )
            Given("success callback") {
                every { getSavedAddressUseCase.execute(any(), any()) } answers {
                    firstArg<(com.tokopedia.dropoff.data.response.AddressResponse) -> Unit>().invoke(successResponse)
                }
                every { mapper.mapAddress(successResponse) } returns successDistrict
            }
            When("executed") {
                viewModel.savedAddress.observeForever(savedObserver)
                viewModel.getSavedAddress()
            }
            Then("liva data is changed to success") {
                savedObserver.onChanged(Success(successDistrict))
            }
        }

        Scenario("fail") {
            val testError = Throwable("test error")
            Given("error callback") {
                every { getDistrictUseCase.execute(any(), any()) } answers {
                    secondArg<(Throwable) -> Unit>().invoke(testError)
                }
            }
            When("executed") {
                viewModel.savedAddress.observeForever(savedObserver)
                viewModel.getSavedAddress()
            }
            Given("live data is changed to error") {
                savedObserver.onChanged(Fail(testError))
            }
        }
    }

})