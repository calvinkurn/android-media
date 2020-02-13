package com.tokopedia.logisticaddaddress.features.dropoff_picker

import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticaddaddress.InstantTaskExecutorRuleSpek
import com.tokopedia.logisticaddaddress.data.entity.response.GetStoreResponse
import com.tokopedia.logisticaddaddress.domain.mapper.GetStoreMapper
import com.tokopedia.logisticaddaddress.features.dropoff_picker.model.DropoffUiModel
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

    val mapper: GetStoreMapper = mockk()
    val usecase: GraphqlUseCase<GetStoreResponse> = mockk(relaxed = true)
    val dispatcher: CoroutineDispatcher = mockk()
    lateinit var viewModel: DropoffPickerViewModel

    beforeEachTest {
        viewModel = DropoffPickerViewModel(dispatcher, usecase, mapper)
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
            val observer = mockk<Observer<Result<DropoffUiModel>>>(relaxed = true)
            Given("gives success value") {
                every { usecase.execute(any(), any()) } answers {
                    firstArg<(GetStoreResponse) -> Unit>().invoke(GetStoreResponse())
                }
                every { mapper.map(GetStoreResponse()) } returns DropoffUiModel(listOf(), 0)
            }
            When("executed") {
                viewModel.storeData.observeForever(observer)
                viewModel.getStores("")
            }
            Then("data updated with success value") {
                verify {
                    observer.onChanged(Success(DropoffUiModel(listOf(), 0)))
                }
            }
        }
    }


})