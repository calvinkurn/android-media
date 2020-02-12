package com.tokopedia.mediauploader.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.mediauploader.data.consts.MediaUploaderQuery
import com.tokopedia.mediauploader.data.entity.DataUploaderPolicy
import com.tokopedia.mediauploader.data.entity.SourcePolicy
import com.tokopedia.mediauploader.data.entity.UploaderPolicy
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.MockKStubScope
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class DataPolicyUseCaseTest: Spek({

    Feature("data policy use case") {

        val repository = mockk<GraphqlRepository>(relaxed = true)
        val useCase = DataPolicyUseCase(repository)
        val sourceId = "WXjxja"
        val dataUploaderPolicy = DataUploaderPolicy(
                UploaderPolicy(SourcePolicy(sourceType = "test"))
        )

        Scenario("create param with source id") {
            var requestParams = mapOf<String, Any>()

            When("create param") {
                requestParams = DataPolicyUseCase.createParams(sourceId)
            }
            Then("it should return source id correctly") {
                assert(requestParams["source"] == sourceId)
            }
        }

        Scenario("request data policy with correct source id") {
            Given("request param") {
                useCase.requestParams = DataPolicyUseCase.createParams("id")
            }
            Given("graphql repository") {
                coEvery {
                    repository.getReseponse(listOf())
                } answers {
                    GraphqlResponse(
                            mapOf(DataUploaderPolicy::class.java to DataUploaderPolicy()),
                            null,
                            false
                    )
                }
            }
            Then("it should return policy of uploader") {
                runBlocking {
                    val result = useCase.executeOnBackground()
                    assert(dataUploaderPolicy == result)
                }
            }
        }
    }
})