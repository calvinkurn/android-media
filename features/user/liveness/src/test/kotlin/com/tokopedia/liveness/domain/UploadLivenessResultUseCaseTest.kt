package com.tokopedia.liveness.domain

import com.google.gson.Gson
import com.tokopedia.liveness.data.model.MockUploadImageResponse
import com.tokopedia.liveness.data.model.response.LivenessResponse
import com.tokopedia.liveness.data.network.LivenessDetectionApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import javax.net.ssl.HttpsURLConnection
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UploadLivenessResultUseCaseTest: Spek ({
    val mockWebServer = MockWebServer()
    val uploadUrl = "/"
    val projectId = "1"
    val params = "/"
    var expectedValue = LivenessResponse()

    Feature("Upload image with mock response"){
        mockWebServer.start()

        val services = Retrofit.Builder()
                .baseUrl(mockWebServer.url(uploadUrl))
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .build()
                .create(LivenessDetectionApi::class.java)

        Scenario("succeed register with network service"){

            Given("upload data with success register mock response") {
                val response = MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_OK)
                        .setBody(MockUploadImageResponse.successRegister)
                mockWebServer.enqueue(response)
            }

            When("Call API with correct data and multipart file"){
                runBlocking {
                    val ktpImage = MultipartBody.Part.createFormData("ktp_image", "good ktp image")
                    val faceImage = MultipartBody.Part.createFormData("face_image", "good face image")
                    val tkpdProjectId = RequestBody.create(MediaType.parse("correct_projectId"), projectId)
                    val paramsText = RequestBody.create(MediaType.parse("correct_params"), params)
                    expectedValue = services.uploadImages(tkpdProjectId, paramsText, ktpImage, faceImage)
                }
            }

            Then("it should received true in success register"){
                assertTrue {expectedValue.data.isSuccessRegister}
            }
        }


        Scenario("failed register with network service") {
            Given("upload data with fail register mock response") {
                val response = MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_OK)
                        .setBody(MockUploadImageResponse.failedRegister)
                mockWebServer.enqueue(response)
            }

            When("Call API with bad data (blurry or fraud) and multipart file"){
                runBlocking {
                    val ktpImage = MultipartBody.Part.createFormData("test", "bad ktp image")
                    val faceImage = MultipartBody.Part.createFormData("test", "bad face image")
                    val tkpdProjectId = RequestBody.create(MediaType.parse("test"), projectId)
                    val paramsText = RequestBody.create(MediaType.parse("test"), params)
                    expectedValue = services.uploadImages(tkpdProjectId, paramsText, ktpImage, faceImage)
                }
            }

            Then("it should received false in success register"){
                assertFalse {expectedValue.data.isSuccessRegister}
            }
        }

        Scenario("bad params network service") {

            Given("upload data with bad params request") {
                val response = MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_OK)
                        .setBody(MockUploadImageResponse.badRequest)
                mockWebServer.enqueue(response)
            }

            When("") {
                runBlocking {
                    val ktpImage = MultipartBody.Part.createFormData("test", "ktp image")
                    val faceImage = MultipartBody.Part.createFormData("test", "face image")
                    val tkpdProjectId = RequestBody.create(MediaType.parse("test"), projectId)
                    val paramsText = RequestBody.create(MediaType.parse("bad params"), params)
                    expectedValue = services.uploadImages(tkpdProjectId, paramsText, ktpImage, faceImage)
                }
            }

            Then("the data should be null"){
                assertTrue{ expectedValue.data == null }
            }
        }

        Scenario("get error from Liveness API because file not found") {
            val exceptionMock = Exception("Oops!")
            val ktpImage = MultipartBody.Part.createFormData("", "")
            val faceImage = MultipartBody.Part.createFormData("", "")
            val tkpdProjectId = RequestBody.create(MediaType.parse("test"), projectId)
            val paramsText = RequestBody.create(MediaType.parse("bad params"), params)

            val errorServices = mockk<LivenessDetectionApi>()

            Given("upload data with bad params request") {
                val response = MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_OK)
                        .setBody(MockUploadImageResponse.badRequest)
                mockWebServer.enqueue(response)
            }

            Given("uploadImages throw something") {
                coEvery {
                    expectedValue = errorServices.uploadImages(tkpdProjectId, paramsText, ktpImage, faceImage)
                } throws exceptionMock
            }

            Then("It should throw something") {
                assertFailsWith<Exception> {
                    runBlocking {
                        errorServices.uploadImages(tkpdProjectId, paramsText, ktpImage, faceImage)
                    }
                }
            }
        }

        Scenario("timeout network service") {
            lateinit var response: MockResponse
            Given("upload data timeout") {
                response = MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_CLIENT_TIMEOUT)
                        .setBody("")
                mockWebServer.enqueue(response)
            }

            Then("throw timeout"){
                assertTrue { response.status.contains("408") }
            }
        }

        Scenario("image too large network service") {
            lateinit var response: MockResponse
            Given("upload data too large") {
                response = MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_ENTITY_TOO_LARGE)
                        .setBody("")
                mockWebServer.enqueue(response)
            }

            Then("throw entity too large"){
                assertTrue { response.status.contains("413") }
            }
        }

        afterGroup {
            mockWebServer.shutdown()
        }
    }

})