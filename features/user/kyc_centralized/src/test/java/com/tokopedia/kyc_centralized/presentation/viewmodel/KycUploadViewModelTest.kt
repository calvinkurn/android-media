package com.tokopedia.kyc_centralized.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kyc_centralized.common.KycServerLogger
import com.tokopedia.kyc_centralized.data.model.KycData
import com.tokopedia.kyc_centralized.data.model.KycResponse
import com.tokopedia.kyc_centralized.domain.KycUploadUseCase
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.KycUploadViewModel
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.KycUploadViewModel.Companion.KYC_IV_FACE_CACHE
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.KycUploadViewModel.Companion.KYC_IV_KTP_CACHE
import com.tokopedia.kyc_centralized.util.CipherProviderImpl
import com.tokopedia.kyc_centralized.util.ImageEncryptionUtil
import com.tokopedia.kyc_centralized.util.KycSharedPreferenceImpl
import com.tokopedia.kyc_centralized.util.KycUploadErrorCodeUtil
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.crypto.Cipher
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class KycUploadViewModelTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var useCase: KycUploadUseCase

    @RelaxedMockK
    private lateinit var sharedPreference: KycSharedPreferenceImpl

    @RelaxedMockK
    private lateinit var cipherProviderImpl: CipherProviderImpl

    @RelaxedMockK
    private lateinit var serverLogger: KycServerLogger

    private lateinit var viewModel: KycUploadViewModel

    private val ktpPath = "test ktp path"
    private val facePath = "test face path"
    private val projectId = "1"
    private val originalImagePath = "OriginalPath"
    private val encryptedImagePath = "SuccessPath"

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewModel = spyk(
            KycUploadViewModel(
            useCase,
            CoroutineTestDispatchersProvider,
            sharedPreference,
            cipherProviderImpl,
            serverLogger
        )
        )
    }

    private fun provideEveryUseCase(kycResponse: KycResponse) {
        coEvery {
            useCase.uploadImages(any(), any(), any())
        } answers {
            kycResponse
        }
    }

    private fun provideEverySuccessEncrypt() {
        coEvery {
            viewModel.encryptImage(any(), any())
        } answers {
            Success(encryptedImagePath)
        }
    }

    private fun provideEverySuccessDecrypted(filePath: String) {
        coEvery {
            viewModel.decryptImage(any(), any(), any())
        } answers {
            filePath
        }
    }

    private fun uploadWithEncrypt() {
        viewModel.encryptImage(originalImagePath, KYC_IV_KTP_CACHE)
        viewModel.encryptImage(originalImagePath, KYC_IV_FACE_CACHE)
        viewModel.uploadImages(
            ktpPath,
            facePath,
            projectId,
            isKtpFileUsingEncryption = true,
            isFaceFileUsingEncryption = true
        )
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
        val kycResponse = KycResponse().apply {
            data.isSuccessRegister = true
        }
        provideEveryUseCase(kycResponse)

        coEvery {
            sharedPreference.getByteArrayCache(any())
        } answers {
            encryptedImagePath.encodeToByteArray()
        }

        viewModel.uploadImages(
            ktpPath,
            facePath,
            projectId,
            isKtpFileUsingEncryption = false,
            isFaceFileUsingEncryption = false
        )
        val result = viewModel.kycResponseLiveData.value
        assertResult(result, kycResponse.data)
    }

    @Test
    fun `Register - Success upload image and accepted with encrypt`() {
        val kycResponse = KycResponse().apply {
            data.isSuccessRegister = true
        }
        provideEveryUseCase(kycResponse)
        provideEverySuccessEncrypt()
        provideEverySuccessDecrypted(originalImagePath)

        coEvery {
            sharedPreference.getByteArrayCache(any())
        } answers {
            encryptedImagePath.encodeToByteArray()
        }

        uploadWithEncrypt()
        val result = viewModel.kycResponseLiveData.value
        assertResult(result, kycResponse.data)
    }

    @Test
    fun `Register - Success upload image but rejected`() {
        val kycResponse = KycResponse().apply {
            data.isSuccessRegister = false
        }
        provideEveryUseCase(kycResponse)

        viewModel.uploadImages(
            ktpPath,
            facePath,
            projectId,
            isKtpFileUsingEncryption = false,
            isFaceFileUsingEncryption = false
        )
        val result = viewModel.kycResponseLiveData.value
        assertResultFail(result, kycResponse.data)
    }

    @Test
    fun `Register - Success upload image but rejected with encrypt`() {
        val kycResponse = KycResponse().apply {
            data.isSuccessRegister = false
        }
        provideEveryUseCase(kycResponse)
        provideEverySuccessEncrypt()
        provideEverySuccessDecrypted(originalImagePath)

        coEvery {
            sharedPreference.getByteArrayCache(any())
        } answers {
            encryptedImagePath.encodeToByteArray()
        }

        uploadWithEncrypt()
        val result = viewModel.kycResponseLiveData.value
        assertResultFail(result, kycResponse.data)
    }

    @Test
    fun `Register - Failed and get error header response`() {
        val kycResponse = KycResponse().apply {
            header?.errorCode = "9999"
            header?.message = mutableListOf(
                "Error message on header 1",
                "Error message on header 2"
            )
        }

        provideEveryUseCase(kycResponse)
        provideEverySuccessEncrypt()
        provideEverySuccessDecrypted(originalImagePath)

        coEvery {
            sharedPreference.getByteArrayCache(any())
        } answers {
            encryptedImagePath.encodeToByteArray()
        }

        uploadWithEncrypt()
        val result = viewModel.kycResponseLiveData.value
        assert(result is Fail)
    }

    @Test
    fun `API - get error response`() {
        val exceptionMock = Exception("Oops!")

        coEvery {
            viewModel.uploadImages(any(), any(), any(), any(), any())
        } throws exceptionMock

        assertFailsWith<Exception> {
            viewModel.uploadImages(
                ktpPath,
                facePath,
                projectId,
                isKtpFileUsingEncryption = true,
                isFaceFileUsingEncryption = true
            )
        }
    }

    private fun provideEveryUseCaseThrow(ex: Exception) {
        coEvery {
            useCase.uploadImages(any(), any(), any())
        } throws ex
    }

    private fun provideEveryEncryptFail(ex: Exception) {
        coEvery {
            viewModel.encryptImage(any(), any())
        } answers {
            Fail(ex)
        }
    }

    @Test
    fun `Register - Failed upload image and get exception`() {
        val exceptionMock = mockk<Exception>(relaxed = true)
        provideEveryUseCaseThrow(exceptionMock)

        viewModel.uploadImages(
            ktpPath,
            facePath,
            projectId,
            isKtpFileUsingEncryption = false,
            isFaceFileUsingEncryption = false
        )

        val result = viewModel.kycResponseLiveData.value
        Assert.assertTrue(result is Fail)
    }

    @Test
    fun `Register - Failed upload image and get exception with encrypt`() {
        val exceptionMock = mockk<Exception>(relaxed = true)
        provideEveryUseCaseThrow(exceptionMock)
        provideEveryEncryptFail(exceptionMock)

        viewModel.uploadImages(
            ktpPath,
            facePath,
            projectId,
            isKtpFileUsingEncryption = true,
            isFaceFileUsingEncryption = true
        )

        val result = viewModel.kycResponseLiveData.value
        Assert.assertTrue(result is Fail)
    }

    @Test
    fun `Failed to encrypt Image KTP`() {
        unmockkObject(ImageEncryptionUtil)
        viewModel.encryptImage(ktpPath, KYC_IV_KTP_CACHE)
        val encryptResult = viewModel.encryptImageLiveData.value
        Assert.assertTrue(encryptResult is Fail)
    }

    @Test
    fun `Failed to encrypt Image Face`() {
        unmockkObject(ImageEncryptionUtil)
        viewModel.encryptImage(facePath, KYC_IV_FACE_CACHE)
        val encryptResult = viewModel.encryptImageLiveData.value
        Assert.assertTrue(encryptResult is Fail)
    }

    private fun mockEncryptionUtil() {
        val cipherMock: Cipher = mockk(relaxed = true)
        coEvery {
            ImageEncryptionUtil.writeDecryptedImage(any(), any(), any())
        } answers {
            Unit
        }

        coEvery {
            ImageEncryptionUtil.writeEncryptedImage(any(), any(), any())
        } answers {
            Unit
        }

        coEvery {
            cipherProviderImpl.initAesEncrypt()
        } answers {
            cipherMock
        }

        coEvery {
            cipherProviderImpl.initAesDecrypt(any())
        } answers {
            cipherMock
        }

        coEvery {
            ImageEncryptionUtil.renameImageToOriginalFileName(any())
        } answers {
            encryptedImagePath
        }

        coEvery {
            ImageEncryptionUtil.createCopyOfOriginalFile(any())
        } answers {
            encryptedImagePath
        }
    }

    @Test
    fun `Success encrypt KTP Image`() {
        mockkObject(ImageEncryptionUtil)
        mockEncryptionUtil()

        viewModel.encryptImage(ktpPath, KYC_IV_KTP_CACHE)

        val result = viewModel.encryptImageLiveData.value

        Assert.assertEquals(result, Success(encryptedImagePath))
    }

    @Test
    fun `Success encrypt Face Image`() {
        mockkObject(ImageEncryptionUtil)
        mockEncryptionUtil()

        viewModel.encryptImage(facePath, KYC_IV_FACE_CACHE)

        val result = viewModel.encryptImageLiveData.value

        Assert.assertEquals(result, Success(encryptedImagePath))
    }

    @Test
    fun `Success decrypt Image`() {
        val kycResponse = KycResponse().apply {
            data.isSuccessRegister = false
        }
        mockkObject(ImageEncryptionUtil)
        mockEncryptionUtil()

        coEvery {
            sharedPreference.getByteArrayCache(any())
        } answers {
            encryptedImagePath.encodeToByteArray()
        }

        provideEveryUseCase(kycResponse)
        provideEverySuccessEncrypt()
        provideEverySuccessDecrypted(originalImagePath)

        uploadWithEncrypt()

        val result = viewModel.kycResponseLiveData.value

        assert(result is Success)
        assertEquals(Success(kycResponse.data), result)
        assertFalse { (result as Success).data.isSuccessRegister }
    }

    @Test
    fun `Failed to decrypt Image`() {
        viewModel.uploadImages(
            ktpPath,
            facePath,
            projectId,
            isKtpFileUsingEncryption = true,
            isFaceFileUsingEncryption = true
        )

        val result = viewModel.kycResponseLiveData.value
        Assert.assertTrue(result is Fail)
    }

    @Test
    fun `failed file path krp empty`() {
        mockkObject(ImageEncryptionUtil)
        mockEncryptionUtil()

        coEvery { viewModel.decryptImage(ktpPath, any(), any()) } returns ""

        uploadWithEncrypt()

        val result = viewModel.kycResponseLiveData.value
        assert(result is Fail)
    }

    @Test
    fun `failed file path face empty`() {
        mockkObject(ImageEncryptionUtil)
        mockEncryptionUtil()

        coEvery { viewModel.decryptImage(facePath, any(), any()) } returns ""

        uploadWithEncrypt()

        val result = viewModel.kycResponseLiveData.value
        assert(result is Fail)
    }

    @Test
    fun `upload failed - on decryption ktp file`() {
        coEvery {
            sharedPreference.getByteArrayCache(any())
        } throws Throwable(KycUploadErrorCodeUtil.FAILED_ENCRYPTION)

        viewModel.uploadImages(
            ktpPath,
            facePath,
            projectId,
            isKtpFileUsingEncryption = true,
            isFaceFileUsingEncryption = true
        )

        assert(viewModel.kycResponseLiveData.value is Fail)
        val result = viewModel.kycResponseLiveData.value as Fail
        assert(result.throwable.message?.contains(KycUploadErrorCodeUtil.FAILED_ENCRYPTION) == true)
    }

    @Test
    fun `upload failed - on decryption face file`() {
        coEvery {
            sharedPreference.getByteArrayCache(any())
        } throws Throwable(KycUploadErrorCodeUtil.FAILED_ENCRYPTION)

        viewModel.uploadImages(
            ktpPath,
            facePath,
            projectId,
            isKtpFileUsingEncryption = false,
            isFaceFileUsingEncryption = true
        )

        assert(viewModel.kycResponseLiveData.value is Fail)
        val result = viewModel.kycResponseLiveData.value as Fail
        assert(result.throwable.message?.contains(KycUploadErrorCodeUtil.FAILED_ENCRYPTION) == true)
    }

    @Test
    fun `test sharedPref null`() {
        coEvery {
            sharedPreference.getByteArrayCache(any())
        } returns null

        uploadWithEncrypt()
        assert(viewModel.kycResponseLiveData.value is Fail)
    }

    @Test
    fun `error on header but empty message`() {
        val kycResponse = KycResponse().apply {
            header?.message = mutableListOf("")
            header?.errorCode = null
        }

        provideEveryUseCase(kycResponse)
        provideEverySuccessEncrypt()
        provideEverySuccessDecrypted(originalImagePath)

        coEvery {
            sharedPreference.getByteArrayCache(any())
        } answers {
            encryptedImagePath.encodeToByteArray()
        }

        uploadWithEncrypt()
        assert(viewModel.kycResponseLiveData.value is Fail)
    }
}
