package com.tokopedia.mediauploader.domain

import com.tokopedia.mediauploader.data.UploaderServices
import com.tokopedia.mediauploader.data.entity.MediaUploader
import com.tokopedia.mediauploader.data.entity.UploadData
import com.tokopedia.usecase.RequestParams
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertFailsWith

class MediaUploaderUseCaseTest: Spek({
    Feature("data policy use case") {
        val services = mockk<UploaderServices>()
        val useCase = MediaUploaderUseCase(services)

        val uploadUrl = "http://google.com/"
        val filePath = "/sdcard/DCIM/Camera/test.jpg"
        val uploadId = "abc-456-xyz-123"
        val expectedValue = MediaUploader(
                data = UploadData(uploadId)
        )

        Scenario("create param with upload url and file path") {
            var requestParams = RequestParams()

            When("create param") {
                requestParams = useCase.createParams(uploadUrl, filePath)
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

        Scenario("request upload image") {
            var requestParams = RequestParams()

            Given("set param with upload url and file path") {
                requestParams = useCase.createParams(uploadUrl, filePath)
            }
            Given("upload network services") {
                coEvery {
                    services.uploadFile(uploadUrl, any())
                } answers {
                    expectedValue
                }
            }
            Then("it should received upload_id") {
                runBlocking {
                    val data = useCase(requestParams)
                    assert(data.data.uploadId == uploadId)
                }
            }
        }
    }
})