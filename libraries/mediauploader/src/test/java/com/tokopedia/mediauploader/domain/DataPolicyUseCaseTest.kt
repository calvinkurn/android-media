package com.tokopedia.mediauploader.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.mediauploader.data.consts.MediaUploaderQuery
import com.tokopedia.mediauploader.data.entity.DataUploaderPolicy
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.MockKStubScope
import io.mockk.coEvery
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class DataPolicyUseCaseTest: Spek({

    Feature("data policy use case") {

        val cacheStrategy = mockk<GraphqlCacheStrategy>(relaxed = true)
        val repository = mockk<GraphqlRepository>(relaxed = true)
        val useCase = DataPolicyUseCase(MediaUploaderQuery.dataPolicyQuery, cacheStrategy, repository)

        Scenario("create param with source id") {
            var requestParams = mapOf<String, Any>()
            var sourceId = ""

            Given("source id") {
                sourceId = "HUgbQS"
            }

            When("create param") {
                requestParams = DataPolicyUseCase.createParamDataPolicy(sourceId)
            }

            Then("it should return source id correctly") {
                assert(requestParams["source"] == sourceId)
            }
        }

        Scenario("request data policy with correct source id") {
            val dataUploaderPolicy = DataUploaderPolicy()

            Given("request param") {
                useCase.requestParams = DataPolicyUseCase.createParamDataPolicy("id")
            }

            When("get data") {
                useCase.stubExecuteOnBackground().returns(dataUploaderPolicy)
            }

            Then("should return policy of uploader") {
                coEvery {
                    assert(useCase.executeOnBackground() == dataUploaderPolicy)
                }
            }
        }
    }

})

fun UseCase<*>.stubExecuteOnBackground(): MockKStubScope<Any, Any> {
    val request = this
    return coEvery { request.executeOnBackground() }
}