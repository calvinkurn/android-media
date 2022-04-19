package com.tokopedia.kyc_centralized.util

import com.tokopedia.network.exception.MessageErrorException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object KycUploadErrorCodeUtil {
    private const val NUMBER_EXCEPTION = -99
    private const val KYC_UPLOAD_400 = -1
    private const val KYC_UPLOAD_401 = -2
    private const val KYC_UPLOAD_403 = -3
    private const val KYC_UPLOAD_408 = -4
    private const val KYC_UPLOAD_500 = -5
    private const val KYC_UPLOAD_502 = -6
    private const val KYC_UPLOAD_504 = -7
    private const val KYC_UPLOAD_UNKNOWNHOST = -8
    private const val KYC_UPLOAD_SOCKET_TIMEOUT = -9
    private const val KYC_UPLOAD_MESSAGE_ERROR = -10
    private const val KYC_UPLOAD_IO_EXCEPTION = -11
    private const val KYC_UPLOAD_ERROR_CODE_DEFAULT = -12
    const val KYC_UPLOAD_ERROR_ENCRYPT_DECRYPT = -13

    const val FAILED_ENCRYPTION = "Encryption gagal"
    const val FILE_PATH_KTP_EMPTY = "file ktp tidak ditemukan"
    const val FILE_PATH_FACE_EMPTY = "file selfie/liveness tidak ditemukan"

    fun getErrorCode(e: Throwable): Int {
        if (e is UnknownHostException) {
            return KYC_UPLOAD_UNKNOWNHOST
        } else if (e is SocketTimeoutException) {
            return KYC_UPLOAD_SOCKET_TIMEOUT
        } else if (e is RuntimeException && e.getLocalizedMessage() != null && e.getLocalizedMessage() != "" && e.getLocalizedMessage()?.length?: 0 <= 3) {
            return try {
                when (e.localizedMessage?.toInt()) {
                    400 -> KYC_UPLOAD_400
                    401 -> KYC_UPLOAD_401
                    403 -> KYC_UPLOAD_403
                    408 -> KYC_UPLOAD_408
                    500 -> KYC_UPLOAD_500
                    502 -> KYC_UPLOAD_502
                    504 -> KYC_UPLOAD_504
                    else -> KYC_UPLOAD_ERROR_CODE_DEFAULT
                }
            } catch (ex: NumberFormatException) {
                NUMBER_EXCEPTION
            }
        } else if (e is MessageErrorException && e.message?.isNotEmpty() == true) {
            return KYC_UPLOAD_MESSAGE_ERROR
        } else if (e is IOException) {
            return if(e.message?.contains("AEADBadTagException", ignoreCase = true) == true) {
                KYC_UPLOAD_ERROR_ENCRYPT_DECRYPT
            } else {
                KYC_UPLOAD_IO_EXCEPTION
            }
        }
        return KYC_UPLOAD_ERROR_CODE_DEFAULT
    }
}