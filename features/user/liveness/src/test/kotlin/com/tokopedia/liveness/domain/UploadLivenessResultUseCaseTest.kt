package com.tokopedia.liveness.domain

import com.tokopedia.liveness.data.model.response.LivenessData
import com.tokopedia.liveness.data.repository.LivenessUploadImagesRepository
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class UploadLivenessResultUseCaseTest {
    @RelaxedMockK
    private lateinit var repository: LivenessUploadImagesRepository

    private lateinit var useCase: UploadLivenessResultUseCase

    private val projectId = "1"

    @Before
    fun before() {
        MockKAnnotations.init(this)
        useCase = UploadLivenessResultUseCase(repository)
    }

    @Test
    fun `Successfully upload image`() {
        val expectedResult = LivenessData()

        val ktpImageString = "good ktp image"
        val faceImageString = "good face image"

        coEvery {
            repository.uploadImages(any(), any(), any(), any())
        } coAnswers {
            expectedResult.isSuccessRegister  = true
            expectedResult
        }

        val result = runBlocking {
            useCase.uploadImages(ktpImageString, faceImageString, projectId)
        }

        assertEquals(result, expectedResult)
        assertTrue(result.isSuccessRegister)
    }

    @Test
    fun `Failed to upload image (bad image)`() {
        val expectedResult = mockk<LivenessData>(relaxed = true)
        val ktpImageString = "bad ktp image"
        val faceImageString = "bad face image"

        coEvery {
            repository.uploadImages(any(), any(), any(), any())
        } coAnswers {
            expectedResult.isSuccessRegister  = false
            expectedResult
        }

        val result = runBlocking {
            useCase.uploadImages(ktpImageString, faceImageString, projectId)
        }

        assertEquals(result, expectedResult)
        assertFalse(result.isSuccessRegister)
    }

    @Test
    fun `Failed to upload image (exception)`() {
        val expectedResult = mockk<Exception>(relaxed = true)
        val ktpImageString = "bad ktp image"
        val faceImageString = "bad face image"

        coEvery {
            repository.uploadImages(any(), any(), any(), any())
        } throws expectedResult

        assertFailsWith<Exception> {
            runBlocking {
                useCase.uploadImages(ktpImageString, faceImageString, projectId)
            }
        }
    }
}