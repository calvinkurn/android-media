package com.tokopedia.kyc_centralized.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kyc_centralized.data.model.response.KycData
import com.tokopedia.kyc_centralized.domain.KycUploadUseCase
import com.tokopedia.kyc_centralized.util.ImageEncryptionUtil
import com.tokopedia.kyc_centralized.view.viewmodel.KycUploadViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import javax.crypto.Cipher
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
    private val originalImagePath = "OriginalPath"
    private val encryptedImagePath = "SuccessPath"

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewModel = spyk(KycUploadViewModel(useCase, CoroutineTestDispatchersProvider))
    }

    private fun provideEveryUseCase(kycData: KycData) {
        coEvery {
            useCase.uploadImages(any(), any(), any())
        } answers {
            kycData
        }
    }

    private fun provideEverySuccessEncrypt() {
        coEvery {
            viewModel.encryptImageKtp(any())
        } answers {
            Success(encryptedImagePath)
        }

        coEvery {
            viewModel.encryptImageFace(any())
        } answers {
            Success(encryptedImagePath)
        }
    }

    private fun uploadWithEncrypt() {
        viewModel.encryptImageKtp(originalImagePath)
        viewModel.encryptImageFace(originalImagePath)
        viewModel.uploadImages(ktpPath, facePath, projectId, true)
    }

    private fun assertResult(result: Result<KycData>?, kycData: KycData) {
        Assert.assertEquals(result, Success(kycData))
        Assert.assertTrue((result as Success).data.isSuccessRegister)
    }

    private fun assertResultFail(result: Result<KycData>?, kycData: KycData) {
        Assert.assertEquals(result, Success(kycData))
        Assert.assertFalse((result as Success).data.isSuccessRegister)
    }

    @Test
    fun `Register - Success upload image and accepted`() {
        val kycData = KycData(isSuccessRegister = true)
        provideEveryUseCase(kycData)

        viewModel.uploadImages(ktpPath, facePath, projectId, false)
        val result = viewModel.kycResponseLiveData.value
        assertResult(result, kycData)
    }

    @Test
    fun `Register - Success upload image and accepted with encrypt`() {
        val kycData = KycData(isSuccessRegister = true)
        provideEveryUseCase(kycData)
        provideEverySuccessEncrypt()

        uploadWithEncrypt()
        val result = viewModel.kycResponseLiveData.value
        assertResult(result, kycData)
    }

    @Test
    fun `Register - Success upload image but rejected`() {
        val kycData = KycData(isSuccessRegister = false)
        provideEveryUseCase(kycData)

        viewModel.uploadImages(ktpPath, facePath, projectId, false)
        val result = viewModel.kycResponseLiveData.value
        assertResultFail(result, kycData)
    }

    @Test
    fun `Register - Success upload image but rejected with encrypt`() {
        val kycData = KycData(isSuccessRegister = false)
        provideEveryUseCase(kycData)
        provideEverySuccessEncrypt()

        uploadWithEncrypt()
        val result = viewModel.kycResponseLiveData.value
        assertResultFail(result, kycData)
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

    private fun provideEveryUseCaseThrow(ex: Exception) {
        coEvery {
            useCase.uploadImages(any(), any(), any())
        } throws ex
    }

    private fun provideEveryEncryptFail(ex: Exception) {
        coEvery {
            viewModel.encryptImageKtp(any())
        } answers {
            Fail(ex)
        }
    }

    @Test
    fun `Register - Failed upload image and get exception`() {
        val exceptionMock = mockk<Exception>(relaxed = true)
        provideEveryUseCaseThrow(exceptionMock)

        viewModel.uploadImages(ktpPath, facePath, projectId, false)

        val result = viewModel.kycResponseLiveData.value
        Assert.assertTrue(result is Fail)
    }

    @Test
    fun `Register - Failed upload image and get exception with encrypt`() {
        val exceptionMock = mockk<Exception>(relaxed = true)
        provideEveryUseCaseThrow(exceptionMock)
        provideEveryEncryptFail(exceptionMock)

        viewModel.uploadImages(ktpPath, facePath, projectId, true)

        val result = viewModel.kycResponseLiveData.value
        Assert.assertTrue(result is Fail)
    }

    @Test
    fun `Failed to encrypt Image KTP`() {
        unmockkObject(ImageEncryptionUtil)
        viewModel.encryptImageKtp(ktpPath)
        val encryptResult = viewModel.encryptImageLiveData.value
        Assert.assertTrue(encryptResult is Fail)
    }

    @Test
    fun `Failed to encrypt Image Face`() {
        unmockkObject(ImageEncryptionUtil)
        viewModel.encryptImageFace(facePath)
        val encryptResult = viewModel.encryptImageLiveData.value
        Assert.assertTrue(encryptResult is Fail)
    }

    private fun mockEncryptionUtil() {
        val cipherMock: Cipher = mockk(relaxed = true)
        every {
            ImageEncryptionUtil.writeDecryptedImage(any(), any(), any())
        } answers {
            Unit
        }

        every {
            ImageEncryptionUtil.writeEncryptedImage(any(), any(), any())
        } answers {
            Unit
        }

        every {
            ImageEncryptionUtil.initAesEncrypt()
        } answers {
            cipherMock
        }

        every {
            ImageEncryptionUtil.initAesDecrypt(any())
        } answers {
            cipherMock
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
    }

    @Test
    fun `Success encrypt KTP Image`() {
        mockkObject(ImageEncryptionUtil)
        mockEncryptionUtil()

        viewModel.encryptImageKtp(ktpPath)

        val result = viewModel.encryptImageLiveData.value

        Assert.assertEquals(result, Success(encryptedImagePath))
    }

    @Test
    fun `Success encrypt Face Image`() {
        mockkObject(ImageEncryptionUtil)
        mockEncryptionUtil()

        viewModel.encryptImageKtp(facePath)

        val result = viewModel.encryptImageLiveData.value

        Assert.assertEquals(result, Success(encryptedImagePath))
    }

    @Test
    fun `Success decrypt Image`() {
        val kycData = KycData(isSuccessRegister = false)
        mockkObject(ImageEncryptionUtil)
        mockEncryptionUtil()

        KycUploadViewModel.ivKtp = encryptedImagePath.encodeToByteArray()
        KycUploadViewModel.ivFace = encryptedImagePath.encodeToByteArray()

        provideEveryUseCase(kycData)
        provideEverySuccessEncrypt()

        uploadWithEncrypt()

        val result = viewModel.kycResponseLiveData.value

        Assert.assertEquals(result, Success(kycData))
        Assert.assertFalse((result as Success).data.isSuccessRegister)
    }

    @Test
    fun `Failed to decrypt Image`() {
        viewModel.uploadImages(ktpPath, facePath, projectId, true)
        val result = viewModel.kycResponseLiveData.value
        Assert.assertTrue(result is Fail)
    }
}