package com.tokopedia.mediauploader.domain

import com.tokopedia.mediauploader.MediaRepository
import com.tokopedia.mediauploader.data.entity.DataUploaderPolicy
import com.tokopedia.mediauploader.data.entity.SourcePolicy
import com.tokopedia.mediauploader.data.entity.UploaderPolicy
import com.tokopedia.mediauploader.stubDataPolicyRepository
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DataPolicyUseCaseTest: Spek({
    Feature("data policy use case") {
        val repository = mockk<MediaRepository>(relaxed = true)
        val useCase = DataPolicyUseCase(repository)
        val sourceId = "WXjxja"
        val dataUploaderPolicy = DataUploaderPolicy()

        Scenario("create param with source id") {
            var requestParams = mapOf<String, Any>()

            When("create param") {
                requestParams = DataPolicyUseCase.createParams(sourceId)
            }
            Then("it should return source id correctly") {
                assert(requestParams["source"] == sourceId)
            }
        }

        Scenario("request data policy without source id") {
            Given("graphql repository") {
                repository.stubDataPolicyRepository(onError = mapOf())
            }
            Then("it should return exception of param not found") {
                runBlocking {
                    assertFailsWith<Exception> {
                        useCase(mapOf())
                    }
                }
            }
        }

        Scenario("request data policy with source id") {
            var requestParams = mapOf<String, Any>()

            Given("request param") {
                requestParams = DataPolicyUseCase.createParams(sourceId)
            }
            Given("graphql repository") {
                repository.stubDataPolicyRepository(onError = mapOf())
            }
            Then("it should return policy of uploader") {
                runBlocking {
                    val result = useCase(requestParams)
                    assertEquals(dataUploaderPolicy, result)
                }
            }
        }

        Scenario("request data policy with null error") {
            var requestParams = mapOf<String, Any>()
            Given("create param") {
                requestParams = DataPolicyUseCase.createParams(sourceId)
            }
            Given("graphql repository") {
                repository.stubDataPolicyRepository(onError = null)
            }
            Then("it should return exception of network error") {
                runBlocking {
                    assertFailsWith<NullPointerException> {
                        useCase(requestParams)
                    }
                }
            }
        }
    }
})