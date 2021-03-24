package com.tokopedia.kyc_centralized.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kyc_centralized.data.model.response.KycData
import com.tokopedia.kyc_centralized.domain.KycUploadUseCase
import com.tokopedia.kyc_centralized.util.ImageEncryptionUtil
import com.tokopedia.kyc_centralized.view.viewmodel.KycUploadViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import util.TestAppDispatchProvider
import kotlin.test.assertFailsWith

class KycUploadViewModelTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var useCase: KycUploadUseCase

    private lateinit var viewModel : KycUploadViewModel

    private val ktpPath = "test ktp path"
    private val facePath = "test face path"
    private val projectId = "1"

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewModel = spyk(KycUploadViewModel(useCase, TestAppDispatchProvider))
    }

    @Test
    fun `Register - Success upload image and accepted`() {
        val livenessData = KycData()

        coEvery {
            useCase.uploadImages(any(), any(), any())
        } answers {
            livenessData.isSuccessRegister = true
            livenessData
        }

        viewModel.uploadImages(ktpPath, facePath, projectId, false)

        val result = viewModel.kycResponseLiveData.value
        Assert.assertEquals(result, Success(livenessData))
        Assert.assertTrue((result as Success).data.isSuccessRegister)
    }

    @Test
    fun `Register - Success upload image and accepted with encrypt`() {
        val livenessData = KycData()
        val encryptedImagePath = "SuccessPath"

        coEvery {
            useCase.uploadImages(any(), any(), any())
        } answers {
            livenessData.isSuccessRegister = true
            livenessData
        }

        coEvery {
            viewModel.encryptImage(any(), any())
        } answers {
            Success(encryptedImagePath)
        }

        viewModel.uploadImages(ktpPath, facePath, projectId, true)

        val result = viewModel.kycResponseLiveData.value

        Assert.assertEquals(result, Success(livenessData))
        Assert.assertTrue((result as Success).data.isSuccessRegister)
    }

    @Test
    fun `Register - Success upload image but rejected`() {
        val livenessData = KycData()

        coEvery {
            useCase.uploadImages(any(), any(), any())
        } answers {
            livenessData.isSuccessRegister = false
            livenessData
        }

        viewModel.uploadImages(ktpPath, facePath, projectId, false)

        val result = viewModel.kycResponseLiveData.value
        Assert.assertEquals(result, Success(livenessData))
        Assert.assertFalse((result as Success).data.isSuccessRegister)
    }

    @Test
    fun `Register - Success upload image but rejected with encrypt`() {
        val livenessData = KycData()
        val encryptedImagePath = "SuccessPath"

        coEvery {
            useCase.uploadImages(any(), any(), any())
        } answers {
            livenessData.isSuccessRegister = false
            livenessData
        }

        coEvery {
            viewModel.encryptImage(any(), any())
        } answers {
            Success(encryptedImagePath)
        }

        viewModel.uploadImages(ktpPath, facePath, projectId, true)

        val result = viewModel.kycResponseLiveData.value

        Assert.assertEquals(result, Success(livenessData))
        Assert.assertFalse((result as Success).data.isSuccessRegister)
    }

    @Test
    fun `API - get error response`() {
        val viewModelMock = mockk<KycUploadViewModel>(relaxed = true)
        val exceptionMock = Exception("Oops!")

        coEvery {
            viewModelMock.uploadImages(any(), any(), any(), any())
        } throws exceptionMock

        assertFailsWith<Exception> {
            viewModelMock.uploadImages(ktpPath, facePath, projectId, true)
        }
    }

    @Test
    fun `API - get error response with empty params`() {
        val viewModelMock = mockk<KycUploadViewModel>(relaxed = true)
        val exceptionMock = Exception("Oops!")

        coEvery {
            viewModelMock.uploadImages(any(), any(), any(), any())
        } throws exceptionMock

        assertFailsWith<Exception> {
            viewModelMock.uploadImages("", "", "", true)
        }
    }

    @Test
    fun `Register - Failed upload image and get exception`() {
        val exceptionMock = mockk<Exception>(relaxed = true)

        coEvery {
            useCase.uploadImages(any(), any(), any())
        } throws exceptionMock

        viewModel.uploadImages(ktpPath, facePath, projectId, false)

        val result = viewModel.kycResponseLiveData.value
        Assert.assertTrue(result is Fail)
    }

    @Test
    fun `Register - Failed upload image and get exception with encrypt`() {
        val exceptionMock = mockk<Exception>(relaxed = true)

        coEvery {
            useCase.uploadImages(any(), any(), any())
        } throws exceptionMock

        coEvery {
            viewModel.encryptImage(any(), any())
        } answers {
            Fail(exceptionMock)
        }

        viewModel.uploadImages(ktpPath, facePath, projectId, true)

        val result = viewModel.kycResponseLiveData.value

        Assert.assertTrue(result is Fail)
    }

    @Test
    fun `Failed to encrypt Image`() {
        viewModel.encryptImage(ktpPath, true)
        val encryptResult = viewModel.encryptImageLiveData.value
        Assert.assertTrue(encryptResult is Fail)
    }

    @Test
    fun `Success decrypt Image`() {
        val livenessData = KycData()
        val encryptedImagePath = "SuccessPath"
        mockkObject(ImageEncryptionUtil)
        KycUploadViewModel.ivKtp = encryptedImagePath.encodeToByteArray()
        KycUploadViewModel.ivFace = encryptedImagePath.encodeToByteArray()

        every {
            ImageEncryptionUtil.writeDecryptedImage(any(), any(), any())
        } answers {
            Unit
        }

        every {
            ImageEncryptionUtil.initAesDecrypt(any(), any())
        } answers {
            Unit
        }

        every {
            ImageEncryptionUtil.renameImageToOriginalFileName(any())
        } answers {
            encryptedImagePath
        }

        every {
            ImageEncryptionUtil.createCopyOfOriginalFile(any())
        } answers {
            encryptedImagePath
        }

        coEvery {
            useCase.uploadImages(any(), any(), any())
        } answers {
            livenessData.isSuccessRegister = false
            livenessData
        }

        coEvery {
            viewModel.encryptImage(any(), any())
        } answers {
            Success(encryptedImagePath)
        }

        viewModel.encryptImage(ktpPath, true)
        viewModel.encryptImage(facePath, false)
        viewModel.uploadImages(ktpPath, facePath, projectId, true)

        val result = viewModel.kycResponseLiveData.value

        Assert.assertEquals(result, Success(livenessData))
        Assert.assertFalse((result as Success).data.isSuccessRegister)
    }

    @Test
    fun `Failed to decrypt Image`() {
        viewModel.uploadImages(ktpPath, facePath, projectId, true)
        val result = viewModel.kycResponseLiveData.value
        Assert.assertTrue(result is Fail)
    }
}