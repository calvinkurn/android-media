package com.tokopedia.dropoff.ui.dropoff_picker

import android.accounts.NetworkErrorException
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticaddaddress.InstantTaskExecutorRuleSpek
import com.tokopedia.dropoff.data.response.GetStoreResponse
import com.tokopedia.dropoff.domain.GetStoreMapper
import com.tokopedia.dropoff.ui.dropoff_picker.model.DropoffUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.CoroutineDispatcher
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object DropoffPickerViewModelTest : Spek({

    InstantTaskExecutorRuleSpek(this)

    val mapper: com.tokopedia.dropoff.domain.GetStoreMapper = mockk()
    val usecase: GraphqlUseCase<com.tokopedia.dropoff.data.response.GetStoreResponse> = mockk(relaxed = true)
    val dispatcher: CoroutineDispatcher = mockk()
    val storeObserver = mockk<Observer<Result<com.tokopedia.dropoff.ui.dropoff_picker.model.DropoffUiModel>>>(relaxed = true)
    lateinit var viewModel: com.tokopedia.dropoff.ui.dropoff_picker.DropoffPickerViewModel

    beforeEachTest {
        viewModel = com.tokopedia.dropoff.ui.dropoff_picker.DropoffPickerViewModel(dispatcher, usecase, mapper)
    }

    Feature("getting store") {

        Scenario("graphql usecase call flow") {
            When("executed") {
                viewModel.getStores("")
            }
            Then("usecase executed as sequence") {
                verifySequence {
                    usecase.setTypeClass(any())
                    usecase.setRequestParams(any())
                    usecase.setGraphqlQuery(any())
                    usecase.execute(any(), any())
                }
            }
        }

        Scenario("success") {
            val testData = com.tokopedia.dropoff.ui.dropoff_picker.model.DropoffUiModel(listOf(), 1)

            Given("gives success value") {
                every { usecase.execute(any(), any()) } answers {
                    firstArg<(com.tokopedia.dropoff.data.response.GetStoreResponse) -> Unit>().invoke(com.tokopedia.dropoff.data.response.GetStoreResponse())
                }
                every { mapper.map(com.tokopedia.dropoff.data.response.GetStoreResponse()) } returns testData
            }
            When("executed") {
                viewModel.storeData.observeForever(storeObserver)
                viewModel.getStores("")
            }
            Then("data updated with success value") {
                verify {
                    storeObserver.onChanged(Success(testData))
                }
            }
        }

        Scenario("fail") {
            val testError = NetworkErrorException("test error")
            Given("gives error") {
                every { usecase.execute(any(), any()) } answers {
                    secondArg<(Throwable) -> Unit>().invoke(testError)
                }
            }
            When("executed") {
                viewModel.storeData.observeForever(storeObserver)
                viewModel.getStores("")
            }
            Then("data updated with error value") {
                verify {
                    storeObserver.onChanged(Fail(testError))
                }
            }
        }
    }


})