package com.tokopedia.logisticaddaddress.features.dropoff_picker

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticaddaddress.data.entity.response.GetStoreResponse
import com.tokopedia.logisticaddaddress.domain.mapper.GetStoreMapper
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.CoroutineDispatcher
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object DropoffPickerViewModelTest : Spek({

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

    }


})