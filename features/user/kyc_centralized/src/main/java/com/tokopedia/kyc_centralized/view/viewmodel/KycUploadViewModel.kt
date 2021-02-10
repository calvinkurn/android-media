package com.tokopedia.kyc_centralized.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kyc_centralized.data.model.response.KycData
import com.tokopedia.kyc_centralized.domain.KycUploadUseCase
import com.tokopedia.kyc_centralized.util.DispatcherProvider
import com.tokopedia.kyc_centralized.util.ImageEncryptionUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class KycUploadViewModel @Inject constructor(
        private val kycUploadUseCase: KycUploadUseCase,
        private val dispatcher: DispatcherProvider
) : BaseViewModel(dispatcher.main()) {

    private val _kycResponse = MutableLiveData<Result<KycData>>()
    val kycResponseLiveData : LiveData<Result<KycData>>
        get() = _kycResponse

    private val _encryptImage = MutableLiveData<Result<String>>()
    val encryptImageLiveData : LiveData<Result<String>>
        get() = _encryptImage

    fun uploadImages(ktpPath: String, facePath: String, tkpdProjectId: String, isUsingEncrypt: Boolean) {
        launchCatchError(block = {
            withContext(dispatcher.io()) {
                var finalKtp = ktpPath
                var finalFace = facePath
                if(isUsingEncrypt) {
                    finalKtp = decryptImage(ktpPath, isKtpImage = true)
                    finalFace = decryptImage(facePath, isKtpImage = false)
                }
                val kycUploadResult = kycUploadUseCase.uploadImages(finalKtp, finalFace, tkpdProjectId)
                _kycResponse.postValue(Success(kycUploadResult))
            }
        }) {
            _kycResponse.postValue(Fail(it))
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun encryptImage(originalFilePath: String, isKtpImage: Boolean) {
        launchCatchError(block = {
            withContext(dispatcher.io()) {
                Log.d("ENCRYPT-START", "${System.currentTimeMillis()}")
                val encryptedImagePath = ImageEncryptionUtil.createCopyOfOriginalFile(originalFilePath)
                val fis = FileInputStream(originalFilePath)
                val aes = Cipher.getInstance(ImageEncryptionUtil.ALGORITHM)
                aes.init(Cipher.ENCRYPT_MODE, ImageEncryptionUtil.getKey())
                //save the IV for decrypt
                if(isKtpImage) {
                    ivKtp = aes.iv
                } else {
                    ivFace = aes.iv
                }

                val fs = FileOutputStream(File(encryptedImagePath))
                val out = CipherOutputStream(fs, aes)
                out.write(fis.readBytes(1024 * 1024))
                out.flush()
                out.close()

                //Delete original file
                ImageEncryptionUtil.deleteFile(originalFilePath)

                //Rename encrypted image file to original name
                val createdFile = ImageEncryptionUtil.renameImageToOriginalFileName(encryptedImagePath)
                _encryptImage.postValue(Success(createdFile))
                Log.d("ENCRYPT-END", "${System.currentTimeMillis()}")
                Log.d("RESULT-ENCRYPT", createdFile)
            }
        }, onError = {
            Log.d("RESULT-ENCRYPT", it.message.toString())
            it.printStackTrace()
            _encryptImage.postValue(Fail(it))
        })
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private fun decryptImage(originalFilePath: String, isKtpImage: Boolean): String {
        Log.d("DECRYPT-START", "${System.currentTimeMillis()}")
        val decryptedFilePath = ImageEncryptionUtil.createCopyOfOriginalFile(originalFilePath)
        val fis = FileInputStream(originalFilePath)
        val aes = Cipher.getInstance(ImageEncryptionUtil.ALGORITHM)
        //Get the right IV
        val tempIv: ByteArray? = if(isKtpImage) {
            ivKtp
        } else {
            ivFace
        }
        val spec = GCMParameterSpec(ImageEncryptionUtil.IV_SIZE, tempIv)
        aes.init(Cipher.DECRYPT_MODE, ImageEncryptionUtil.getKey(), spec)

        val out = CipherInputStream(fis, aes)
        File(decryptedFilePath).outputStream().use {
            out.copyTo(it)
        }

        //Delete encrypted file and IV
        ImageEncryptionUtil.deleteFile(originalFilePath)
        if(isKtpImage) {
            ivKtp = null
        } else {
            ivFace = null
        }

        val createdFile = ImageEncryptionUtil.renameImageToOriginalFileName(decryptedFilePath)
        Log.d("DECRYPT-END", "${System.currentTimeMillis()}")
        Log.d("RESULT-DECRYPT", createdFile)

        return createdFile
    }

    companion object {
        private var ivKtp: ByteArray? = null
        private var ivFace: ByteArray? = null
    }
}