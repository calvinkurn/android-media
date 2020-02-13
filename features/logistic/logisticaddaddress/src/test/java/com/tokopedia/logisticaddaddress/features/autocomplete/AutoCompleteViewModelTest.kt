package com.tokopedia.logisticaddaddress.features.autocomplete

import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticaddaddress.InstantTaskExecutorRuleSpek
import com.tokopedia.logisticaddaddress.data.entity.response.AddressResponse
import com.tokopedia.logisticaddaddress.domain.mapper.AutoCompleteMapper
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.AutocompleteResponse
import com.tokopedia.logisticaddaddress.domain.model.get_district.GetDistrictResponse
import com.tokopedia.logisticaddaddress.domain.usecase.queryTest
import com.tokopedia.logisticaddaddress.features.autocomplete.model.SuggestedPlace
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
    val getSavedAddressUseCase: GraphqlUseCase<AddressResponse> = mockk(relaxUnitFun = true)
    val mapper: AutoCompleteMapper = mockk()
    lateinit var viewModel: AutoCompleteViewModel

    beforeEachTest {
        viewModel = AutoCompleteViewModel(dispatcher, autoCompleteUseCase,
                getDistrictUseCase, getSavedAddressUseCase, mapper)
    }

    Feature("get auto complete") {
        val autoCompleteObserver: Observer<Result<List<SuggestedPlace>>> = mockk(relaxed = true)
        Scenario("success") {
            val successResponse = AutocompleteResponse()
            val successList = listOf(
                    SuggestedPlace("Jakarta Pusat")
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
            Then("live data changed to success") {
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
            Given("live data changed to error") {
                autoCompleteObserver.onChanged(Fail(testError))
            }
        }
    }

})