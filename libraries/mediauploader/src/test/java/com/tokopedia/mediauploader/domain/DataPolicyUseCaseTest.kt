package com.tokopedia.mediauploader.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
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
import org.junit.Assert.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class DataPolicyUseCaseTest: Spek({

    Feature("data policy use case") {

        val repository = mockk<GraphqlRepository>(relaxed = true)
        val useCase = DataPolicyUseCase(MediaUploaderQuery.dataPolicyQuery, repository)
        val dataUploaderPolicy = DataUploaderPolicy(
                UploaderPolicy(SourcePolicy(sourceType = "test"))
        )

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
            var returnValue: DataUploaderPolicy?= null

            Given("request param") {
                useCase.requestParams = DataPolicyUseCase.createParamDataPolicy("id")

                coEvery {
                    repository.getReseponse(listOf()).getData<DataUploaderPolicy>(DataUploaderPolicy::class.java)
                } answers {
                    GraphqlResponse(
                            mapOf(DataUploaderPolicy::class.java to DataUploaderPolicy()),
                            null,
                            false
                    ).getData<DataUploaderPolicy>(DataUploaderPolicy::class.java)
                }

                coEvery {
                    repository.getReseponse(listOf()).getError(
                            DataUploaderPolicy::class.java
                    )
                } answers {
                    listOf()
                }
            }
            When("get policy uploader") {
                runBlocking { useCase.executeOnBackground() }
            }
            Then("it should return policy of uploader") {
                useCase.execute({}, {})
            }
        }

//        Scenario("request data policy with param empty") {
//            When("get policy uploader") {
//                graphqlUseCase.stubExecuteOnBackground().returns(dataUploaderPolicy)
//            }
//            Then("it should return throw exception of missing param") {
//                useCase.get({}, {
//                    assertEquals(it, Throwable(""))
//                })
//            }
//        }
    }

})

fun UseCase<*>.stubExecuteOnBackground(): MockKStubScope<Any, Any> {
    val request = this
    return coEvery { request.executeOnBackground() }
}