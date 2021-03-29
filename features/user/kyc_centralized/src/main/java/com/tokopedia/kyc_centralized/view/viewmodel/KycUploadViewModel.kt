package com.tokopedia.kyc_centralized.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kyc_centralized.data.model.response.KycData
import com.tokopedia.kyc_centralized.domain.KycUploadUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kyc_centralized.util.ImageEncryptionUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.crypto.Cipher
import javax.inject.Inject

class KycUploadViewModel @Inject constructor(
        private val kycUploadUseCase: KycUploadUseCase,
        private val dispatcher: CoroutineDispatchers
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
                    ivKtp?.let { finalKtp = decryptImageKtp(ktpPath) }
                    ivFace?.let { finalFace = decryptImageFace(facePath) }
                }
                val kycUploadResult = kycUploadUseCase.uploadImages(finalKtp, finalFace, tkpdProjectId)
                _kycResponse.postValue(Success(kycUploadResult))
            }
        }) {
            _kycResponse.postValue(Fail(it))
        }
    }

    fun encryptImageKtp(originalFilePath: String) {
        launchCatchError(block = {
            withContext(dispatcher.io) {
                val encryptedImagePath = ImageEncryptionUtil.createCopyOfOriginalFile(originalFilePath)
                val aes = ImageEncryptionUtil.initAesEncrypt()
                //save the Ktp IV for decrypt
                ivKtp = aes.iv
                val createdFile = writeEncryptedResult(originalFilePath, encryptedImagePath, aes)
                _encryptImage.postValue(Success(createdFile))
            }
        }, onError = {
            it.printStackTrace()
            _encryptImage.postValue(Fail(it))
        })
    }

    fun encryptImageFace(originalFilePath: String) {
        launchCatchError(block = {
            withContext(dispatcher.io) {
                val encryptedImagePath = ImageEncryptionUtil.createCopyOfOriginalFile(originalFilePath)
                val aes = ImageEncryptionUtil.initAesEncrypt()
                //save the Face IV for decrypt
                ivFace = aes.iv
                val createdFile = writeEncryptedResult(originalFilePath, encryptedImagePath, aes)
                _encryptImage.postValue(Success(createdFile))
            }
        }, onError = {
            it.printStackTrace()
            _encryptImage.postValue(Fail(it))
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

    private fun decryptImageKtp(originalFilePath: String): String {
        val decryptedFilePath = ImageEncryptionUtil.createCopyOfOriginalFile(originalFilePath)
        val aes = ImageEncryptionUtil.initAesDecrypt(ivKtp)
        val resultPath = writeDecryptedResult(originalFilePath, decryptedFilePath, aes)
        //delete the IV
        ivKtp = null
        return resultPath
    }

    private fun decryptImageFace(originalFilePath: String): String {
        val decryptedFilePath = ImageEncryptionUtil.createCopyOfOriginalFile(originalFilePath)
        val aes = ImageEncryptionUtil.initAesDecrypt(ivFace)
        val resultPath = writeDecryptedResult(originalFilePath, decryptedFilePath, aes)
        //delete the IV
        ivFace = null
        return resultPath
    }

    private fun writeDecryptedResult(originalFilePath: String, decryptedFilePath: String, aes: Cipher): String {
        ImageEncryptionUtil.writeDecryptedImage(originalFilePath, decryptedFilePath, aes)
        return deleteAndRenameResult(originalFilePath, decryptedFilePath)
    }

    companion object {
        var ivKtp: ByteArray? = null
        var ivFace: ByteArray? = null
        const val KYC_USING_ENCRYPT = "android_kyc_enabled_encrypt"
    }
}