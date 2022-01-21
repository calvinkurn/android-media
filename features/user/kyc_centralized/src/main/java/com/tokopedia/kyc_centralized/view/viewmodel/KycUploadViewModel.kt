package com.tokopedia.kyc_centralized.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kyc_centralized.data.model.response.KycData
import com.tokopedia.kyc_centralized.domain.KycUploadUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kyc_centralized.util.ImageEncryptionUtil
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import com.tokopedia.kyc_centralized.util.KycUploadErrorCodeUtil.FAILED_ENCRYPTION
import com.tokopedia.kyc_centralized.util.KycUploadErrorCodeUtil.FILE_PATH_FACE_EMPTY
import com.tokopedia.kyc_centralized.util.KycUploadErrorCodeUtil.FILE_PATH_KTP_EMPTY
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.crypto.Cipher
import javax.inject.Inject

class KycUploadViewModel @Inject constructor(
        private val kycUploadUseCase: KycUploadUseCase,
        private val dispatcher: CoroutineDispatchers,
        private val kycSharedPreference: KycSharedPreference
) : BaseViewModel(dispatcher.main) {

    private val _kycResponse = MutableLiveData<Result<KycData>>()
    val kycResponseLiveData : LiveData<Result<KycData>>
        get() = _kycResponse

    private val _encryptImage = MutableLiveData<Result<String>>()
    val encryptImageLiveData : LiveData<Result<String>>
        get() = _encryptImage

    fun uploadImages(ktpPath: String, facePath: String, tkpdProjectId: String, isUsingEncrypt: Boolean) {
        launchCatchError(block = {
            withContext(dispatcher.io) {
                var finalKtp = ktpPath
                var finalFace = facePath
                if(isUsingEncrypt) {
                    try {
                        kycSharedPreference.getByteArrayCache(KYC_IV_KTP_CACHE)?.let {
                            finalKtp = decryptImage(ktpPath, it, KYC_IV_KTP_CACHE)
                        }
                    } catch (e: Exception) {
                        _kycResponse.postValue(Fail(Throwable("$FAILED_ENCRYPTION : on decrypt file KTP; error: ${e.message}")))
                        return@withContext
                    }

                    try {
                        kycSharedPreference.getByteArrayCache(KYC_IV_FACE_CACHE)?.let {
                            finalFace = decryptImage(facePath, it, KYC_IV_FACE_CACHE)
                        }
                    } catch (e: Exception) {
                        _kycResponse.postValue(Fail(Throwable("$FAILED_ENCRYPTION : on decrypt file Selfie/Liveness; error: ${e.message}")))
                        return@withContext
                    }
                }

                when {
                    finalKtp.isEmpty() -> _kycResponse.postValue(Fail(Throwable(FILE_PATH_KTP_EMPTY)))
                    finalFace.isEmpty() -> _kycResponse.postValue(Fail(Throwable(FILE_PATH_FACE_EMPTY)))
                    else -> {
                        val kycUploadResult = kycUploadUseCase.uploadImages(finalKtp, finalFace, tkpdProjectId)
                        _kycResponse.postValue(Success(kycUploadResult))
                    }
                }
            }
        }) {
            _kycResponse.postValue(Fail(it))
        }
    }

    fun encryptImage(originalFilePath: String, ivCache: String) {
        launchCatchError(block = {
            withContext(dispatcher.io) {
                val encryptedImagePath = ImageEncryptionUtil.createCopyOfOriginalFile(originalFilePath)
                val aes = ImageEncryptionUtil.initAesEncrypt()
                //save the Ktp IV for decrypt
                kycSharedPreference.saveByteArrayCache(ivCache, aes.iv)
                val createdFile = writeEncryptedResult(originalFilePath, encryptedImagePath, aes)
                _encryptImage.postValue(Success(createdFile))
            }
        }, onError = {
            _encryptImage.postValue(Fail(Throwable("$FAILED_ENCRYPTION : on encrypt $originalFilePath; error: ${it.message}")))
        })
    }

    private fun writeEncryptedResult(originalFilePath: String, encryptedImagePath: String, aes: Cipher): String {
        ImageEncryptionUtil.writeEncryptedImage(originalFilePath, encryptedImagePath, aes)
        return deleteAndRenameResult(originalFilePath, encryptedImagePath)
    }

    private fun deleteAndRenameResult(originalFilePath: String, resultFilePath: String): String {
        //Delete original file
        ImageEncryptionUtil.deleteFile(originalFilePath)
        //Rename encrypted image file to original name
        return ImageEncryptionUtil.renameImageToOriginalFileName(resultFilePath)
    }

    private fun decryptImage(originalFilePath: String, iv: ByteArray, ivCache: String): String {
        val decryptedFilePath = ImageEncryptionUtil.createCopyOfOriginalFile(originalFilePath)
        val aes = ImageEncryptionUtil.initAesDecrypt(iv)
        val resultPath = writeDecryptedResult(originalFilePath, decryptedFilePath, aes)
        //delete the IV
        kycSharedPreference.removeCache(ivCache)
        return resultPath
    }

    private fun writeDecryptedResult(originalFilePath: String, decryptedFilePath: String, aes: Cipher): String {
        ImageEncryptionUtil.writeDecryptedImage(originalFilePath, decryptedFilePath, aes)
        return deleteAndRenameResult(originalFilePath, decryptedFilePath)
    }

    companion object {
        const val KYC_USING_ENCRYPT = "android_kyc_enabled_encrypt"
        const val KYC_IV_KTP_CACHE = "android_kyc_iv_ktp"
        const val KYC_IV_FACE_CACHE = "android_kyc_iv_face"
    }
}