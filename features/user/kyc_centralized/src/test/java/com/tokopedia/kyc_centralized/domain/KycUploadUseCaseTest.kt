package com.tokopedia.kyc_centralized.domain

import com.tokopedia.kyc_centralized.data.model.response.KycData
import com.tokopedia.kyc_centralized.data.repository.KycUploadImagesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class KycUploadUseCaseTest {
    @RelaxedMockK
    private lateinit var repository: KycUploadImagesRepository

    private lateinit var useCase: KycUploadUseCase

    private val projectId = "1"

    @Before
    fun before() {
        MockKAnnotations.init(this)
        useCase = KycUploadUseCase(repository)
    }

    @Test
    fun `Successfully upload image`() {
        val expectedResult = KycData()

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
        Assert.assertTrue(result.isSuccessRegister)
    }

    @Test
    fun `Failed to upload image (bad image)`() {
        val expectedResult = mockk<KycData>(relaxed = true)
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
        Assert.assertFalse(result.isSuccessRegister)
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