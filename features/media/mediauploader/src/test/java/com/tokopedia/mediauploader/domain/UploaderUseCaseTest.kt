package com.tokopedia.mediauploader.domain

import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.data.entity.*
import com.tokopedia.mediauploader.common.state.UploadResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.File
import kotlin.test.assertTrue

class UploaderUseCaseTest {

    private val dataPolicyUseCase = mockk<DataPolicyUseCase>(relaxed = true)
    private val mediaUploaderUseCase = mockk<MediaUploaderUseCase>()
    private val useCase = UploaderUseCase(dataPolicyUseCase, mediaUploaderUseCase)

    private val file = mockk<File>()
    private val sourceId = "WXjxja"
    private val filePath = "image.jpg"
    private val filePathInvalidExtension = "image.jpt"
    private val uploadId = "abc-123-xyz-456"

    @Test fun `It should be able upload image successfully`() {
        // Given
        every { file.exists() } answers { true }
        every { file.path } answers { filePath }

        val requestParams = useCase.createParams(sourceId, file)

        coEvery {
            dataPolicyUseCase(any())
        } answers {
            DataUploaderPolicy()
        }

        coEvery {
            mediaUploaderUseCase(any())
        } answers {
            MediaUploader(data = UploadData(uploadId))
        }

        // Then
        runBlocking {
            when(val execute = useCase(requestParams)) {
                is UploadResult.Success -> {
                    assert(execute.uploadId == uploadId)
                }
            }
        }
    }

    @Test fun `It should not be able to upload image because of invalid file`() {
        // Given
        val requestParams = useCase.createParams(sourceId, File(""))

        coEvery {
            dataPolicyUseCase(any())
        } answers {
            DataUploaderPolicy()
        }

        // Then
        runBlocking {
            when(val execute = useCase(requestParams)) {
                is UploadResult.Error -> {
                    assert(execute.message.isNotEmpty())
                }
            }
        }
    }

    @Test fun `It should not be able to upload image because of invalid extension`() {
        // Given
        every { file.exists() } answers { true }
        every { file.path } answers { filePathInvalidExtension }

        val requestParams = useCase.createParams(sourceId, file)

        coEvery {
            dataPolicyUseCase(any())
        } answers {
            DataUploaderPolicy()
        }

        coEvery {
            mediaUploaderUseCase(any())
        } answers {
            MediaUploader(data = UploadData(uploadId))
        }

        // Then
        runBlocking {
            when(val execute = useCase(requestParams)) {
                is UploadResult.Error -> {
                    assert(execute.message.isNotEmpty())
                }
            }
        }
    }

    @Test fun `It should not be able to upload image because of large of file`() {
        // Given
        every { file.exists() } answers { true }
        every { file.path } answers { filePath }
        every { file.length() } answers { 500 }

        val requestParams = useCase.createParams(sourceId, file)

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

        coEvery {
            mediaUploaderUseCase(any())
        } answers {
            MediaUploader(data = UploadData(uploadId))
        }

        // Then
        runBlocking {
            val execute = useCase(requestParams)

            assertTrue { execute is UploadResult.Error }
        }
    }

}