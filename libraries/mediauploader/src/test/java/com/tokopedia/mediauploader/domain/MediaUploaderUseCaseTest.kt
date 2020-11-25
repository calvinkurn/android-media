package com.tokopedia.mediauploader.domain

import com.google.gson.Gson
import com.tokopedia.mediauploader.MockUploaderResponse
import com.tokopedia.mediauploader.data.UploaderServices
import com.tokopedia.mediauploader.data.entity.MediaUploader
import com.tokopedia.mediauploader.data.state.ProgressCallback
import com.tokopedia.usecase.RequestParams
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import kotlin.test.assertFailsWith

class MediaUploaderUseCaseTest: Spek({

    val mockWebServer = MockWebServer()

    val uploadUrl = "/"
    val filePath = "image.jpg"
    var expectedValue = MediaUploader()

    Feature("data policy use case") {

        val services = mockk<UploaderServices>()
        val useCase = MediaUploaderUseCase(services)

        useCase.progressCallback = object : ProgressCallback {
            override fun onProgress(percentage: Int) {}
        }

        Scenario("create param with upload url and file path") {
            var requestParams = RequestParams()

            When("create param") {
                requestParams = MediaUploaderUseCase.createParams(uploadUrl, filePath, "60")
            }
            Then("it should return upload url correctly") {
                assert(requestParams.getString("url", "") == uploadUrl)
            }
        }

        Scenario("request upload image without param") {
            val emptyParams = RequestParams()
            Then("it should got exception no param found") {
                runBlocking {
                    assertFailsWith<Exception> {
                        useCase(emptyParams)
                    }
                }
            }
        }

        Scenario("request upload image from usecase") {
            var requestParams = RequestParams()

            Given("set param with upload url and file path") {
                requestParams = MediaUploaderUseCase.createParams(uploadUrl, filePath, "60")
            }

            Given("upload network services") {
                coEvery {
                    services.uploadFile(uploadUrl, any(), "60")
                } answers {
                    expectedValue
                }
            }

            Then("it should received upload_id") {
                runBlocking {
                    val data = useCase(requestParams)
                    assert(data.data?.uploadId == expectedValue.data?.uploadId)
                }
            }
        }
    }

    Feature("data policy with mock response") {

        mockWebServer.start()

        //retrofit builder
        val services = Retrofit.Builder()
                .baseUrl(mockWebServer.url(uploadUrl))
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .build()
                .create(UploaderServices::class.java)

        Scenario("request upload image with network service") {
            Given("upload id data response") {
                val response = MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_OK)
                        .setBody(MockUploaderResponse.success)
                mockWebServer.enqueue(response)
            }

            Given("network services") {
                runBlocking {
                    val multipart = MultipartBody.Part.createFormData("test", "test")
                    expectedValue = services.uploadFile(uploadUrl, multipart, "60")
                }
            }

            Then("it should received upload_id") {
                assert(expectedValue.data?.uploadId?.isNotEmpty()?: false)
            }
        }

        Scenario("request upload id with invalid resource dimen") {
            Given("upload id data response") {
                val response = MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_OK)
                        .setBody(MockUploaderResponse.error)
                mockWebServer.enqueue(response)
            }

            Given("network services") {
                runBlocking {
                    val multipart = MultipartBody.Part.createFormData("test", "test")
                    expectedValue = services.uploadFile(uploadUrl, multipart, "60")
                }
            }

            Then("it should received null data") {
                mockWebServer.takeRequest(1, TimeUnit.SECONDS)
                assert(expectedValue.data == null)
            }
        }

        afterGroup {
            mockWebServer.shutdown()
        }
    }
})