package com.tokopedia.mediauploader.domain

import com.tokopedia.mediauploader.data.entity.*
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.usecase.RequestParams
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.io.File

class UploaderUseCaseTest: Spek({

    val dataPolicyUseCase = mockk<DataPolicyUseCase>(relaxed = true)
    val mediaUploaderUseCase = mockk<MediaUploaderUseCase>()
    val useCase = UploaderUseCase(dataPolicyUseCase, mediaUploaderUseCase)

    val file = mockk<File>()

    val sourceId = "WXjxja"
    val filePath = "image.jpg"
    val filePathInvalidExtension = "image.jpt"
    val uploadId = "abc-123-xyz-456"

    Feature("uploader use case") {
        Scenario("request data policy with source id") {
            var requestParams = RequestParams()

            Given("request param") {
                every { file.exists() } answers { true }
                every { file.path } answers { filePath }

                requestParams = useCase.createParams(sourceId, file)
            }
            Given("data policy use case") {
                coEvery {
                    dataPolicyUseCase(any())
                } answers {
                    DataUploaderPolicy()
                }
            }
            Given("media uploader use case") {
                every {
                    mediaUploaderUseCase.progressCallback = any()
                } answers {}

                coEvery {
                    mediaUploaderUseCase(any())
                } answers {
                    MediaUploader(data = UploadData(uploadId))
                }
            }
            Then("it should return success") {
                runBlocking {
                    when(val execute = useCase(requestParams)) {
                        is UploadResult.Success -> {
                            assert(execute.uploadId == uploadId)
                        }
                    }
                }
            }
        }

        Scenario("upload data with invalid file") {
            var requestParams = RequestParams()

            Given("request param") {
                requestParams = useCase.createParams(sourceId, File(""))
            }
            Given("data policy with file not found") {
                coEvery {
                    dataPolicyUseCase(RequestParams.EMPTY)
                } answers {
                    DataUploaderPolicy()
                }
            }
            Then("it should return file not found") {
                runBlocking {
                    when(val execute = useCase(requestParams)) {
                        is UploadResult.Error -> {
                            assert(execute.message.isNotEmpty())
                        }
                    }
                }
            }
        }

        Scenario("upload data with invalid extension") {
            var requestParams = RequestParams()

            Given("request param") {
                every { file.exists() } answers { true }
                every { file.path } answers { filePathInvalidExtension }

                requestParams = useCase.createParams(sourceId, file)
            }
            Given("data policy use case") {
                coEvery {
                    dataPolicyUseCase(any())
                } answers {
                    DataUploaderPolicy()
                }
            }
            Given("media uploader use case") {
                coEvery {
                    mediaUploaderUseCase(any())
                } answers {
                    MediaUploader(data = UploadData(uploadId))
                }
            }
            Then("it should return invalid extension") {
                runBlocking {
                    when(val execute = useCase(requestParams)) {
                        is UploadResult.Error -> {
                            assert(execute.message.isNotEmpty())
                        }
                    }
                }
            }
        }

        Scenario("upload data with large file") {
            var requestParams = RequestParams()

            Given("request param") {
                every { file.exists() } answers { true }
                every { file.path } answers { filePath }
                every { file.length() } answers { 500 }

                requestParams = useCase.createParams(sourceId, file)
            }
            Given("data policy use case") {
                coEvery {
                    dataPolicyUseCase(any())
                } answers {
                    val policy = Policy(
                            maxFileSize = 1000,
                            extension = ".jpg,.jpeg"
                    )
                    val sourcePolicy = SourcePolicy(imagePolicy = policy)
                    val uploaderPolicy = UploaderPolicy(sourcePolicy)
                    DataUploaderPolicy(uploaderPolicy)
                }
            }
            Given("media uploader use case") {
                coEvery {
                    mediaUploaderUseCase(any())
                } answers {
                    MediaUploader(data = UploadData(uploadId))
                }
            }
            Then("it should return large file exception") {
                runBlocking {
                    when(val execute = useCase(requestParams)) {
                        is UploadResult.Error -> {
                            assert(execute.message.isNotEmpty())
                        }
                    }
                }
            }
        }
    }

})