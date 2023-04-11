package com.tokopedia.kyc_centralized.ui.tokoKyc.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kyc_centralized.common.KYCConstant.LIVENESS_TAG
import com.tokopedia.kyc_centralized.common.KycServerLogger
import com.tokopedia.kyc_centralized.data.model.KycData
import com.tokopedia.kyc_centralized.domain.KycUploadUseCase
import com.tokopedia.kyc_centralized.util.CipherProvider
import com.tokopedia.kyc_centralized.util.ImageEncryptionUtil
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import com.tokopedia.kyc_centralized.util.KycUploadErrorCodeUtil.FAILED_ENCRYPTION
import com.tokopedia.kyc_centralized.util.KycUploadErrorCodeUtil.FILE_PATH_FACE_EMPTY
import com.tokopedia.kyc_centralized.util.KycUploadErrorCodeUtil.FILE_PATH_KTP_EMPTY
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.crypto.Cipher
import javax.inject.Inject

class KycUploadViewModel @Inject constructor(
    private val kycUploadUseCase: KycUploadUseCase,
    private val dispatcher: CoroutineDispatchers,
    private val kycSharedPreference: KycSharedPreference,
    private val cryptoFactory: CipherProvider,
    private val serverLogger: KycServerLogger
) : BaseViewModel(dispatcher.main) {

    private val _kycResponse = MutableLiveData<Result<KycData>>()
    val kycResponseLiveData: LiveData<Result<KycData>>
        get() = _kycResponse

    private val _encryptImage = MutableLiveData<Result<String>>()
    val encryptImageLiveData: LiveData<Result<String>>
        get() = _encryptImage

    fun uploadImages(
        ktpPath: String,
        facePath: String,
        tkpdProjectId: String,
        isKtpFileUsingEncryption: Boolean,
        isFaceFileUsingEncryption: Boolean
    ) {
        Timber.d("projectId: $tkpdProjectId")
        val startTimeLog = System.currentTimeMillis()
        var encryptionTimeKtp = 0L
        var encryptionTimeFace = 0L
        var finalKtp = ktpPath
        var finalFace = facePath

        launchCatchError(block = {
            withContext(dispatcher.io) {
                Timber.d("$LIVENESS_TAG: Start uploading")

                if (isKtpFileUsingEncryption) {
                    val startTime = System.currentTimeMillis()
                    kycSharedPreference.getByteArrayCache(KYC_IV_KTP_CACHE)?.let { ivKey ->
                        finalKtp = decryptImage(ktpPath, ivKey, KYC_IV_KTP_CACHE)
                    }
                    encryptionTimeKtp = System.currentTimeMillis() - startTime
                }

                if (isFaceFileUsingEncryption) {
                    val startTime = System.currentTimeMillis()
                    kycSharedPreference.getByteArrayCache(KYC_IV_FACE_CACHE)?.let { ivKey ->
                        finalFace = decryptImage(facePath, ivKey, KYC_IV_FACE_CACHE)
                    }
                    encryptionTimeFace = System.currentTimeMillis() - startTime
                }

                when {
                    finalKtp.isEmpty() -> {
                        serverLogger.kycUploadMonitoring(
                            type = FAIL,
                            uploadTime = System.currentTimeMillis() - startTimeLog,
                            encryptionTimeFileKtp = encryptionTimeKtp,
                            encryptionTimeFileFace = encryptionTimeFace,
                            fileKtp = finalKtp,
                            fileFace = finalFace,
                            message = FILE_PATH_KTP_EMPTY
                        )
                        _kycResponse.postValue(Fail(Throwable(FILE_PATH_KTP_EMPTY)))
                    }
                    finalFace.isEmpty() -> {
                        serverLogger.kycUploadMonitoring(
                            type = FAIL,
                            uploadTime = System.currentTimeMillis() - startTimeLog,
                            encryptionTimeFileKtp = encryptionTimeKtp,
                            encryptionTimeFileFace = encryptionTimeFace,
                            fileKtp = finalKtp,
                            fileFace = finalFace,
                            message = FILE_PATH_FACE_EMPTY
                        )
                        _kycResponse.postValue(Fail(Throwable(FILE_PATH_FACE_EMPTY)))
                    }
                    else -> {
                        val result =
                            kycUploadUseCase.uploadImages(finalKtp, finalFace, tkpdProjectId)
                        if (result.header?.message?.size.orZero() > 0) {
                            val message = result.header?.message?.get(0).orEmpty()
                            serverLogger.kycUploadMonitoring(
                                type = ERROR_HEADER,
                                uploadTime = System.currentTimeMillis() - startTimeLog,
                                encryptionTimeFileKtp = encryptionTimeKtp,
                                encryptionTimeFileFace = encryptionTimeFace,
                                fileKtp = finalKtp,
                                fileFace = finalFace,
                                message = String.format(
                                    "%s (%s)",
                                    message,
                                    result.header?.errorCode.orEmpty()
                                )
                            )
                            _kycResponse.postValue(Fail(MessageErrorException(message)))
                        } else {
                            if (result.data.isSuccessRegister) {
                                serverLogger.kycUploadMonitoring(
                                    type = SUCCESS,
                                    uploadTime = System.currentTimeMillis() - startTimeLog,
                                    encryptionTimeFileKtp = encryptionTimeKtp,
                                    encryptionTimeFileFace = encryptionTimeFace,
                                    fileKtp = finalKtp,
                                    fileFace = finalFace
                                )
                            } else {
                                serverLogger.kycUploadMonitoring(
                                    type = FAIL,
                                    uploadTime = System.currentTimeMillis() - startTimeLog,
                                    encryptionTimeFileKtp = encryptionTimeKtp,
                                    encryptionTimeFileFace = encryptionTimeFace,
                                    fileKtp = finalKtp,
                                    fileFace = finalFace,
                                    message = result.data.listMessage.toString()
                                )
                            }
                            _kycResponse.postValue(Success(result.data))
                        }
                    }
                }
            }
        }) {
            serverLogger.kycUploadMonitoring(
                type = FAIL,
                uploadTime = System.currentTimeMillis() - startTimeLog,
                encryptionTimeFileKtp = encryptionTimeKtp,
                encryptionTimeFileFace = encryptionTimeFace,
                fileKtp = finalKtp,
                fileFace = finalFace,
                message = it.message.toString()
            )
            _kycResponse.postValue(Fail(it))
        }
    }

    fun encryptImage(originalFilePath: String, ivCache: String) {
        launchCatchError(block = {
            withContext(dispatcher.io) {
                val encryptedImagePath =
                    ImageEncryptionUtil.createCopyOfOriginalFile(originalFilePath)
                val aes = cryptoFactory.initAesEncrypt()
                //save the Ktp IV for decrypt
                kycSharedPreference.saveByteArrayCache(ivCache, aes.iv)
                val createdFile = writeEncryptedResult(originalFilePath, encryptedImagePath, aes)
                _encryptImage.postValue(Success(createdFile))

                Timber.d(
                    "$LIVENESS_TAG: Start encrypting %s, %s(%s)",
                    originalFilePath.substringAfterLast("/"),
                    ivCache,
                    aes.iv
                )
            }
        }, onError = {
            _encryptImage.postValue(Fail(Throwable("$FAILED_ENCRYPTION : on encrypt $originalFilePath; error: ${it.message}")))
            Timber.d(
                "$LIVENESS_TAG: Failed encrypting %s, %s",
                originalFilePath.substringAfterLast("/"),
                ivCache
            )
        })
    }

    fun writeEncryptedResult(
        originalFilePath: String,
        encryptedImagePath: String,
        aes: Cipher
    ): String {
        ImageEncryptionUtil.writeEncryptedImage(originalFilePath, encryptedImagePath, aes)
        return deleteAndRenameResult(originalFilePath, encryptedImagePath)
    }

    private fun deleteAndRenameResult(originalFilePath: String, resultFilePath: String): String {
        //Delete original file
        ImageEncryptionUtil.deleteFile(originalFilePath)
        //Rename encrypted image file to original name
        return ImageEncryptionUtil.renameImageToOriginalFileName(resultFilePath)
    }

    fun decryptImage(originalFilePath: String, iv: ByteArray, ivCache: String): String {
        Timber.d(
            "$LIVENESS_TAG: Start decrypting %s, %s(%s)",
            originalFilePath.substringAfterLast("/"),
            ivCache,
            iv
        )
        val decryptedFilePath = ImageEncryptionUtil.createCopyOfOriginalFile(originalFilePath)
        val aes = cryptoFactory.initAesDecrypt(iv)
        val resultPath = writeDecryptedResult(originalFilePath, decryptedFilePath, aes)
        //delete the IV
        kycSharedPreference.removeCache(ivCache)
        return resultPath
    }

    private fun writeDecryptedResult(
        originalFilePath: String,
        decryptedFilePath: String,
        aes: Cipher
    ): String {
        ImageEncryptionUtil.writeDecryptedImage(originalFilePath, decryptedFilePath, aes)
        return deleteAndRenameResult(originalFilePath, decryptedFilePath)
    }

    companion object {
        const val KYC_USING_ENCRYPT = "android_kyc_enabled_encrypt"
        const val KYC_IV_KTP_CACHE = "android_kyc_iv_ktp"
        const val KYC_IV_FACE_CACHE = "android_kyc_iv_face"

        private const val SUCCESS = "Success"
        private const val FAIL = "Fail"
        private const val ERROR_HEADER = "ErrorHeader"

        private const val LIMIT_MESSAGE = 1000
    }
}
