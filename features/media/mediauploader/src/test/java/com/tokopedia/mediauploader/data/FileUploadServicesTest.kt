package com.tokopedia.mediauploader.data

import com.google.gson.Gson
import com.tokopedia.mediauploader.MockUploaderResponse
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import kotlin.test.assertTrue

class FileUploadServicesTest {

    private val mockWebServer = MockWebServer()
    private val url = "/"

    private val services = Retrofit.Builder()
        .baseUrl(mockWebServer.url(url))
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()
        .create(FileUploadServices::class.java)

    @Before fun setUp() {
        try {
            mockWebServer.start()
        } catch (e: Exception) {}
    }

    @Test fun `It should success upload image and received uploadId correctly via web server`() {
        runBlocking {
            // Given
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockUploaderResponse.success)
            mockWebServer.enqueue(response)

            // When
            val multipart = MultipartBody.Part.createFormData("test", "test")
            val result = services.uploadFile(url, multipart, "")

            // Then
            assertTrue { result.data?.uploadId?.isNotEmpty() == true }
        }
    }

    @Test fun `It should not success upload image because time out`() {
        runBlocking {
            // Given
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockUploaderResponse.error)
            mockWebServer.enqueue(response)

            // When
            val multipart = MultipartBody.Part.createFormData("test", "test")
            val result = services.uploadFile(url, multipart, "")

            mockWebServer.takeRequest(1, TimeUnit.SECONDS)

            // Then
            assertTrue { result.data == null }
        }
    }

    @After fun tearDown() {
        mockWebServer.shutdown()
    }

}